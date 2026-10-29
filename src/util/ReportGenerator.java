package util;

import model.Account;
import repository.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;

public class ReportGenerator {

    public static String generate(Repository<Account> repository) {
        List<Account> accounts = repository.findAll();
        double totalBalance = accounts.stream()
                .mapToDouble(Account::getBalance)
                .sum();

        StringBuilder report = new StringBuilder();

        report.append("========== MINIBANK REPORT ==========\n");
        report.append("Total accounts: ").append(accounts.size()).append("\n");
        report.append(String.format("Total bank balance: Rs. %.2f%n", totalBalance));
        report.append("-------------------------------------\n");

        accounts.sort(Comparator.comparing(Account::getAccountId));

        for (Account account : accounts) {
            report.append(account).append("\n");
        }

        report.append("=====================================\n");
        return report.toString();
    }

    public static void saveReport(Repository<Account> repository, String filename) {
        try {
            Path path = Path.of(filename);

            if (path.getParent() != null) {
                Files.createDirectories(path.getParent());
            }

            Files.writeString(path, generate(repository));
            System.out.println("Report saved to: " + path.toAbsolutePath());

        } catch (IOException e) {
            System.out.println("Could not save report: " + e.getMessage());
        }
    }

    private ReportGenerator() {
    }
}
