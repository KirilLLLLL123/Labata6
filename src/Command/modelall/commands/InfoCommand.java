package Command.modelall.commands;

import Command.modelall.service.WorkerService;
import Command.modelall.exceptions.InvalidCommandArgumentsException;
import Command.modelall.exceptions.OperationCancelledException;

public class InfoCommand implements Command {
    private WorkerService service;

    public InfoCommand() {
        // Пустой конструктор
    }

    public void setWorkerService(WorkerService service) {
        this.service = service;
    }

    @Override
    public void execute(String args) throws OperationCancelledException, InvalidCommandArgumentsException {
        if (service != null) {
            service.printInfo();
        } else {
            System.out.println("WorkerService не инициализирован.");
        }
    }

    @Override
    public String toString() {
        return "info";
    }
}
