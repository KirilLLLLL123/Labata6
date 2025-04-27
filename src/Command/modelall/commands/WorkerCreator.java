package Command.modelall.commands;

import Command.modelall.input.InputManager;
import Command.modelall.model.Coordinates;
import Command.modelall.model.Person;
import Command.modelall.model.Position;
import Command.modelall.model.Status;
import Command.modelall.model.Worker;
import Command.modelall.exceptions.InvalidCommandArgumentsException;
import Command.modelall.exceptions.OperationCancelledException;

import java.time.LocalDate;
import java.util.Arrays;

/**
 * Утилитный класс: пошагово спрашивает поля и строит Worker.
 */
public final class WorkerCreator {

    private WorkerCreator() { }                              // util-class

    /**
     * @param inputManager источник ввода (консоль/скрипт)
     * @return готовый Worker
     */
    public static Worker createWorker(InputManager inputManager)
            throws OperationCancelledException, InvalidCommandArgumentsException {

        /* -------- имя -------- */
        String name = inputManager.getInput("Введите имя: ");
        while (name.isBlank()) {
            name = inputManager.getInput("Ошибка: имя не может быть пустым. Введите имя: ");
        }

        /* -------- координаты -------- */
        double x = inputManager.getDouble(
                "Введите координату X (число, не больше 311): ", 0, 311);
        long   y = inputManager.getLong(
                "Введите координату Y (целое число): ");
        Coordinates coordinates;
        try {
            coordinates = new Coordinates(x, y);
        } catch (Exception e) {
            throw new InvalidCommandArgumentsException(
                    "Ошибка создания координат: " + e.getMessage());
        }

        /* -------- зарплата -------- */
        double salary = inputManager.getDouble(
                "Введите зарплату (число > 0): ", 1, Double.MAX_VALUE);

        /* -------- Position -------- */
        Position position = null;
        System.out.println("Доступные должности: " + Arrays.toString(Position.values()));
        String posInput = inputManager.getInput(
                "Введите должность (или оставьте пустым для null): ");
        while (!posInput.isEmpty()) {
            try {
                position = Position.valueOf(posInput.toUpperCase());
                break;
            } catch (IllegalArgumentException e) {
                System.out.println("Ошибка! Введите одно из значений: "
                        + Arrays.toString(Position.values()));
                posInput = inputManager.getInput(
                        "Введите должность (или оставьте пустым для null): ");
            }
        }

        /* -------- Status -------- */
        Status status = null;
        System.out.println("Доступные статусы: " + Arrays.toString(Status.values()));
        String statInput = inputManager.getInput(
                "Введите статус (или оставьте пустым для null): ");
        while (!statInput.isEmpty()) {
            try {
                status = Status.valueOf(statInput.toUpperCase());
                break;
            } catch (IllegalArgumentException e) {
                System.out.println("Ошибка! Введите одно из значений: "
                        + Arrays.toString(Status.values()));
                statInput = inputManager.getInput(
                        "Введите статус (или оставьте пустым для null): ");
            }
        }

        /* -------- Person -------- */
        LocalDate birthday = inputManager.getDate("Введите дату рождения (YYYY-MM-DD): ");
        long      height   = inputManager.getLong ("Введите рост  (число > 0): ",
                1, Long.MAX_VALUE);
        int       weight   = inputManager.getInt  ("Введите вес   (число > 0): ",
                1, Integer.MAX_VALUE);
        Person person;
        try {
            person = new Person(birthday, height, weight);
        } catch (Exception e) {
            throw new InvalidCommandArgumentsException(
                    "Ошибка создания Person: " + e.getMessage());
        }

        /* -------- итоговый Worker -------- */
        try {
            return new Worker(name, coordinates, salary, position, status, person);
        } catch (Exception e) {
            throw new InvalidCommandArgumentsException(
                    "Ошибка создания Worker: " + e.getMessage());
        }
    }
}
