package Command.modelall.commands;

import Command.modelall.exceptions.OperationCancelledException;
import Command.modelall.exceptions.InvalidCommandArgumentsException;

public class ExitCommand implements Command {
    public ExitCommand() {
        // Пустой конструктор
    }

    @Override
    public void execute(String args) throws OperationCancelledException, InvalidCommandArgumentsException {
        System.out.println("Завершение работы программы.");
        System.exit(0);
    }

    @Override
    public String toString() {
        return "exit";
    }
}
