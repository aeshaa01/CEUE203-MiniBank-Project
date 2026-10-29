package service;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class TransactionProcessor implements AutoCloseable {

    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public <T> T process(Callable<T> transaction) {
        Future<T> future = executor.submit(transaction);

        try {
            return future.get();

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Transaction was interrupted.", e);

        } catch (ExecutionException e) {
            Throwable cause = e.getCause();

            if (cause instanceof RuntimeException) {
                throw (RuntimeException) cause;
            }

            throw new RuntimeException("Transaction failed.", cause);
        }
    }

    @Override
    public void close() {
        executor.shutdown();
    }
}
