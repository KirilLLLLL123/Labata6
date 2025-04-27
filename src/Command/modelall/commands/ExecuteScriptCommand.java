package Command.modelall.commands;

import Command.modelall.input.InputManager;
import Command.modelall.exceptions.CommandNotFoundException;
import Command.modelall.exceptions.InvalidCommandArgumentsException;
import Command.modelall.exceptions.OperationCancelledException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Set;
import java.util.HashSet;

// Команда execute_script выполняет команды из указанного файла.
public class ExecuteScriptCommand implements Command {
    private CommandHandler commandHandler;
    private InputManager inputManager;
    // Инициализируем executingScripts, чтобы оно никогда не было null
    private Set<String> executingScripts = new HashSet<>();

    public ExecuteScriptCommand() {
        // Пустой конструктор
    }

    public void setCommandHandler(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }

    public void setInputManager(InputManager inputManager) {
        this.inputManager = inputManager;
    }

    public void setExecutingScripts(Set<String> executingScripts) {
        this.executingScripts = executingScripts;
    }

    @Override
    public void execute(String filename) throws OperationCancelledException, InvalidCommandArgumentsException {
        if (filename.isEmpty()) {
            throw new InvalidCommandArgumentsException("Ошибка: необходимо указать имя файла скрипта.");
        }

        if (executingScripts.contains(filename)) {
            throw new InvalidCommandArgumentsException("Ошибка: обнаружен цикл вызова скриптов.");
        }

        executingScripts.add(filename);

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String commandLine;
            while ((commandLine = reader.readLine()) != null) {
                System.out.println("> " + commandLine);
                try {
                    commandHandler.runLine(commandLine);
                } catch (CommandNotFoundException e) {
                    System.out.println(e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("Ошибка при выполнении скрипта: " + e.getMessage());
        } finally {
            executingScripts.remove(filename);
        }
    }

    @Override
    public String toString() {
        return "execute_script";
    }
}
