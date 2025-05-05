package Command.modelall.input;

import Command.modelall.exceptions.OperationCancelledException;
import java.time.LocalDate;
import java.util.Scanner;
import java.util.Arrays;


public class InputManager {
    private final Scanner scanner = new Scanner(System.in);

    public String getInput(String prompt) throws OperationCancelledException {
        System.out.print(prompt);
        String input = scanner.nextLine().trim();
        if ("cancel".equalsIgnoreCase(input)) {
            throw new OperationCancelledException("Операция отменена пользователем.");
        }
        return input;
    }

    /**
     * Запрашивает ввод значения перечисления (enum) с проверкой на корректность.
     *
     * @param enumClass класс перечисления
     * @param prompt текст приглашения
     * @param <T> тип перечисления
     * @return выбранное значение перечисления или null, если пользователь оставил ввод пустым
     * @throws OperationCancelledException если операция отменена пользователем
     */
    public <T extends Enum<T>> T getEnum(Class<T> enumClass, String prompt) throws OperationCancelledException {
        while (true) {
            System.out.print(prompt);
            String input = getInput("").trim();
            if (input.isEmpty()) return null; // пустой ввод интерпретируется как null
            try {
                return Enum.valueOf(enumClass, input.toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.println("Ошибка! Введите одно из значений: " + Arrays.toString(enumClass.getEnumConstants()));
            }
        }
    }

    /**
     * Запрашивает ввод значения типа double в заданном диапазоне.
     *
     * @param prompt текст приглашения
     * @param min минимальное значение
     * @param max максимальное значение
     * @return введённое число
     * @throws OperationCancelledException если операция отменена
     */
    public double getDouble(String prompt, double min, double max) throws OperationCancelledException {
        while (true) {
            try {
                String input = getInput(prompt);
                double value = Double.parseDouble(input);
                if (value < min || value > max) {
                    System.out.println("Ошибка: число должно быть в диапазоне (" + min + ", " + max + ").");
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: введите корректное число.");
            }
        }
    }

    /**
     * Запрашивает ввод целого числа.
     *
     * @param prompt текст приглашения
     * @return введённое число
     * @throws OperationCancelledException если операция отменена
     */
    public long getLong(String prompt) throws OperationCancelledException {
        while (true) {
            try {
                String input = getInput(prompt);
                return Long.parseLong(input);
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: введите корректное целое число.");
            }
        }
    }

    /**
     * Запрашивает ввод целого числа в заданном диапазоне.
     *
     * @param prompt текст приглашения
     * @param min минимальное значение
     * @param max максимальное значение
     * @return введённое число
     * @throws OperationCancelledException если операция отменена
     */
    public long getLong(String prompt, long min, long max) throws OperationCancelledException {
        while (true) {
            try {
                String input = getInput(prompt);
                long value = Long.parseLong(input);
                if (value < min || value > max) {
                    System.out.println("Ошибка: число должно быть в диапазоне (" + min + ", " + max + ").");
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: введите корректное целое число.");
            }
        }
    }

    /**
     * Запрашивает ввод целого числа (int) в заданном диапазоне.
     *
     * @param prompt текст приглашения
     * @param min минимальное значение
     * @param max максимальное значение
     * @return введённое число
     * @throws OperationCancelledException если операция отменена
     */
    public int getInt(String prompt, int min, int max) throws OperationCancelledException {
        while (true) {
            try {
                String input = getInput(prompt);
                int value = Integer.parseInt(input);
                if (value < min || value > max) {
                    System.out.println("Ошибка: число должно быть в диапазоне (" + min + ", " + max + ").");
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: введите корректное целое число.");
            }
        }
    }

    /**
     * Запрашивает ввод даты в формате YYYY-MM-DD.
     *
     * @param prompt текст приглашения
     * @return введенная дата в виде LocalDate
     * @throws OperationCancelledException если операция отменена
     */
    public LocalDate getDate(String prompt) throws OperationCancelledException {
        while (true) {
            try {
                String input = getInput(prompt);
                return LocalDate.parse(input);
            } catch (Exception e) {
                System.out.println("Ошибка: введите дату в формате YYYY-MM-DD.");
            }
        }
    }

    public Scanner getScanner() {
        return scanner;
    }
}
