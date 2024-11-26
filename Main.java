import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

class User {
    private String name;
    private String address;
    private String contact;
    private String password;
    private int accountNumber;
    private double balance;
    private ArrayList<String> transactionHistory;

    public User(String name, String address, String contact, String password, int accountNumber, double initialDeposit) {
        this.name = name;
        this.address = address;
        this.contact = contact;
        this.password = password;
        this.accountNumber = accountNumber;
        this.balance = initialDeposit;
        this.transactionHistory = new ArrayList<>();
        addTransaction("Initial Deposit: " + initialDeposit);
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public double getBalance() {
        return balance;
    }

    public boolean validatePassword(String inputPassword) {
        return password.equals(inputPassword);
    }

    public void deposit(double amount) {
        balance += amount;
        addTransaction("Deposited: " + amount);
    }

    public boolean withdraw(double amount) {
        if (amount > balance) {
            return false;
        }
        balance -= amount;
        addTransaction("Withdraw: " + amount);
        return true;
    }

    public void addTransaction(String transaction) {
        transactionHistory.add(transaction);
    }

    public ArrayList<String> getTransactionHistory() {
        return transactionHistory;
    }

    public void updateDetails(String name, String address, String contact) {
        this.name = name;
        this.address = address;
        this.contact = contact;
    }
}

class BankingSystem {
    private HashMap<Integer, User> users;
    private int accountNumberCounter;

    public BankingSystem() {
        users = new HashMap<>();
        accountNumberCounter = 1001;
    }

    public User registerUser(String name, String address, String contact, String password, double initialDeposit) {
        int accountNumber = accountNumberCounter++;
        User user = new User(name, address, contact, password, accountNumber, initialDeposit);
        users.put(accountNumber, user);
        System.out.println("Registration Successful! Your account number is: " + accountNumber);
        return user;
    }

    public User login(int accountNumber, String password) {
        User user = users.get(accountNumber);
        if (user != null && user.validatePassword(password)) {
            return user;
        }
        return null;
    }

    public boolean transferFunds(User sender, int recipientAccountNumber, double amount) {
        User recipient = users.get(recipientAccountNumber);
        if (recipient != null && sender.withdraw(amount)) {
            recipient.deposit(amount);
            sender.addTransaction("Transferred: " + amount + " to Account: " + recipientAccountNumber);
            recipient.addTransaction("Received: " + amount + " from Account: " + sender.getAccountNumber());
            return true;
        }
        return false;
    }
}

public class Main {
    public static void main(String[] args) {
        BankingSystem bankingSystem = new BankingSystem();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n--- Banking System ---");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    System.out.print("Enter Name: ");
                    String name = scanner.next();
                    System.out.print("Enter Address: ");
                    String address = scanner.next();
                    System.out.print("Enter Contact: ");
                    String contact = scanner.next();
                    System.out.print("Set Password: ");
                    String password = scanner.next();
                    System.out.print("Initial Deposit: ");
                    double initialDeposit = scanner.nextDouble();
                    bankingSystem.registerUser(name, address, contact, password, initialDeposit);
                    break;

                case 2:
                    System.out.print("Enter Account Number: ");
                    int accountNumber = scanner.nextInt();
                    System.out.print("Enter Password: ");
                    String loginPassword = scanner.next();
                    User user = bankingSystem.login(accountNumber, loginPassword);
                    if (user != null) {
                        System.out.println("Login Successful!");
                        userMenu(user, bankingSystem, scanner);
                    } else {
                        System.out.println("Invalid Account Number or Password.");
                    }
                    break;

                case 3:
                    System.out.println("Exiting System. Goodbye!");
                    scanner.close();
                    return;

                default:
                    System.out.println("Invalid Option. Try again.");
            }
        }
    }

    private static void userMenu(User user, BankingSystem bankingSystem, Scanner scanner) {
        while (true) {
            System.out.println("\n--- User Menu ---");
            System.out.println("1. Deposit");
            System.out.println("2. Withdraw");
            System.out.println("3. Fund Transfer");
            System.out.println("4. View Account Statement");
            System.out.println("5. Logout");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    System.out.print("Enter Deposit Amount: ");
                    double depositAmount = scanner.nextDouble();
                    user.deposit(depositAmount);
                    System.out.println("Deposit Successful. New Balance: " + user.getBalance());
                    break;

                case 2:
                    System.out.print("Enter Withdrawal Amount: ");
                    double withdrawAmount = scanner.nextDouble();
                    if (user.withdraw(withdrawAmount)) {
                        System.out.println("Withdrawal Successful. New Balance: " + user.getBalance());
                    } else {
                        System.out.println("Insufficient Funds.");
                    }
                    break;

                case 3:
                    System.out.print("Enter Recipient Account Number: ");
                    int recipientAccount = scanner.nextInt();
                    System.out.print("Enter Transfer Amount: ");
                    double transferAmount = scanner.nextDouble();
                    if (bankingSystem.transferFunds(user, recipientAccount, transferAmount)) {
                        System.out.println("Transfer Successful.");
                    } else {
                        System.out.println("Transfer Failed. Check Funds or Recipient Account.");
                    }
                    break;

                case 4:
                    System.out.println("Account Statement:");
                    for (String transaction : user.getTransactionHistory()) {
                        System.out.println(transaction);
                    }
                    break;

                case 5:
                    System.out.println("Logging out...");
                    return;

                default:
                    System.out.println("Invalid Option. Try again.");
            }
        }
    }
}
