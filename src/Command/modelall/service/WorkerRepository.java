package Command.modelall.service;

import Command.modelall.model.Worker;
import java.util.HashMap;
import java.util.Map;

 // Отвечает за хранение работников в памяти.
public class WorkerRepository {
    private final Map<Long, Worker> workers = new HashMap<>();

    public Map<Long, Worker> getWorkers() {
        return workers;
    }

    public boolean containsWorker(long id) {
        return workers.containsKey(id);
    }

    public boolean isEmpty() {
        return workers.isEmpty();
    }

    public void saveWorker(Long key, Worker worker) {
        workers.put(key, worker);
    }

    public void removeWorker(Long key) {
        workers.remove(key);
    }
}
