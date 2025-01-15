public class Arm {
    private Joint[] arm = new Joint[]{new Joint(), new Joint(), new Joint(), new Joint(), new Joint(), new Joint()};
    private double[] currentAngle = new double[arm.length];
    private double baseAngle;
    private double distamce;
    private final double PRECISION = 0.0001;  // Higher precision for finer control

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
        arm[0].setLength(15);
        arm[1].setLength(10);
        arm[2].setLength(10);
        arm[3].setLength(40);
        arm[4].setLength(4);
        arm[5].setLength(16);

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
            this.distamce = distance1;
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
            this.distamce = distance1;
        }

        this.baseAngle = optimalAngle;
        System.out.println("Optimal Angle: " + optimalAngle + ", Minimal Distance: " + minimalDistance);
    }

    private double calculateEuclideanDistance(double roll1, double pitch1, double yaw1, double roll2, double pitch2, double yaw2) {
        return Math.sqrt(
                Math.pow(roll2 - roll1, 2) +
                        Math.pow(pitch2 - pitch1, 2) +
                        Math.pow(yaw2 - yaw1, 2)
        );
    }

    private double[] calculareEndEffector(boolean returnValue) {
        double currentAngle = 0;
        double roll = 0;
        double pitch = 0;
        double yaw = 0;

        for (int i = 0; i < arm.length; i++) {
            CircleOffset round = new CircleOffset(arm[i].getLength());
            arm[i].setRotation(currentAngle);
            currentAngle += arm[i].getRelativeAngle();  // Update current angle
            round.calculateDistance(currentAngle);  // Calculate position
            roll += round.getX();
            pitch += round.getY();
            yaw += round.getZ();
        }

        // Add base rotation plate contribution
        CircleOffset round = new CircleOffset(roll);
        round.calculateDistance(baseAngle);
        pitch += round.getY();

        return new double[]{roll, pitch, yaw};  // Return calculated position
    }
    private double getDistance() {
        return this.distamce;
    }

    private double getRadius() {
        double[] coordinates = calculareEndEffector(false);
        if(coordinates[0] > coordinates[1]) {
            return coordinates[0];
        } else if (coordinates[1] > coordinates[0]) {
            return coordinates[1];
        }
        throw new IllegalArgumentException("Invalid");
    }

    public static void main(String[] args) {
        Arm arm = new Arm();
        arm.setArm();

        // Define target position
        double targetYaw = 20;    // X-axis
        double targetPitch = 40;  // Y-axis
        double targetRoll = 0;   // Z-axis


        System.out.println("Initial End Effector Position:");
        double[] initialPosition = arm.calculareEndEffector(true);
        System.out.println("Initial Position: " + java.util.Arrays.toString(initialPosition));

        // Optimize angles for each joint

        do{

            for (int i = arm.arm.length - 1; i >= 0; i--) {
                arm.findMinimalDistance(i, targetYaw, targetPitch, targetRoll);
                System.out.println("Distance: " + arm.getDistance());
            }
            arm.findMinimalDistance(arm.getRadius(), targetYaw, targetPitch, targetRoll);
        } while (Math.abs(arm.getDistance()) > arm.getPRECISION());
        System.out.println("\nFinal End Effector Position:");
        double[] finalPosition = arm.calculareEndEffector(true);
        System.out.println("Final Position: " + java.util.Arrays.toString(finalPosition));
    }
}
