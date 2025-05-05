package Client;

import Command.modelall.exceptions.InvalidDataException;
import Command.modelall.model.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Строит Worker из пользовательского ввода.
 * Каждый ввод проверяется внутри цикла while(true), поэтому
 * приложение не может «упасть» из‑за некорректных данных.
 */
public class WorkerBuilder {

    private final WorkerCreator creator;

    public WorkerBuilder(Scanner sc) {
        this.creator = new WorkerCreator(sc);
    }

    /** Построить Worker (ошибки ввода не «пробивают» наружу). */
    public Worker build() {
        return creator.build();
    }

    /* --------------------------------------------------------------------- */

    public static class WorkerCreator {
        private final Scanner sc;

        public WorkerCreator(Scanner sc) {
            this.sc = sc;
        }

        /** Считывает все поля и возвращает готовый Worker. */
        public Worker build() {
            while (true) {                                           // повторяем всё при InvalidDataException
                try {
                    String   name        = readName();
                    Coordinates coordinates = readCoordinates();
                    double   salary      = readSalary();
                    Position  position    = readEnum(Position.class,
                            "Доступные должности", true);
                    Status    status      = readEnum(Status.class,
                            "Доступные статусы", true);
                    LocalDate birthday    = readDate();
                    long     height      = readLong(
                            "Введите рост (число > 0): ",
                            1, Long.MAX_VALUE,
                            "Ошибка: рост должен быть числом больше нуля");
                    int      weight      = (int) readLong(
                            "Введите вес (число > 0): ",
                            1, Integer.MAX_VALUE,
                            "Ошибка: вес должен быть числом больше нуля");

                    Person person = new Person(birthday, height, weight); // может бросить InvalidDataException
                    return new Worker(name, coordinates, salary,
                            position, status, person);          // может бросить InvalidDataException

                } catch (InvalidDataException ex) {
                    System.out.println("Ошибка " + ex.getMessage());
                    System.out.println("Попробуем ещё раз с самого начала.\n");
                    // цикл while(true) повторит весь ввод
                }
            }
        }

        /* ------------------ чтение отдельных полей ------------------ */

        private String readName() {
            while (true) {
                System.out.print("Введите имя: ");
                String name = sc.nextLine().trim();
                if (!name.isEmpty()) return name;
                System.out.println("Ошибка: имя не может быть пустым.");
            }
        }

        private Coordinates readCoordinates() {
            while (true) {
                try {
                    System.out.print("Введите координату X (число, не больше 311): ");
                    double x = readDouble(
                            0, 311,
                            "Ошибка: координата X должна быть числом ≤ 311");

                    System.out.print("Введите координату Y (целое число): ");
                    long y = readLong(
                            Long.MIN_VALUE, Long.MAX_VALUE,
                            "Ошибка: координата Y должна быть целым числом");

                    return new Coordinates(x, y);             // может бросить InvalidDataException
                } catch (InvalidDataException ex) {
                    System.out.println("Ошибка " + ex.getMessage());
                }
            }
        }

        private double readSalary() {
            System.out.print("Введите зарплату (число > 0): ");
            return readDouble(
                    0, Double.MAX_VALUE,
                    "Ошибка: зарплата должна быть числом больше нуля");
        }

        /** Универсальный метод чтения enum (с возможностью null). */
        private <E extends Enum<E>> E readEnum(Class<E> type,
                                               String title,
                                               boolean allowNull) {
            String variants = Arrays.toString(type.getEnumConstants());
            while (true) {
                System.out.println(title + ": " + variants);
                System.out.print("Введите значение"
                        + (allowNull ? " (или оставьте пустым для null): "
                        : ": "));
                String input = sc.nextLine().trim();
                if (input.isEmpty() && allowNull) return null;
                try {
                    return Enum.valueOf(type, input.toUpperCase());
                } catch (IllegalArgumentException ex) {
                    System.out.println("Ошибка: допустимые значения "
                            + variants);
                }
            }
        }

        private LocalDate readDate() {
            while (true) {
                System.out.print("Введите дату рождения (YYYY-MM-DD): ");
                String raw = sc.nextLine().trim();
                if (raw.isEmpty()) return null; // поле необязательное
                try {
                    LocalDate d = LocalDate.parse(raw);
                    if (d.isAfter(LocalDate.now())) {
                        System.out.println("Ошибка: дата не может быть в будущем.");
                        continue;
                    }
                    return d;
                } catch (DateTimeParseException ex) {
                    System.out.println("Ошибка: формат YYYY-MM-DD, пример 1990-05-17");
                }
            }
        }

        /* ------------- базовые методы чтения чисел ------------- */

        private double readDouble(double minExclusive, double maxInclusive,
                                  String errorMessage) {
            while (true) {
                try {
                    double value = Double.parseDouble(sc.nextLine().trim());
                    if (value > minExclusive && value <= maxInclusive) return value;
                } catch (NumberFormatException ignored) { }
                System.out.println(errorMessage);
            }
        }

        private long readLong(String prompt,
                              long minInclusive, long maxInclusive,
                              String errorMessage) {
            System.out.print(prompt);
            return readLong(minInclusive, maxInclusive, errorMessage);
        }

        private long readLong(long minInclusive, long maxInclusive,
                              String errorMessage) {
            while (true) {
                try {
                    long value = Long.parseLong(sc.nextLine().trim());
                    if (value >= minInclusive && value <= maxInclusive) return value;
                } catch (NumberFormatException ignored) { }
                System.out.println(errorMessage);
            }
        }
    }
}
