class EuclidianDistance {

    private double roll1, roll2, pitchaw1, pitchaw2, yaw1, yaw2, distance;
    public EuclidianDistance(double roll1, double pitchaw1, double yaw1, double roll2, double pitchaw2, double yaw2) {
        this.roll1 = roll1;
        this.pitchaw1 = pitchaw1;
        this.yaw1 = yaw1;
        this.roll2 = roll2;
        this.pitchaw2 = pitchaw2;
        this.yaw2 = yaw2;
    }
    public EuclidianDistance() {
        this.roll1 = 0;
        this.pitchaw1 = 0;
        this.yaw1 = 0;
        this.roll2 = 0;
        this.pitchaw2 = 0;
        this.yaw2 = 0;

    }
    public double euclidianDistance() {
        distance = Math.pow((Math.pow(this.roll2 - this.roll1, 2) + Math.pow(this.pitchaw2 - this.pitchaw1, 2) +  Math.pow(this.yaw2 - this.yaw1, 2) * 1.0), 0.5);
        return distance;
    }

    public double getroll1() {
        return roll1;
    }

    public void setroll1(double roll1) {
        this.roll1 = roll1;
    }

    public double getroll2() {
        return roll2;
    }

    public void setroll2(double roll2) {
        this.roll2 = roll2;
    }

    public double getpitchaw1() {
        return pitchaw1;
    }

    public void setpitchaw1(double pitchaw1) {
        this.pitchaw1 = pitchaw1;
    }

    public double getpitchaw2() {
        return pitchaw2;
    }

    public void setpitchaw2(double pitchaw2) {
        this.pitchaw2 = pitchaw2;
    }

    public double getyaw1() {
        return yaw1;
    }

    public void setyaw1(double yaw1) {
        this.yaw1 = yaw1;
    }

    public double getyaw2() {
        return yaw2;
    }

    public void setyaw2(double yaw2) {
        this.yaw2 = yaw2;
    }

    public double getDistance() {
        euclidianDistance();
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}
