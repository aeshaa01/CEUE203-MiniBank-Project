package model;

import annotation.Id;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Account implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String accountId;

    private String holderName;
    private double balance;
    private List<String> transactions = new ArrayList<>();

    public Account(String accountId, String holderName, double openingBalance) {
        this.accountId = accountId;
        this.holderName = holderName;
        this.balance = openingBalance;
        addTransaction("Account opened with balance Rs. " + openingBalance);
    }

    public String getAccountId() {
        return accountId;
    }

    public String getHolderName() {
        return holderName;
    }

    public synchronized double getBalance() {
        return balance;
    }

    public synchronized void deposit(double amount) {
        balance += amount;
        addTransaction("Deposited Rs. " + amount);
    }

    public synchronized void withdraw(double amount) {
        balance -= amount;
        addTransaction("Withdrawn Rs. " + amount);
    }

    public synchronized void addTransaction(String description) {
        String time = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));

        transactions.add(time + " | " + description);
    }

    public synchronized List<String> getTransactions() {
        return Collections.unmodifiableList(new ArrayList<>(transactions));
    }

    @Override
    public String toString() {
        return "Account ID: " + accountId
                + " | Holder: " + holderName
                + " | Balance: Rs. " + String.format("%.2f", getBalance());
    }
}
