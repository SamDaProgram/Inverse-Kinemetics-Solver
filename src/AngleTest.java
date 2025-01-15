import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

public class AngleTest {
    public static void main(String[] args) {
        Random rand = new Random();
        double[] used_num = new double[2000];
        double randomNum;
        for (int i = 0; i < 2000; i++) {
            boolean used = true;
            do {
                 randomNum =1+ rand.nextDouble() * 359.0;
                used = false; // Assume it's not used
                for (int j = 0; j < i; j++) { // Compare only with previously used numbers
                    if (used_num[j] == randomNum) {
                        used = true; // Found a duplicate, flag it as used
                        break;
                    }
                }
            } while (used); // Keep trying until a unique number is found

            used_num[i] = randomNum; // Store the unique number

            // Print the used numbers for debugging
            for (double num : used_num) {
                System.out.println(num);
            }

            // Assuming CircleOffset is a class that exists
            CircleOffset circleOffset = new CircleOffset(randomNum);
            try (PrintWriter writer = new PrintWriter(new FileWriter("csv/angle_test_data_case_#" + i + ".csv"))) {
                // Write CSV header
                writer.println("Angle,x,y,z");

                // Loop through each angle (0 to 359)
                for (int angle = 0; angle < 360; angle++) {
                    // Assuming position[] holds x and y values
                    double[] position = calculatePosition(angle,randomNum); // Replace with actual calculation method

                    // Round the x, y values to 3 decimal places
                    double x = circleOffset.round(position[0], 3);
                    double y = circleOffset.round(position[1], 3);
                    double z = circleOffset.getZ(); // Assuming z is retrieved via getZ()

                    // Write the data to the CSV file in the format: Angle, x, y, z
                    writer.printf("%d,%.3f,%.3f,%.3f%n", angle, x, y, z);
                }

                System.out.println("Test data saved to angle_test_data_case_" + i + ".csv");
                Thread.sleep(30);

            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    // Placeholder for position calculation method
    private static double[] calculatePosition(int angle, double num) {
        // Replace with your actual calculation logic
        double[] position = new double[2];
        // Example calculation (replace with your own logic)
        position[0] = num * Math.cos(Math.toRadians(angle)); // x
        position[1] = num * Math.sin(Math.toRadians(angle)); // y
        return position;
    }
}
