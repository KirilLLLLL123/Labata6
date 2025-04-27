package Command.modelall.commands;

import Command.modelall.input.InputManager;
import Command.modelall.service.WorkerService;
import Command.modelall.model.Worker;
import Command.modelall.exceptions.OperationCancelledException;
import Command.modelall.exceptions.InvalidCommandArgumentsException;


public class InsertCommand implements Command {
    private WorkerService service;
    private InputManager inputManager;

    public InsertCommand() {
        // Пустой конструктор
    }

    public void setWorkerService(WorkerService service) {
        this.service = service;
    }

    public void setInputManager(InputManager inputManager) {
        this.inputManager = inputManager;
    }

    @Override
    public void execute(String args) throws OperationCancelledException, InvalidCommandArgumentsException {
        try {
            long key = Long.parseLong(args.trim());
            Worker worker = WorkerCreator.createWorker(inputManager);
            service.insert(key, worker);
            System.out.println("Работник добавлен с ключом " + key);
        } catch (NumberFormatException e) {
            throw new InvalidCommandArgumentsException("Ключ должен быть числом.");
        }
    }

    @Override
    public String toString() {
        return "insert";
    }
}
