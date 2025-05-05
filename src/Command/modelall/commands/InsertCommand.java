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
    public void execute(String args)
            throws OperationCancelledException, InvalidCommandArgumentsException {

        // 1️⃣ если ключ не передан после "insert", просим ввести
        if (args == null || args.isBlank()) {
            System.out.print("Введите ключ (целое число): ");
            args = inputManager.getScanner().nextLine();
        }

        // 2️⃣ пытаемся распарсить
        long key;
        try {
            key = Long.parseLong(args.trim());
        } catch (NumberFormatException e) {
            throw new InvalidCommandArgumentsException("Ключ должен быть числом.");
        }

        // 3️⃣ создаём Worker обычным способом
        Worker worker = WorkerCreator.createWorker(inputManager);

        // 4️⃣ кладём в коллекцию/базу
        service.insert(key, worker);
        System.out.println("Работник добавлен с ключом " + key);
    }


    @Override
    public String toString() {
        return "insert";
    }
}
