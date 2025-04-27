package Command.modelall.storage;

import Command.modelall.model.Worker;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import java.io.File;
import java.util.List;

public class JsonWorkerStorage implements WorkerStorage {
    private final String filename;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public JsonWorkerStorage(String filename) {
        this.filename = filename;
    }

    @Override
    public List<Worker> loadWorkers() throws Exception {
        File file = new File(filename);
        if (!file.exists()) return List.of();
        return objectMapper.readValue(file, new TypeReference<List<Worker>>() {});
    }

    @Override
    public void saveWorkers(Iterable<Worker> workers) throws Exception {
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(filename), workers);
    }
}
