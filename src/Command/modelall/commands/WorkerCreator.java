package Command.modelall.commands;

import Command.modelall.input.InputManager;
import Command.modelall.model.Coordinates;
import Command.modelall.model.Position;
import Command.modelall.model.Status;
import Command.modelall.model.Worker;
import Command.modelall.exceptions.OperationCancelledException;
import Command.modelall.exceptions.InvalidCommandArgumentsException;
import java.time.LocalDate;
import java.util.Arrays;

/**
 * Утилитный класс для создания объекта Worker посредством последовательного ввода данных.
 */
public class WorkerCreator {

    /**
     * Создает объект Worker, запрашивая данные у пользователя через InputManager.
     * Если пользователь вводит "cancel" на любом этапе, выбрасывается соответствующее исключение.
     *
     * @param inputManager менеджер ввода
     * @return созданный объект Worker
     * @throws OperationCancelledException если операция отменена пользователем
     * @throws InvalidCommandArgumentsException если введены некорректные данные
     */
    public static Worker createWorker(InputManager inputManager)
            throws OperationCancelledException, InvalidCommandArgumentsException {
        // Ввод имени
        String name = inputManager.getInput("Введите имя: ");
        while (name.isEmpty()) {
            name = inputManager.getInput("Ошибка: имя не может быть пустым. Введите имя: ");
        }

        // Ввод координат
        double x = inputManager.getDouble("Введите координату X (число, не больше 311): ", 0, 311);
        long y = inputManager.getLong("Введите координату Y (целое число): ");
        Coordinates coordinates;
        try {
            coordinates = new Coordinates(x, y);
        } catch (Exception e) {
            throw new InvalidCommandArgumentsException("Ошибка создания координат: " + e.getMessage());
        }

        // Ввод зарплаты
        double salary = inputManager.getDouble("Введите зарплату (число > 0): ", 1, Double.MAX_VALUE);

        // Ввод Position (enum)
        Position position = null;
        System.out.println("Доступные должности: "+ Arrays.toString(Position.values()));
        String posInput = inputManager.getInput("Введите позицию (или оставьте пустым для null): ");
        while (!posInput.isEmpty()) {
            try {
                position = Position.valueOf(posInput.toUpperCase());
                break;
            } catch (IllegalArgumentException e) {
                System.out.println("Ошибка! Введите одно из значений: " + Arrays.toString(Position.values()));
                posInput = inputManager.getInput("Введите позицию (или оставьте пустым для null): ");
            }
        }

        // Ввод Status (enum)
        Status status = null;
        System.out.println("Доступные статусы: " + Arrays.toString(Status.values()));
        String statInput = inputManager.getInput("Введите статус (или оставьте пустым для null): ");
        while (!statInput.isEmpty()) {
            try {
                status = Status.valueOf(statInput.toUpperCase());
                break;
            } catch (IllegalArgumentException e) {
                System.out.println("Ошибка! Введите одно из значений: " + Arrays.toString(Status.values()));
                statInput = inputManager.getInput("Введите статус (или оставьте пустым для null): ");
            }
        }

        // Ввод персональных данных (Person)
        LocalDate birthday = inputManager.getDate("Введите дату рождения (YYYY-MM-DD): ");
        long height = inputManager.getLong("Введите рост (число > 0): ", 1, Long.MAX_VALUE);
        int weight = inputManager.getInt("Введите вес (число > 0): ", 1, Integer.MAX_VALUE);
        Command.modelall.model.Person person;
        try {
            person = new Command.modelall.model.Person(birthday, height, weight);
        } catch (Exception e) {
            throw new InvalidCommandArgumentsException("Ошибка создания Person: " + e.getMessage());
        }

        try {
            return new Worker(name, coordinates, salary, position, status, person);
        } catch (Exception e) {
            throw new InvalidCommandArgumentsException("Ошибка создания Worker: " + e.getMessage());
        }
    }
}
