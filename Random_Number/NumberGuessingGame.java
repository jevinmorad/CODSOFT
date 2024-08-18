/**
 * A simple number guessing game where the user tries to guess a randomly generated number.
 * The game generates a random number within a specified range, and the user attempts to guess it.
 * Feedback is provided for each guess to indicate whether the guess is too high, too low, or correct.
 * The user has a limited number of attempts and can play multiple rounds, with scores based on the
 * number of attempts taken.
 * 
 * Author: Jevin Morad
 * Created Date: 2/7/2024
*/

import java.util.Random;
import java.util.Scanner;

public class NumberGuessingGame {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Random random = new Random();
        boolean playAgain = true;
        int totalRounds = 0;
        int totalScore = 0;

        System.out.println("Welcome to the Number Guessing Game!");

        while (playAgain) {
            totalRounds++;
            int score = playRound(sc, random);
            totalScore += score;

            System.out.println("\nYour current score is: " + totalScore);

            // Ask user to play again
            System.out.print("\nDo you want to play again? (yes/no): ");
            String response = sc.next();
            playAgain = response.equalsIgnoreCase("yes");
        }

        System.out.println("\nYou played " + totalRounds + " rounds with a total score of "
                + totalScore + ".\nThank you for playing!\n");

        sc.close();
    }

    /**
     * Plays a single round of the number guessing game.
     * In this round, a random number is generated, and the user is prompted to
     * guess the number within a specified number of attempts.
     * The method provides feedback after each guess, indicating whether the guess
     * is too high, too low, or correct.
     * The game continues until the user guesses correctly or exhausts all attempts.
     * The score for the round is calculated based on the number of attempts taken.
     * A higher score is awarded for guessing the number in fewer attempts.
     * 
     * @param scanner the Scanner object used to read user input from the console.
     * @param random  the Random object used to generate the random number for the
     *                round.
     * @return score achieved in the current round, based on the number of attempts
     *         made
     */
    public static int playRound(Scanner sc, Random random) {
        int maxNumber = 100;
        int maxNumberAttempts = 10;
        int attempts = 0;
        int score = 0;
        int numberToGuess = random.nextInt(maxNumber);
        boolean guessedCorrect = false;

        System.out.println("\nGuess the number between 1 and 100. You have 10 attempts.");

        // Loop until the user either guesses correctly or exhausts all attempts
        while (attempts < maxNumberAttempts && !guessedCorrect) {
            System.out.print("\nAttempt " + (attempts + 1) + ": ");
            int userGuess = sc.nextInt();
            attempts++;

            if (userGuess == numberToGuess) {
                guessedCorrect = true;
                score = maxNumberAttempts - attempts + 1; // Higher score for fewer attempts
                System.out.println("\nCongratulations! You guessed the correct number in " + attempts + " attempts.");
                System.out.println("You scored: " + score + " points in this round.");
            } else if (userGuess < numberToGuess) {
                System.out.println("Your guess is too low.");
            } else {
                System.out.println("Your guess is too high.");
            }
        }

        // Inform the user if they've used all attempts without guessing correctly
        if (!guessedCorrect) {
            System.out.println("Sorry, you've used all your attempts. The correct number was " + numberToGuess + ".");
        }

        return score;
    }
}
