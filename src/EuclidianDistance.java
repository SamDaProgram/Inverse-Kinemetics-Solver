public class EuclidianDistance {

    private double roll1, roll2, pitchaw1, pitchaw2, yaw1, yaw2;
    private double distance;

    // Constructor with initialization
    public EuclidianDistance(double roll1, double pitchaw1, double yaw1, double roll2, double pitchaw2, double yaw2) {
        this.roll1 = roll1;
        this.pitchaw1 = pitchaw1;
        this.yaw1 = yaw1;
        this.roll2 = roll2;
        this.pitchaw2 = pitchaw2;
        this.yaw2 = yaw2;
        this.distance = computeDistance(); // Calculate immediately
    }

    // Default constructor
    public EuclidianDistance() {
        this(0, 0, 0, 0, 0, 0); // Delegate to main constructor
    }

    // Core distance computation
    private double computeDistance() {
        return Math.sqrt(
                Math.pow(this.roll2 - this.roll1, 2) +
                        Math.pow(this.pitchaw2 - this.pitchaw1, 2) +
                        Math.pow(this.yaw2 - this.yaw1, 2)
        );
    }

    // Getter for distance
    public double getDistance() {
        return distance; // No redundant computation
    }

    // Update values and recalculate distance
    public void setRoll1(double roll1) {
        this.roll1 = roll1;
        this.distance = computeDistance();
    }

    public void setRoll2(double roll2) {
        this.roll2 = roll2;
        this.distance = computeDistance();
    }

    public void setPitchaw1(double pitchaw1) {
        this.pitchaw1 = pitchaw1;
        this.distance = computeDistance();
    }

    public void setPitchaw2(double pitchaw2) {
        this.pitchaw2 = pitchaw2;
        this.distance = computeDistance();
    }

    public void setYaw1(double yaw1) {
        this.yaw1 = yaw1;
        this.distance = computeDistance();
    }

    public void setYaw2(double yaw2) {
        this.yaw2 = yaw2;
        this.distance = computeDistance();
    }

    // Getters for individual components (unchanged)
    public double getRoll1() {
        return roll1;
    }

    public double getRoll2() {
        return roll2;
    }

    public double getPitchaw1() {
        return pitchaw1;
    }

    public double getPitchaw2() {
        return pitchaw2;
    }

    public double getYaw1() {
        return yaw1;
    }

    public double getYaw2() {
        return yaw2;
    }
}
