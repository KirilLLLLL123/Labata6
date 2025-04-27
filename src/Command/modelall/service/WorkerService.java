package Command.modelall.service;

import Command.modelall.model.Position;
import Command.modelall.model.Worker;
import Command.modelall.storage.WorkerStorage;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * Класс WorkerService для управления коллекцией работников.
 */
public class WorkerService {
    private final WorkerStorage storage;
    private final Map<Long, Worker> workers = new HashMap<>();

    public WorkerService(WorkerStorage storage) {
        this.storage = storage;
    }

    public void loadWorkers() {
        try {
            List<Worker> list = storage.loadWorkers();
            for (Worker w : list) {
                workers.put(w.getId(), w);
            }
        } catch (Exception e) {
            System.out.println("Ошибка загрузки коллекции: " + e.getMessage());
        }
    }

    public void saveWorkers() {
        try {
            storage.saveWorkers(workers.values());
        } catch (Exception e) {
            System.out.println("Ошибка сохранения коллекции: " + e.getMessage());
        }
    }

    public void showWorkers() {
        workers.values().forEach(System.out::println);
    }

    public void insert(long key, Worker worker) {
        workers.put(key, worker);
    }

    public void update(long id, Worker worker) {
        if (workers.containsKey(id)) {
            workers.put(id, worker);
        } else {
            System.out.println("Работник с таким id не найден.");
        }
    }

    public void removeKey(long key) {
        workers.remove(key);
    }

    public boolean containsWorker(long id) {
        return workers.containsKey(id);
    }

    public void removeLowerKey(Long key) {
        if (workers.isEmpty()) {
            System.out.println("Коллекция пуста, нечего удалять.");
            return;
        }

        int initialSize = workers.size();
        workers.entrySet().removeIf(entry -> entry.getKey() < key);
        int removedCount = initialSize - workers.size();

        System.out.println("Удалено элементов: " + removedCount);
    }


    public void clear() {
        workers.clear();
    }

    public void replaceIfGreater(long key, Worker newWorker) {
        Worker existing = workers.get(key);
        if (existing != null && newWorker.getSalary() > existing.getSalary()) {
            workers.put(key, newWorker);
        }
    }

    public void sumOfSalary() {
        double sum = workers.values().stream().mapToDouble(Worker::getSalary).sum();
        System.out.println("Сумма всех зарплат: " + sum);
    }

    public void countGreaterThanPosition(Position position) {
        long count = workers.values().stream()
                .filter(worker -> worker.getPosition() != null && worker.getPosition().ordinal() > position.ordinal())
                .count();
        System.out.println("Количество работников с должностью выше " + position + ": " + count);
    }

    public void printFieldAscendingSalary() {
        workers.values().stream()
                .sorted((w1, w2) -> Double.compare(w1.getSalary(), w2.getSalary()))
                .forEach(worker -> System.out.println(worker.getSalary()));
    }

    public void printInfo() {
        System.out.println("Количество работников: " + workers.size());
        System.out.println("Тип коллекции: " + workers.getClass().getName());
    }
    public boolean isCollectionEmpty() {
        return workers.isEmpty();
    }
}
