package Server;
/** загрузка/сохранение workers.csv */

import Command.modelall.model.Worker;
import Command.modelall.storage.CsvWorkerStorage;

import java.util.*;

/** Тонкая оболочка над CsvWorkerStorage из 5-й лабы. */
public final class CSVStorage {

    private final CsvWorkerStorage storage;

    public CSVStorage(String file) {
        this.storage = new CsvWorkerStorage(file);
    }

    /** Загружаем из файла и кладём в TreeMap< Integer, Worker > (ключ = id). */
    public Map<Integer, Worker> load() throws Exception {
        List<Worker> list = storage.loadWorkers();
        Map<Integer, Worker> map = new TreeMap<>();
        for (Worker w : list) {
            map.put((int) w.getId(), w);       // id → ключ (подойдёт для вашей Map)
        }
        return map;
    }

    /** Сохраняем всю коллекцию обратно в CSV. */
    public void save(Map<Integer, Worker> map) throws Exception {
        storage.saveWorkers(map.values());
    }
}

