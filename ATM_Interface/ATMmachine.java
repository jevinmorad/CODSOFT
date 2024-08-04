import java.util.HashMap;
import java.util.Scanner;

/**
 * ATM interface contains methods of ATM
 */
interface ATM {
    void authenticate(String pin) throws AuthenticationException;

    void withdraw(double amount) throws InsufficientBalanceException;

    void deposit(double amount);

    double getBalance();
}

class InsufficientBalanceException extends Exception {
    public InsufficientBalanceException(String message) {
        super(message);
    }
}

class AuthenticationException extends Exception {
    public AuthenticationException(String message) {
        super(message);
    }
}

class BankAccount implements ATM {
    private double balance;
    private String pin;
    private int failedLoginAttempts;

    // Constants for max attempts for authentication configurations
    private static final int MAX_ATTEMPTS = 3;

    /**
     * Initializes a new bank account with the given balance and PIN.
     *
     * @param initialBalance the initial balance of the account
     * @param pin            the account PIN for authentication
     */
    public BankAccount(double initialBalance, String pin) {
        this.balance = initialBalance;
        this.pin = pin;
        this.failedLoginAttempts = 0;
    }

    /**
     * Authenticate the user with the pin.
     * 
     * @param pin
     * @throws AuthenticationException
     */
    @Override
    public void authenticate(String pin) throws AuthenticationException {
        if (isLocked()) {
            throw new AuthenticationException("Account is locked due to too many failed login attempts.");
        }

        if (!this.pin.equals(pin)) {
            failedLoginAttempts++;
            if (isLocked()) {
                throw new AuthenticationException("Too many failed attempts. Account is locked.");
            }
            throw new AuthenticationException("Incorrect PIN.");
        }

        failedLoginAttempts = 0; // Reset failed attempts on successful authentication
    }

    public boolean isLocked() {
        return this.failedLoginAttempts >= MAX_ATTEMPTS;
    }

    /**
     * Deposit amount into the account.
     * 
     * @param amount the amount to deposit
     */
    @Override
    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            System.out.printf("Successfully deposited $%.2f\n", amount);
        } else {
            System.out.println("Deposit amount must be positive.");
        }
    }

    /**
     * Withdraw amount from the account if sufficient balance is available
     * 
     * @param amount the amount to withdraw
     * @throws InsufficientBalanceException if the withdrawal amount is greater than
     *                                      the balance
     */
    @Override
    public void withdraw(double amount) throws InsufficientBalanceException {
        if (amount > 0) {
            if (amount <= balance) {
                balance -= amount;
                System.out.printf("Successfully withdrew $%.2f\n", amount);
            } else {
                throw new InsufficientBalanceException("Insufficient balance for withdrawal.");
            }
        } else {
            System.out.println("Withdrawal amount must be positive.");
        }
    }

    /**
     * Returns the current balance of the account.
     * 
     * @return the balance of the account
     */
    @Override
    public double getBalance() {
        return balance;
    }
}

public class ATMmachine {
    private HashMap<String, BankAccount> accounts;
    private BankAccount currentAccount;
    private Scanner sc;

    // Initialize some demo accounts
    public ATMmachine() {
        accounts = new HashMap<>();
        accounts.put("abc", new BankAccount(1000, "123")); // Added initial balance for testing
        accounts.put("xyz", new BankAccount(500, "567"));
    }

    /**
     * Starts the ATM application and processes user input for various banking
     * operations.
     */
    private void start() {
        System.out.println("\nWelcome to the ATM!");
        sc = new Scanner(System.in);

        // Authenticate user before any transaction
        if (!authenticateUser()) {
            sc.close();
            return;
        }

        boolean exit = false;
        while (!exit) {
            System.out.println("\nPlease choose an option:");
            System.out.println("1. Check Balance");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");

            int choice = sc.nextInt();
            switch (choice) {
                case 1:
                    checkBalance();
                    break;

                case 2:
                    handleDeposit();
                    break;

                case 3:
                    handleWithdrawal();
                    break;

                case 4:
                    exit = true;
                    System.out.println("Thank you for using the ATM. Goodbye!");
                    break;

                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
        }

        sc.close();
    }

    /**
     * Authenticate the user by prompting for user ID and PIN.
     * 
     * @return true if authentication is successful, false otherwise
     */
    private boolean authenticateUser() {
        
        while (true) {
            System.out.println("\n----------Authentication----------");
            System.out.print("Enter user ID: ");
            String userId = sc.next();

            // Check for user's availability.
            if (!accounts.containsKey(userId)) {
                System.out.println("\nAuthentication failed: User ID not found.");
                return false;
            }

            currentAccount = accounts.get(userId);

            if (currentAccount.isLocked()) {
                System.out.println("Account is locked due to too many failed attempts.");
                return false;
            }

            System.out.print("Enter PIN: ");
            String pin = sc.next();

            try {
                currentAccount.authenticate(pin);
                System.out.println("\nAuthentication successful.");
                return true;
            } catch (AuthenticationException e) {
                System.out.println("\nAuthentication failed: " + e.getMessage());
                if (currentAccount.isLocked()) {
                    return false;
                }
                System.out.println("Please try again.");
            }
        }
    }

    /**
     * Checks and displays the current balance of the user's account.
     */
    private void checkBalance() {
        System.out.printf("Current Balance: $%.2f\n", currentAccount.getBalance());
    }

    /**
     * Handles the deposit operation by prompting for an amount and depositing it
     * into the user's account.
     */
    private void handleDeposit() {
        System.out.print("Enter amount to deposit: ");
        double depositAmount = sc.nextDouble();
        currentAccount.deposit(depositAmount);
    }

    /**
     * Handles the withdrawal operation by prompting for an amount and withdrawing
     * it from the user's account.
     */
    private void handleWithdrawal() {
        System.out.print("Enter amount to withdraw: ");
        double withdrawAmount = sc.nextDouble();

        try {
            currentAccount.withdraw(withdrawAmount);
        } catch (InsufficientBalanceException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        ATMmachine atm = new ATMmachine();
        atm.start();
    }
}