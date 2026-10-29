package service;

import exception.AccountNotFoundException;
import exception.BankException;
import exception.InsufficientFundsException;
import model.Account;
import repository.Repository;

import java.util.List;

public class BankService {

    private final Repository<Account> accounts;

    public BankService(Repository<Account> accounts) {
        this.accounts = accounts;
    }

    public synchronized Account openAccount(
            String id, String name, double openingBalance) {

        id = Validator.validateAccountId(id);
        name = Validator.validateName(name);
        openingBalance = Validator.validateOpeningBalance(openingBalance);

        if (accounts.findById(id).isPresent()) {
            throw new BankException("Account ID already exists.");
        }

        Account account = new Account(id, name, openingBalance);
        accounts.save(account);

        return account;
    }

    private Account getAccount(String id) {
        final String accountId = Validator.validateAccountId(id);

        return accounts.findById(accountId)
                .orElseThrow(() ->
                        new AccountNotFoundException(
                                "Account not found: " + accountId));
    }

    public synchronized void deposit(String id, double amount) {
        Account account = getAccount(id);
        amount = Validator.validateAmount(amount);
        account.deposit(amount);
    }

    public synchronized void withdraw(String id, double amount) {
        Account account = getAccount(id);
        amount = Validator.validateAmount(amount);

        if (account.getBalance() < amount) {
            throw new InsufficientFundsException("Insufficient balance.");
        }

        account.withdraw(amount);
    }

    public synchronized void transfer(
            String fromId, String toId, double amount) {

        fromId = Validator.validateAccountId(fromId);
        toId = Validator.validateAccountId(toId);
        amount = Validator.validateAmount(amount);

        if (fromId.equals(toId)) {
            throw new BankException(
                    "Source and destination must be different.");
        }

        Account from = getAccount(fromId);
        Account to = getAccount(toId);

        if (from.getBalance() < amount) {
            throw new InsufficientFundsException(
                    "Insufficient balance for transfer.");
        }

        from.withdraw(amount);
        from.addTransaction("Transfer to account " + toId);

        to.deposit(amount);
        to.addTransaction("Transfer received from account " + fromId);
    }

    public synchronized List<String> statement(String id) {
        return getAccount(id).getTransactions();
    }

    public synchronized List<Account> listAccounts() {
        return accounts.findAll();
    }

    public synchronized double getTotalBalance() {
        return accounts.findAll().stream()
                .mapToDouble(Account::getBalance)
                .sum();
    }

    public synchronized int getAccountCount() {
        return accounts.count();
    }
}