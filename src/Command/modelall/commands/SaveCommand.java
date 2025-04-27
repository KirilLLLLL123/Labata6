package Command.modelall.commands;

import Command.modelall.service.WorkerService;
import Command.modelall.exceptions.OperationCancelledException;
import Command.modelall.exceptions.InvalidCommandArgumentsException;

public class SaveCommand implements Command {
    private WorkerService service;

    public SaveCommand() {
        // Пустой конструктор
    }

    public void setWorkerService(WorkerService service) {
        this.service = service;
    }

    @Override
    public void execute(String args) throws OperationCancelledException, InvalidCommandArgumentsException {
        service.saveWorkers();
        System.out.println("Коллекция сохранена.");
    }

    @Override
    public String toString() {
        return "save";
    }
}
