package Command.modelall.storage;

import Command.modelall.model.Worker;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.List;

public class XmlWorkerStorage implements WorkerStorage {

    private final String filename;

    public XmlWorkerStorage(String filename) {
        this.filename = filename;
    }

    @Override
    public List<Worker> loadWorkers() throws Exception {
        File file = new File(filename);
        if (!file.exists()) return List.of();
        JAXBContext context = JAXBContext.newInstance(WorkersWrapper.class, Worker.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        WorkersWrapper wrapper = (WorkersWrapper) unmarshaller.unmarshal(file);
        return wrapper.getWorkers();
    }

    @Override
    public void saveWorkers(Iterable<Worker> workers) throws Exception {
        WorkersWrapper wrapper = new WorkersWrapper();
        // Переводим Iterable в List
        List<Worker> workerList = new java.util.ArrayList<>();
        for (Worker w : workers) {
            workerList.add(w);
        }
        wrapper.setWorkers(workerList);
        JAXBContext context = JAXBContext.newInstance(WorkersWrapper.class, Worker.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(wrapper, new File(filename));
    }
}
