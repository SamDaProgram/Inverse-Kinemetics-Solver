public class Arm {
    private Joint[] arm = new Joint[]{new Joint(), new Joint(),new Joint(), new Joint(),new Joint(), new Joint()};
    private double[] currentAngle = new double[arm.length];
    private double baseAngle;

    private void setArm(){
        //set limits
        arm[0].setLimits(-180,200);
        arm[1].setLimits(-180,200);
        arm[2].setLimits(-180,200);
        arm[3].setLimits(-180,200);
        arm[4].setLimits(-180,300);
        arm[5].setLimits(-100,300);

        //set lengths
        arm[0].setLength(20);
        arm[1].setLength(20);
        arm[2].setLength(20);
        arm[3].setLength(20);
        arm[4].setLength(20);
        arm[5].setLength(20);

       arm[0].setRelativeAngle(3);
       arm[1].setRelativeAngle(10);
       arm[2].setRelativeAngle(2);
       arm[3].setRelativeAngle(5);
       arm[4].setRelativeAngle(4);
       arm[5].setRelativeAngle(8);

       baseAngle = 10;

    }
    private void updateAngle(){
        for (int i = 0; i < arm.length; i++) {
            currentAngle[i] = arm[i].getLength();
        }
    }

    private void findMinimalDistance(int index, double targetYaw, double targetPitch, double targetRoll) {
        final double PRECISION = 0.1;  // Reduced precision
        int rows = (int) ((arm[index].getMaxAngle() - arm[index].getMinAngle()) / PRECISION) + 1;
        double[][] distances = new double[rows][2];

        int j = 0;
        for (double i = arm[index].getMinAngle(); i <= arm[index].getMaxAngle(); i += PRECISION) {
            arm[index].setRelativeAngle(i);  // Set relative angle
            double[] calculated = calculareEndEffector(true);  // Calculate end effector position
            double distance = new EuclidianDistance(targetRoll, targetPitch, targetYaw, calculated[0], calculated[1], calculated[2]).getDistance();
            distances[j][0] = i;  // Store angle
            distances[j][1] = distance;  // Store distance
            j++;
        }

        double[] result = findRowWithLowestDistance(distances);
        arm[index].setRelativeAngle(result[0]);  // Set the angle with the minimal distance
        System.out.println("Optimal Angle: " + result[0] + ", Minimal Distance: " + result[1]);

        for (int i = 0; i < distances.length; i++) {
         //   System.out.println(distances[i][0] + ", " + distances[i][1]);
        }
    }




    private double[] findRowWithLowestDistance(double[][] distances) {
        if (distances == null || distances.length == 0) {
            throw new IllegalArgumentException("The distances array cannot be null or empty.");
        }

        double minDistance = Double.MAX_VALUE;
        int minIndex = -1;
        final double MIN_THRESHOLD = 0.0001;  // You can set a threshold for negligible distances

        for (int i = 0; i < distances.length; i++) {
            if (distances[i][1] < minDistance && distances[i][1] > MIN_THRESHOLD) {
                minDistance = distances[i][1];
                minIndex = i;
            }
        }

        // If no valid minimal distance is found, return the smallest distance (even if it's the threshold)
        if (minIndex == -1) {
            System.out.println("Warning: No significant minimal distance found; using the smallest distance.");
            for (int i = 0; i < distances.length; i++) {
                if (distances[i][1] < minDistance) {
                    minDistance = distances[i][1];
                    minIndex = i;
                }
            }
        }

        double correspondingAngle = distances[minIndex][0]; // Ensure we return the angle with the minimum distance
      //  System.out.println("Optimal Angle: " + correspondingAngle + ", Minimal Distance: " + minDistance); // Debug output
        return new double[]{correspondingAngle, minDistance};
    }




    private void calculareEndEffector(){
        double currentAngle = 0;
        double roll = 0;
        double pitch = 0;
        double yaw= 0;

        for (int i = 0; i < arm.length; i++) {
            CircleOffset round = new CircleOffset(arm[i].getLength());
            arm[i].setRotation(currentAngle);
            currentAngle += arm[i].getRelativeAngle();
            round.calculateDistance(currentAngle);
            roll+= round.getY();
            yaw+= round.getX();
            int j = i +1;
            System.out.println("Position at Joint " + j + ": X "+roll+" Y "+pitch+" Z "+yaw+" Realative Position: "+ round.getAngle() + " Ofset Position: "+ arm[i].getRealAngle());
        }

        //rotation plate
        CircleOffset round = new CircleOffset(roll);
        round.calculateDistance(baseAngle);
        pitch += round.getY();

        System.out.println("Position at End Effector:  Pitch: " + pitch + " Roll: " + roll + " Yaw: " + yaw);
    }
    private double[] calculareEndEffector(boolean returnValue) {
        double currentAngle = 0;
        double roll = 0;
        double pitch = 0;
        double yaw = 0;

        for (int i = 0; i < arm.length; i++) {
            CircleOffset round = new CircleOffset(arm[i].getLength());
            arm[i].setRotation(currentAngle);
            currentAngle += arm[i].getRelativeAngle();  // Sum up relative angles
            round.calculateDistance(currentAngle);  // Calculate new joint position
            roll += round.getX();
            pitch += round.getY();
            yaw += round.getZ();
         //   System.out.println("Position at Joint " + (i + 1) + ": X " + roll + " Y " + pitch + " Z " + yaw + " Realative Position: " + round.getAngle() + " Ofset Position: " + arm[i].getRealAngle());
        }

        // Rotation plate
        CircleOffset round = new CircleOffset(roll);
        round.calculateDistance(baseAngle);
        pitch += round.getY();

      //  System.out.println("Position at End Effector: Pitch: " + pitch + " Roll: " + roll + " Yaw: " + yaw);
        return new double[]{roll, pitch, yaw};  // Return the position
    }

    public static void main(String[] arg){
        Arm arm = new Arm();
        arm.setArm();
        arm.calculareEndEffector();

        for(int i = 0; i<100; i++) {
            arm.findMinimalDistance(4, 49, 30, 40);
            arm.findMinimalDistance(3, 49, 30, 40);
            arm.findMinimalDistance(2, 49, 30, 40);
            arm.findMinimalDistance(1, 49, 30, 40);
            arm.findMinimalDistance(0, 49, 30, 40);
        }

        arm.calculareEndEffector();}


}
