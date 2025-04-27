package Command.modelall.commands;

import Command.modelall.service.WorkerService;
import Command.modelall.input.InputManager;
import Command.modelall.exceptions.OperationCancelledException;
import Command.modelall.exceptions.InvalidCommandArgumentsException;

public class RemoveKeyCommand implements Command {
    private WorkerService service;
    private InputManager inputManager;

    public RemoveKeyCommand() {
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
            if (!service.containsWorker(key)) {
                System.out.println("Работник с ключом " + key + " не найден.");
                return;
            }
            service.removeKey(key);
            System.out.println("Работник с ключом " + key + " удалён.");
        } catch (NumberFormatException e) {
            throw new InvalidCommandArgumentsException("Ключ должен быть числом.");
        }
    }

    @Override
    public String toString() {
        return "remove_key";
    }
}
