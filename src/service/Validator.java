package service;

import exception.BankException;

public class Validator {

    public static String validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new BankException("Account holder name cannot be empty.");
        }
        return name.trim();
    }

    public static String validateAccountId(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new BankException("Account ID cannot be empty.");
        }
        return id.trim();
    }

    public static double validateAmount(double amount) {
        if (!Double.isFinite(amount) || amount <= 0) {
            throw new BankException("Amount must be a positive number.");
        }
        return Math.round(amount * 100.0) / 100.0;
    }

    public static double validateOpeningBalance(double amount) {
        if (!Double.isFinite(amount) || amount < 0) {
            throw new BankException("Opening balance cannot be negative.");
        }
        return Math.round(amount * 100.0) / 100.0;
    }

    private Validator() {
    }
}
