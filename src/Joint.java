
public class Joint {
    private double minAngle;
    private double maxAngle;
    private double relativeAngle = 0; // Angle relative to the previous joint
    private double length;
    private double width;
    private double height;
    private double realAngle;
    private double rotation;

    public double getRotation() {
        return rotation;
    }

    public void setRotation(double rotation) {
        this.rotation = rotation;
        this.realAngle = rotation +relativeAngle;
    }

    public Joint() {
    }

    public double getRealAngle() {
        return realAngle;
    }

    public void setRealAngle(double realAngle) {
        this.realAngle = realAngle;
    }


    public Joint(double minAngle, double maxAngle, double length) {
        if (minAngle > maxAngle) {
            throw new IllegalArgumentException("minAngle must be less than or equal to maxAngle");
        }
        if (length <= 0) {
            throw new IllegalArgumentException("length must be positive");
        }
        this.minAngle = minAngle;
        this.maxAngle = maxAngle;
        this.length = length;
    }
    private void calculateHeight(){
        double angleRadians = Math.toRadians(relativeAngle);
        this.height = Math.sin(angleRadians) * this.length;
    }

    private void calculaWidth(){
        double angleRadians = Math.toRadians(relativeAngle);
        this.width = Math.cos(angleRadians) * this.length;
    }
    public double[] getPosition(){
        return new double[]{this.width, this.height};
    }
    public double getRelativeAngle() {
        return this.relativeAngle;
    }

    public void setRelativeAngle(double relativeAngle) {
        if (relativeAngle < minAngle || relativeAngle > maxAngle) {
            throw new IllegalArgumentException("Angle out of bounds");
        }
        this.relativeAngle = relativeAngle;
    }

    public double getLength() {
        return this.length;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getMinAngle() {
        return minAngle;
    }

    public void setMinAngle(double minAngle) {
        this.minAngle = minAngle;
    }

    public double getMaxAngle() {
        return maxAngle;
    }

    public void setMaxAngle(double maxAngle) {
        this.maxAngle = maxAngle;
    }

    public void setLength(double length) {
        this.length = length;
    }
    public void setLimits(double minAngle, double maxAngle) {
        if (minAngle > maxAngle) {
            throw new IllegalArgumentException("minAngle must be less than or equal to maxAngle");
        }
        this.minAngle = minAngle;
        this.maxAngle = maxAngle;
    }

}


