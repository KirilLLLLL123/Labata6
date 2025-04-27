package Command.modelall.commands;

import Command.modelall.exceptions.InvalidCommandArgumentsException;
import Command.modelall.exceptions.OperationCancelledException;
import java.util.Map;

/**
 * Команда help выводит список доступных команд.
 * Список команд задается через сеттер.
 */
public class HelpCommand implements Command {
    private Map<String, Command> commands;

    public HelpCommand() {
        // Пустой конструктор
    }

    public void setCommands(Map<String, Command> commands) {
        this.commands = commands;
    }

    @Override
    public void execute(String args) throws OperationCancelledException, InvalidCommandArgumentsException {
        if (commands == null || commands.isEmpty()) {
            System.out.println("Список команд пуст.");
            return;
        }
        System.out.println("Доступные команды:");
        for (String commandName : commands.keySet()) {
            System.out.println("- " + commandName);
        }
    }

    @Override
    public String toString() {
        return "help";
    }
}
