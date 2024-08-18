import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

/**
 * QuizApplication is a quiz game where users answer multiple-choice questions.
 * Each question has a time limit for answering, and the user's score is calculated based on correct answers.
 *
 * Features:
 * - Multiple-choice questions: Each question has four options.
 * - Timed questions: Users have a limited time to answer each question.
 * - Score calculation: The user's score is calculated and displayed at the end.
 * - Summary of results: The application shows which questions were answered correctly or incorrectly.
 *
 * Author: Jevin Morad
 * Date: 6/8/2024
 */
public class QuizApplication {

    /**
     * Represents a quiz question with text, multiple-choice options, and the index of the correct answer.
     */
    static class Question {
        String question; // The text of the question
        String[] options; // Array of answer options
        int correctAnswer; // The index of the correct answer (1-based)

        /**
         * Constructs a Question with the specified text, options, and correct answer index.
         *
         * @param question      The text of the question
         * @param options       The array of answer options
         * @param correctAnswer The index of the correct answer (1-based)
         */
        public Question(String question, String[] options, int correctAnswer) {
            this.question = question;
            this.options = options;
            this.correctAnswer = correctAnswer;
        }
    }

    private static final int TIME_LIMIT = 10; // Time limit in seconds for each question
    private static int currentAnswer = -1; // Stores the user's current answer
    private static boolean isTimeUp = false; // Flag to track if time is up for the question

    /**
     * The main method that executes the quiz application.
     *
     * @param args Command-line arguments (not used)
     */
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("\nWelcome to quiz.\n");
        // Define quiz questions
        Question[] questions = {
                new Question(
                        "What is the largest planet in our solar system?",
                        new String[] { "Earth", "Mars", "Jupiter", "Saturn" },
                        3),
                new Question(
                        "Who wrote the play \"Romeo and Juliet\"?",
                        new String[] { "William Shakespeare", "Charles Dickens", "Jane Austen", "Mark Twain" },
                        1),
                new Question(
                        "What is the chemical symbol for water?",
                        new String[] { "O2", "H2O", "CO2", "NaCl" },
                        2),
                new Question(
                        "Which element is known as the \"King of Metals\"?",
                        new String[] { "Gold", "Silver", "Iron", "Platinum" },
                        1),
                new Question(
                        "What is the capital of India?",
                        new String[] { "Mumbai", "Delhi", "Bangalore", "Kolkata" },
                        2),
                new Question(
                        "Who painted the \"Mona Lisa\"?",
                        new String[] { "Vincent van Gogh", "Pablo Picasso", "Leonardo da Vinci", "Claude Monet" },
                        3),
                new Question(
                        "Which planet is known as the \"Blue Planet\"?",
                        new String[] { "Mars", "Venus", "Neptune", "Earth" },
                        4),
                new Question(
                        "In what year did the Titanic sink?",
                        new String[] { "1905", "1912", "1923", "1930" },
                        2),
                new Question(
                        "What is the hardest natural substance on Earth?",
                        new String[] { "Gold", "Iron", "Diamond", "Quartz" },
                        3),
                new Question(
                        "Who is known as the \"Father of Computers\"?",
                        new String[] { "Albert Einstein", "Charles Babbage", "Isaac Newton", "Thomas Edison" },
                        2),
        };

        startQuiz(questions, sc);

        sc.close(); // Close the sc
    }

    public static void startQuiz(Question[] questions, Scanner sc) {
        int score = 0; // Initialize the user's score
        boolean[] correctAnswers = new boolean[questions.length]; // Array to track correct answers

        // Iterate over each question
        for (int i = 0; i < questions.length; i++) {
            isTimeUp = false; // Reset the timer flag for each question
            currentAnswer = -1; // Reset the current answer

            // Display the question and options
            System.out.println("\nQuestion " + (i + 1) + ":");
            System.out.println(questions[i].question);
            for (int j = 0; j < questions[i].options.length; j++) {
                System.out.println((j + 1) + ") " + questions[i].options[j]);
            }

            // Set up the timer for the question
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    isTimeUp = true;
                    System.out.println("\nTime is up!");
                    System.out.println("Press ENTER for next question.");

                }
            }, TIME_LIMIT * 1000); // Schedule the timer task for TIME_LIMIT seconds

            // Prompt the user for an answer within the time limit
            while (!isTimeUp) {
                System.out.print("Enter the number of your answer: ");
                try {
                    currentAnswer = Integer.parseInt(sc.nextLine());
                    if (currentAnswer >= 1 && currentAnswer <= questions[i].options.length) {
                        break; // Answer received in range
                    } else {
                        System.out.println(
                                "Invalid choice. Please enter a number between 1 and " + questions[i].options.length);
                    }
                } catch (NumberFormatException e) {
                }
            }
            timer.cancel(); // Cancel the timer after the answer is received

            // Check if the user's answer is correct
            if (currentAnswer == questions[i].correctAnswer) {
                score++;
                correctAnswers[i] = true;
            }
        }

        // Display the final results
        displayResults(score, questions.length, correctAnswers, questions);
    }

    /**
     * Displays the results of the quiz, including the user's score and a summary of correct and incorrect answers.
     *
     * @param score          The user's score
     * @param totalQuestions The total number of questions
     * @param correctAnswers An array indicating which questions were answered
     *                       correctly
     * @param questions      The array of quiz questions
     */
    private static void displayResults(int score, int totalQuestions, boolean[] correctAnswers, Question[] questions) {
        System.out.println("\nQuiz Complete!");
        System.out.println("Your final score: " + score + " out of " + totalQuestions);
        System.out.println("\nSummary:");

        // Display a summary of each question's result
        for (int i = 0; i < questions.length; i++) {
            System.out.println("Question " + (i + 1) + ": " + questions[i].question);
            if (correctAnswers[i]) {
                System.out.println("Correct!\n");
            } else {
                System.out.println(
                        "Incorrect. Correct answer: " + questions[i].options[questions[i].correctAnswer - 1] + "\n");
            }
        }
    }
}