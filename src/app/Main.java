package app;

import model.Account;
import repository.Repository;
import service.BankService;
import service.TransactionProcessor;
import util.Persistence;
import util.ReportGenerator;

import java.util.List;
import java.util.Scanner;

public class Main {

    private static final String DATA_FILE = "data/accounts.dat";
    private static final String REPORT_FILE = "data/bank-report.txt";

    public static void main(String[] args) {

        Repository<Account> repository = new Repository<>(Account.class);
        Persistence.load(repository, DATA_FILE);

        BankService bank = new BankService(repository);

        try (Scanner scanner = new Scanner(System.in);
             TransactionProcessor processor = new TransactionProcessor()) {

            boolean running = true;

            while (running) {
                printMenu();

                System.out.print("Enter choice: ");

                if (!scanner.hasNextLine()) {
                    System.out.println("\nNo input available. Saving and exiting.");
                    break;
                }

                String choice = scanner.nextLine().trim();

                try {
                    switch (choice) {

                        case "1": {
                            System.out.print("Enter account ID: ");
                            String id = read(scanner);

                            System.out.print("Enter account holder name: ");
                            String name = read(scanner);

                            double opening = readAmount(scanner, "Opening balance: ");

                            Account account = bank.openAccount(id, name, opening);
                            System.out.println("Account opened successfully!");
                            System.out.println(account);
                            break;
                        }

                        case "2": {
                            String id = read(scanner, "Account ID: ");
                            double amount = readAmount(scanner, "Deposit amount: ");

                            processor.process(() -> {
                                bank.deposit(id, amount);
                                return null;
                            });

                            System.out.println("Deposit successful.");
                            break;
                        }

                        case "3": {
                            String id = read(scanner, "Account ID: ");
                            double amount = readAmount(scanner, "Withdrawal amount: ");

                            processor.process(() -> {
                                bank.withdraw(id, amount);
                                return null;
                            });

                            System.out.println("Withdrawal successful.");
                            break;
                        }

                        case "4": {
                            String from = read(scanner, "From account ID: ");
                            String to = read(scanner, "To account ID: ");
                            double amount = readAmount(scanner, "Transfer amount: ");

                            processor.process(() -> {
                                bank.transfer(from, to, amount);
                                return null;
                            });

                            System.out.println("Transfer successful.");
                            break;
                        }

                        case "5": {
                            String id = read(scanner, "Account ID: ");
                            List<String> statement = bank.statement(id);

                            System.out.println("\n===== ACCOUNT STATEMENT =====");

                            for (String entry : statement) {
                                System.out.println(entry);
                            }
                            break;
                        }

                        case "6": {
                            List<Account> accounts = bank.listAccounts();

                            if (accounts.isEmpty()) {
                                System.out.println("No accounts available.");
                            } else {
                                System.out.println("\n===== ALL ACCOUNTS =====");

                                for (Account account : accounts) {
                                    System.out.println(account);
                                }
                            }
                            break;
                        }

                        case "7": {
                            System.out.println(
                                    ReportGenerator.generate(repository));

                            ReportGenerator.saveReport(repository, REPORT_FILE);
                            break;
                        }

                        case "8": {
                            Persistence.save(repository, DATA_FILE);
                            break;
                        }

                        case "0": {
                            Persistence.save(repository, DATA_FILE);
                            running = false;
                            System.out.println("Thank you for using MiniBank!");
                            break;
                        }

                        default:
                            System.out.println("Invalid menu option.");
                    }

                } catch (RuntimeException e) {
                    System.out.println("Error: " + e.getMessage());
                }

                System.out.println();
            }

        } finally {
            Persistence.save(repository, DATA_FILE);
        }
    }

    private static void printMenu() {
        System.out.println("========== MINIBANK ==========");
        System.out.println("1. Open Account");
        System.out.println("2. Deposit");
        System.out.println("3. Withdraw");
        System.out.println("4. Transfer");
        System.out.println("5. Account Statement");
        System.out.println("6. List Accounts");
        System.out.println("7. Generate Report");
        System.out.println("8. Save Data");
        System.out.println("0. Save and Exit");
        System.out.println("==============================");
    }

    private static String read(Scanner scanner) {
        if (!scanner.hasNextLine()) {
            throw new IllegalStateException("No input available.");
        }

        return scanner.nextLine().trim();
    }

    private static String read(Scanner scanner, String prompt) {
        System.out.print(prompt);
        return read(scanner);
    }

    private static double readAmount(Scanner scanner, String prompt) {
        System.out.print(prompt);
        String input = read(scanner);

        try {
            return Double.parseDouble(input);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Enter a valid number.");
        }
    }
}
