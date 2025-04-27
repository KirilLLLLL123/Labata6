package Server;
/** загрузка/сохранение workers.csv */

import Command.modelall.model.Worker;
import Command.modelall.storage.CsvWorkerStorage;
import Command.modelall.storage.WorkerStorage;

import java.util.Map;

/** Тонкая оболочка над уже существующим CsvWorkerStorage,
 *  чтобы не трогать код из 5-й лабы. */
public final class CSVStorage {

    private final CsvWorkerStorage storage;

    public CSVStorage(String file) { storage = new CsvWorkerStorage(file); }

    public Map<Integer, Worker> load() throws Exception { return storage.load(); }

    public void save(Map<Integer, Worker> map) throws Exception { storage.save(map); }
}
