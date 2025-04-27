package Command.modelall.commands;

import Command.modelall.service.WorkerService;
import Command.modelall.input.InputManager;
import Command.modelall.exceptions.OperationCancelledException;
import Command.modelall.exceptions.InvalidCommandArgumentsException;

public class RemoveLowerKeyCommand implements Command {
    private WorkerService service;
    private InputManager inputManager;

    public RemoveLowerKeyCommand() {
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
            if (service.isCollectionEmpty()) {
                System.out.println("Коллекция пуста, нечего удалять.");
                return;
            }
            service.removeLowerKey(key);
            System.out.println("Команда выполнена.");
        } catch (NumberFormatException e) {
            throw new InvalidCommandArgumentsException("Ключ должен быть числом.");
        }
    }

    @Override
    public String toString() {
        return "remove_lower_key";
    }
}
