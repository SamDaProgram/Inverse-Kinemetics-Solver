import java.util.Arrays;

public class Arm {
    private Joint[] arm = new Joint[]{new Joint(), new Joint(), new Joint(), new Joint(), new Joint(), new Joint()};
    private double[] currentAngle = new double[arm.length];
    private double baseAngle;
    private double distance;
    private final double PRECISION = 0.001;  // Higher precision for finer control

    public double getPRECISION() {
        return PRECISION;
    }

    private void setArm() {
        // Set limits for each joint
        arm[0].setLimits(-180, 200);
        arm[1].setLimits(-180, 200);
        arm[2].setLimits(-180, 200);
        arm[3].setLimits(-180, 200);
        arm[4].setLimits(-180, 300);
        arm[5].setLimits(-100, 300);

        // Set lengths of each joint
        arm[0].setLength(20);
        arm[1].setLength(20);
        arm[2].setLength(20);
        arm[3].setLength(20);
        arm[4].setLength(20);
        arm[5].setLength(20);

        // Set initial relative angles
        arm[0].setRelativeAngle(3);
        arm[1].setRelativeAngle(10);
        arm[2].setRelativeAngle(2);
        arm[3].setRelativeAngle(5);
        arm[4].setRelativeAngle(4);
        arm[5].setRelativeAngle(8);

        baseAngle = 10; // Set base rotation angle
    }

    private void updateAngle() {
        for (int i = 0; i < arm.length; i++) {
            currentAngle[i] = arm[i].getRelativeAngle();
        }
    }

    private void findMinimalDistance(int index, double targetYaw, double targetPitch, double targetRoll) {
        double minAngle = arm[index].getMinAngle();
        double maxAngle = arm[index].getMaxAngle();
        double optimalAngle = minAngle;
        double minimalDistance = Double.MAX_VALUE;

        while (maxAngle - minAngle > PRECISION) {
            double midAngle1 = minAngle + (maxAngle - minAngle) / 3;
            double midAngle2 = maxAngle - (maxAngle - minAngle) / 3;

            // Test midAngle1
            arm[index].setRelativeAngle(midAngle1);
            double[] calc1 = calculareEndEffector(true);
            double distance1 = calculateEuclideanDistance(targetRoll, targetPitch, targetYaw, calc1[0], calc1[1], calc1[2]);

            // Test midAngle2
            arm[index].setRelativeAngle(midAngle2);
            double[] calc2 = calculareEndEffector(true);
            double distance2 = calculateEuclideanDistance(targetRoll, targetPitch, targetYaw, calc2[0], calc2[1], calc2[2]);

            // Narrow the search range
            if (distance1 < distance2) {
                maxAngle = midAngle2;
                if (distance1 < minimalDistance) {
                    minimalDistance = distance1;
                    optimalAngle = midAngle1;
                }
            } else {
                minAngle = midAngle1;
                if (distance2 < minimalDistance) {
                    minimalDistance = distance2;
                    optimalAngle = minimalDistance;
                }
            }
            this.distance = distance1;
        }

        arm[index].setRelativeAngle(optimalAngle);
        System.out.println("Optimal Angle: " + optimalAngle + ", Minimal Distance: " + minimalDistance);
    }

    private void findMinimalDistance(double length, double targetYaw, double targetPitch, double targetRoll) {
        double minAngle = 0;
        double maxAngle = 360;
        double optimalAngle = minAngle;
        double minimalDistance = Double.MAX_VALUE;

        while (maxAngle - minAngle > PRECISION) {
            double midAngle1 = minAngle + (maxAngle - minAngle) / 3;
            double midAngle2 = maxAngle - (maxAngle - minAngle) / 3;

            // Test midAngle1
            this.baseAngle = midAngle1;
            double[] calc1 = calculareEndEffector(true);
            double distance1 = calculateEuclideanDistance(targetRoll, targetPitch, targetYaw, calc1[0], calc1[1], calc1[2]);

            // Test midAngle2
            this.baseAngle = midAngle2;
            double[] calc2 = calculareEndEffector(true);
            double distance2 = calculateEuclideanDistance(targetRoll, targetPitch, targetYaw, calc2[0], calc2[1], calc2[2]);

            // Narrow the search range
            if (distance1 < distance2) {
                maxAngle = midAngle2;
                if (distance1 < minimalDistance) {
                    minimalDistance = distance1;
                    optimalAngle = midAngle1;
                }
            } else {
                minAngle = midAngle1;
                if (distance2 < minimalDistance) {
                    minimalDistance = distance2;
                    optimalAngle = minimalDistance;
                }
            }
            this.distance = distance1;
        }

        this.baseAngle = optimalAngle;
        System.out.println("Rotating Base Optimal Angle: " + optimalAngle + ", Minimal Distance: " + minimalDistance);
    }

    private double calculateEuclideanDistance(double roll1, double pitch1, double yaw1, double roll2, double pitch2, double yaw2) {
        return Math.sqrt(
                Math.pow(roll2 - roll1, 2) +
                        Math.pow(pitch2 - pitch1, 2) +
                        Math.pow(yaw2 - yaw1, 2)
        );
    }

    public double getBaseAngle() {
        return baseAngle;
    }

    private double[] calculareEndEffector(boolean returnValue) {
        double currentAngle = 0;
        double roll = 0;  // Forward/backward (X-axis)
        double pitch = 0; // Side-to-side (Y-axis)
        double yaw = 0;   // Height (Z-axis)
        double radius = 0;

        // First pass: Calculate in 2D (no yaw yet)
        for (int i = 0; i < arm.length; i++) {
            CircleOffset round = new CircleOffset(arm[i].getLength());
            arm[i].setRotation(currentAngle);
            currentAngle += arm[i].getRelativeAngle();  // Update current angle
            round.calculateDistance(currentAngle);  // Calculate position

            yaw += round.getX();   // Update X (Roll) - Forward/backward
            radius += round.getY();  // Update Y (Pitch) - Side-to-side
        }

        // Now calculate the yaw (height) from the base rotation plate contribution
        // Using the radius from 2D to calculate the final 3D position
        CircleOffset round = new CircleOffset(radius);
        round.calculateDistance(baseAngle); // Update position considering base angle
        // Using radius (from 2D calculation) to figure out pitch and roll
        roll = round.getX();  // Forward/backward movement (Roll)
        pitch = round.getY(); // Side-to-side movement (Pitch)

        return new double[]{roll, pitch, yaw};  // Return calculated position with yaw preserved
    }

    private double getDistance() {
        return this.distance;
    }

    private double getRadius() {
        try {
            double[] coordinates = calculareEndEffector(false);
            if (coordinates[0] > coordinates[1]) {
                return coordinates[0];
            } else {
                return coordinates[1];
            }
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        Arm arm = new Arm();
        arm.setArm();

        // Define target position (ensure these are in the correct axis order)
        double targetYaw = 70;    // Height (Z-axis)
        double targetPitch = 0;   // Side-to-side (Y-axis)
        double targetRoll = 10;    // Forward/backward (X-axis)

        System.out.println("Initial End Effector Position:");
        double[] initialPosition = arm.calculareEndEffector(true);
        System.out.println("Initial Position: " + Arrays.toString(initialPosition));

        // Optimize angles for each joint
        do {
            for (int i = arm.arm.length - 1; i >= 0; i--) {
                arm.findMinimalDistance(i, targetYaw, targetPitch, targetRoll);
                double[] position = arm.calculareEndEffector(false);
                System.out.println("Distance: " + arm.getDistance());
                System.out.println("Position: Pitch " + position[0] + ", Roll " + position[1] + ", Yaw " + position[2]);
            }
            arm.findMinimalDistance(arm.getRadius(), targetYaw, targetPitch, targetRoll);

        } while (Math.abs(arm.getDistance()) > arm.getPRECISION());
        System.out.println("\nFinal End Effector Position:");
        double[] finalPosition = arm.calculareEndEffector(true);
        System.out.println("Final Position: " + Arrays.toString(finalPosition));
        System.out.println("Optimal Position Base: " + arm.getBaseAngle());
        double[] optimalPosition = new double[arm.arm.length];
        for (int i = 0; i < arm.arm.length; i++) {
            optimalPosition[i] = arm.arm[i].getRelativeAngle();
            System.out.println("Optimal Position Joint "+i +": " + optimalPosition[i]+" degrees");
        }
    }
}
