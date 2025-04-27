package Server;
/** внутренняя логика Stream-API */
import Command.modelall.model.Worker;

import java.util.Map;
import java.util.TreeMap;

/** Обёртка вокруг Map + сохранение/загрузка через CSVStorage. */
public class CollectionManager {

    private final CSVStorage storage;
    private final Map<Integer, Worker> workers = new TreeMap<>();

    public CollectionManager(CSVStorage storage) { this.storage = storage; }

    /* ------------ file I/O ------------ */

    public void load() throws Exception {
        workers.clear();
        workers.putAll(storage.load());
    }

    public void save() throws Exception { storage.save(workers); }

    /* ------------ business ------------ */

    public Map<Integer, Worker> view() {       // не отдаём исходную map
        return new TreeMap<>(workers);         // автоматическая сортировка
    }

    public Map<Integer, Worker> inner() {      // нужен для команд
        return workers;
    }
}
