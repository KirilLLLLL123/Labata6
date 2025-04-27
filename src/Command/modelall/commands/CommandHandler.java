package Command.modelall.commands;

import Command.modelall.input.InputManager;
import Command.modelall.service.WorkerService;
import Command.modelall.exceptions.CommandNotFoundException;
import Command.modelall.exceptions.InvalidCommandArgumentsException;
import Command.modelall.exceptions.OperationCancelledException;

import java.io.File;
import java.net.URL;
import java.net.URISyntaxException;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CommandHandler {
    private final WorkerService service;
    private final InputManager inputManager;
    private final Map<String, Command> commands = new HashMap<>();
    private final LinkedList<String> history = new LinkedList<>();
    private static final Logger logger = Logger.getLogger(CommandHandler.class.getName());

    public CommandHandler(WorkerService service, InputManager inputManager) {
        this.service = service;
        this.inputManager = inputManager;
        try {
            registerCommandsUsingReflection("commands");
            injectDependencies();
            // Явно устанавливаем карту команд для HelpCommand
            Command helpCmd = commands.get("help");
            if (helpCmd instanceof HelpCommand) {
                ((HelpCommand) helpCmd).setCommands(commands);
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Ошибка при регистрации команд: " + e.getMessage(), e);
        }
    }

    private void registerCommandsUsingReflection(String packageName) throws Exception {
        for (Class<?> cls : findClasses(packageName)) {
            if (Command.class.isAssignableFrom(cls) && !cls.isInterface()) {
                try {
                    Command cmd = (Command) cls.getDeclaredConstructor().newInstance();
                    String commandName = cmd.toString().toLowerCase();
                    commands.put(commandName, cmd);
                } catch (Exception e) {
                    logger.log(Level.WARNING, "Не удалось создать экземпляр команды из класса: " + cls.getName(), e);
                }
            }
        }
    }

    private Set<Class<?>> findClasses(String packageName) throws URISyntaxException, ClassNotFoundException, Exception {
        Set<Class<?>> classes = new HashSet<>();
        String path = packageName.replace('.', '/');
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Enumeration<URL> resources = classLoader.getResources(path);
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            if ("file".equals(resource.getProtocol())) {
                File directory = new File(resource.toURI());
                if (!directory.exists()) {
                    throw new ClassNotFoundException("Папка не найдена: " + path);
                }
                for (File file : Objects.requireNonNull(directory.listFiles())) {
                    if (file.isFile() && file.getName().endsWith(".class")) {
                        String className = packageName + "." + file.getName().replace(".class", "");
                        classes.add(Class.forName(className));
                    }
                }
            } else if ("jar".equals(resource.getProtocol())) {
                String jarPath = resource.getPath().substring(5, resource.getPath().indexOf("!"));
                try (JarFile jarFile = new JarFile(jarPath)) {
                    Enumeration<JarEntry> entries = jarFile.entries();
                    while (entries.hasMoreElements()) {
                        JarEntry entry = entries.nextElement();
                        String entryName = entry.getName();
                        if (entryName.startsWith(path) && entryName.endsWith(".class") && !entry.isDirectory()) {
                            String className = entryName.replace('/', '.').replace(".class", "");
                            classes.add(Class.forName(className));
                        }
                    }
                }
            } else {
                throw new URISyntaxException(resource.toString(), "Unsupported protocol: " + resource.getProtocol());
            }
        }
        return classes;
    }

    private void injectDependencies() {
        for (Command cmd : commands.values()) {
            try {
                cmd.getClass().getMethod("setWorkerService", WorkerService.class).invoke(cmd, service);
            } catch (Exception ignored) {}
            try {
                cmd.getClass().getMethod("setInputManager", InputManager.class).invoke(cmd, inputManager);
            } catch (Exception ignored) {}
            try {
                cmd.getClass().getMethod("setHistory", LinkedList.class).invoke(cmd, history);
            } catch (Exception ignored) {}
            try {
                cmd.getClass().getMethod("setCommandHandler", CommandHandler.class).invoke(cmd, this);
            } catch (Exception ignored) {}
        }
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("> ");
            String input;
            try {
                input = scanner.nextLine().trim();
            } catch (NoSuchElementException e) {
                System.out.println("Ввод завершен. Выход из программы.");
                break;
            }
            if (input.isEmpty()) continue;
            String[] parts = input.split(" ", 2);
            String cmdName = parts[0].toLowerCase();
            String args = parts.length > 1 ? parts[1] : "";
            history.add(cmdName);
            if (history.size() > 9) history.removeFirst();
            try {
                Command cmd = commands.get(cmdName);
                if (cmd == null) {
                    throw new CommandNotFoundException("Неизвестная команда: " + cmdName);
                }
                cmd.execute(args);
            } catch (InvalidCommandArgumentsException | OperationCancelledException e) {
                System.out.println("⚠️ " + e.getMessage());
            } catch (CommandNotFoundException e) {
                System.out.println("❌ " + e.getMessage());
            } catch (Exception e) {
                System.out.println("❗ Внутренняя ошибка: " + e.getMessage());
            }
        }
    }

    public void runLine(String line) throws OperationCancelledException, InvalidCommandArgumentsException, CommandNotFoundException {
        if (line.isEmpty()) return;
        String[] parts = line.split(" ", 2);
        String cmdName = parts[0].toLowerCase();
        String args = parts.length > 1 ? parts[1] : "";
        history.add(cmdName);
        if (history.size() > 9) history.removeFirst();
        Command cmd = commands.get(cmdName);
        if (cmd == null) {
            throw new CommandNotFoundException("Неизвестная команда: " + cmdName);
        }
        cmd.execute(args);
    }
}
