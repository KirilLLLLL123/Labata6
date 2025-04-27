package Command.modelall.storage;

import Command.modelall.model.Worker;
import Command.modelall.exceptions.CSVFormatException;
import Command.modelall.exceptions.InvalidDataException;
import java.io.*;
import java.util.*;

public class CsvWorkerStorage implements WorkerStorage {
    private final String filename;

    public CsvWorkerStorage(String filename) {
        this.filename = filename;
    }

    @Override
    public List<Worker> loadWorkers() throws CSVFormatException, InvalidDataException {
        List<Worker> workers = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filename)))) {
            String line;
            while ((line = reader.readLine()) != null) {
                workers.add(Worker.fromCSV(line));
            }
        } catch (IOException e) {
            throw new CSVFormatException("Ошибка чтения файла: " + e.getMessage());
        }
        return workers;
    }

    @Override
    public void saveWorkers(Iterable<Worker> workers) throws CSVFormatException {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename)))) {
            for (Worker worker : workers) {
                writer.write(worker.toCSV());
                writer.newLine();
            }
        } catch (IOException e) {
            throw new CSVFormatException("Ошибка записи в файл: " + e.getMessage());
        }
    }
}
