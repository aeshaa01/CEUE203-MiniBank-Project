package test;

import model.Account;
import repository.Repository;
import service.BankService;
import exception.InsufficientFundsException;

public class BankServiceTest {

    public static void main(String[] args) {
        Repository<Account> repository = new Repository<>(Account.class);
        BankService bank = new BankService(repository);

        bank.openAccount("T101", "Aesha", 1000);
        bank.openAccount("T102", "Riya", 500);

        bank.deposit("T101", 200);
        bank.withdraw("T102", 100);
        bank.transfer("T101", "T102", 300);

        check(repository.count() == 2, "Account count");
        check(repository.findById("T101").orElseThrow().getBalance() == 900,
                "Sender balance");
        check(repository.findById("T102").orElseThrow().getBalance() == 700,
                "Receiver balance");

        boolean rejected = false;

        try {
            bank.withdraw("T102", 5000);
        } catch (InsufficientFundsException e) {
            rejected = true;
        }

        check(rejected, "Insufficient funds validation");

        System.out.println();
        System.out.println("ALL BANK SERVICE SELF-TESTS PASSED!");
    }

    private static void check(boolean condition, String name) {
        if (!condition) {
            throw new AssertionError("FAILED: " + name);
        }

        System.out.println("PASSED: " + name);
    }
}
