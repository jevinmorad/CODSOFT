/**
 * A grade calculator where students enters their marks and get result according entered marks.
 * 
 * Author: Jevin Morad
 * Created Date: 3/7/2024
*/

package Grade_Calculator;

import java.util.Scanner;

public class GradeCalculator {
    /**
     * Main method to execute the grade calculation program.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Prompt the user for the number of subjects
        System.out.print("Enter the number of subjects: ");
        int numberOfSubjects = sc.nextInt();

        // Declare an array to store the marks for each subject
        int[] marks = new int[numberOfSubjects];
        int totalMarks = 0;

        // Input marks for each subject
        for (int i = 0; i < numberOfSubjects; i++) {
            System.out.print("Enter marks for subject " + (i + 1) + " (out of 100): ");
            marks[i] = sc.nextInt();
            
            // loop until marks is not between 0 to 100
            while (marks[i] > 100 || marks[i] < 0) {
                System.out.print("Enter valid marks for subject " + (i + 1) + " (out of 100): ");
                marks[i] = sc.nextInt();
            }
            totalMarks += marks[i];
        }

        // Calculate average percentage
        double averagePercentage = (double) totalMarks / numberOfSubjects;

        // Determine the grade based on average percentage
        char grade = calculateGrade(averagePercentage);

        // Display the results
        System.out.println("\n------------------Results------------------");
        System.out.println("Total Marks: " + totalMarks);
        System.out.printf("Average Percentage: %.2f%%\n", averagePercentage);
        System.out.println("Grade: " + grade);

        sc.close();
    }

    /**
     * Calculates the grade based on the average percentage.
     *
     * @param averagePercentage The average percentage obtained.
     * @return The grade as a character (A, B, C, D, or F).
     */
    public static char calculateGrade(double averagePercentage) {
        if (averagePercentage >= 90) {
            return 'A';
        } else if (averagePercentage >= 80) {
            return 'B';
        } else if (averagePercentage >= 70) {
            return 'C';
        } else if (averagePercentage >= 60) {
            return 'D';
        } else {
            return 'F';
        }
    }
}
