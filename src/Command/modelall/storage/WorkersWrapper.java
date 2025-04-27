package Command.modelall.storage;

import Command.modelall.model.Worker;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "workers")
public class WorkersWrapper {
    private List<Worker> workers;

    @XmlElement(name = "worker")
    public List<Worker> getWorkers() {
        return workers;
    }

    public void setWorkers(List<Worker> workers) {
        this.workers = workers;
    }
}
