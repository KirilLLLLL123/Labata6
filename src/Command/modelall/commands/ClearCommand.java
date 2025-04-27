package Command.modelall.commands;

import Command.modelall.service.WorkerService;
import Command.modelall.exceptions.OperationCancelledException;
import Command.modelall.exceptions.InvalidCommandArgumentsException;

public class ClearCommand implements Command {
    private WorkerService service;

    public ClearCommand() {
        // Пустой конструктор
    }

    public void setWorkerService(WorkerService service) {
        this.service = service;
    }

    @Override
    public void execute(String args) throws OperationCancelledException, InvalidCommandArgumentsException {
        service.clear();
        System.out.println("Коллекция очищена.");
    }

    @Override
    public String toString() {
        return "clear";
    }
}
