package repository;

import annotation.Id;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Repository<T> {

    private final Map<String, T> records = new HashMap<>();
    private final Class<T> type;

    public Repository(Class<T> type) {
        this.type = type;
    }

    private String extractId(T item) {
        try {
            for (Field field : type.getDeclaredFields()) {
                if (field.isAnnotationPresent(Id.class)) {
                    field.setAccessible(true);
                    Object value = field.get(item);

                    if (value == null) {
                        throw new IllegalArgumentException("ID cannot be null.");
                    }

                    return value.toString();
                }
            }

            throw new IllegalStateException(
                    "No field annotated with @Id in " + type.getSimpleName());

        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Could not read ID.", e);
        }
    }

    public synchronized void save(T item) {
        if (item == null) {
            throw new IllegalArgumentException("Cannot save a null record.");
        }

        records.put(extractId(item), item);
    }

    @SafeVarargs
    public final synchronized void saveAll(T... items) {
        for (T item : items) {
            save(item);
        }
    }

    public synchronized Optional<T> findById(String id) {
        return Optional.ofNullable(records.get(id));
    }

    public synchronized List<T> findAll() {
        return new ArrayList<>(records.values());
    }

    public synchronized boolean delete(String id) {
        return records.remove(id) != null;
    }

    public synchronized int count() {
        return records.size();
    }

    public synchronized void clear() {
        records.clear();
    }
}
