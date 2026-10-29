package util;

import model.Account;
import repository.Repository;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Persistence {

    public static void save(Repository<Account> repository, String filename) {
        try {
            Path path = Path.of(filename);
            if (path.getParent() != null) {
                Files.createDirectories(path.getParent());
            }

            try (ObjectOutputStream out = new ObjectOutputStream(
                    new FileOutputStream(filename))) {
                out.writeObject(repository.findAll());
            }

            System.out.println("Account data saved successfully.");

        } catch (Exception e) {
            System.out.println("Could not save account data: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public static void load(Repository<Account> repository, String filename) {
        if (!Files.exists(Path.of(filename))) {
            System.out.println("No previous data found. Starting a new bank.");
            return;
        }

        try (ObjectInputStream in = new ObjectInputStream(
                new FileInputStream(filename))) {

            List<Account> accounts = (List<Account>) in.readObject();
            repository.clear();

            for (Account account : accounts) {
                repository.save(account);
            }

            System.out.println("Loaded " + accounts.size() + " account(s).");

        } catch (Exception e) {
            System.out.println("Could not load previous data: " + e.getMessage());
        }
    }

    private Persistence() {
    }
}
