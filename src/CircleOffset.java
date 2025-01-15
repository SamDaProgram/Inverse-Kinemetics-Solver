public class CircleOffset {

    private double x;
    private double y;
    private double z;
    private double angle;

    public CircleOffset(double x) {
        this.x = x;
        this.z = z;
        this.y = 0; // Initialize y to 0
        this.angle = 0; // Default angle
    }

    public double getX() {
        return round(x,4);
    }

    public double getY() {
        return round(y,4);
    }

    public double getZ() {
        return round(z,4);
    }

    public double getAngle() {
        return (angle == 360) ? 0 : angle; // Ensure angle is displayed as 0 when it equals 360
    }

    public void setAngle(double angle) {
        this.angle = normalizeAngle(angle);
    }

    private double normalizeAngle(double angle) {
        angle = angle % 360;
        return (angle < 0) ? angle + 360 : angle;
    }

    private double[] calculateNewPosition(double radius, double angle) {
        double angleRad = Math.toRadians(angle);
        double xN = Math.cos(angleRad) * radius;
        double yN = Math.sin(angleRad) * radius;
        return new double[]{xN, yN};
    }

    public double[] calculateDistance(double angleChange) {
        // Calculate radius based on the current x and y
        double radius = Math.sqrt(x * x + y * y);

        // Update the angle
        this.setAngle(this.angle + angleChange);

        // Calculate new position
        double[] newPosition = calculateNewPosition(radius, this.angle);
        this.x = newPosition[0];
        this.y = newPosition[1];
        return newPosition;
    }

    public double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException("Decimal places must be non-negative.");
        double factor = Math.pow(10, places);
        return Math.round(value * factor) / factor;
    }





}
