package Command.modelall.model;

import Command.modelall.exceptions.InvalidDataException;
import java.time.LocalDate;

public class Person {
    private final LocalDate birthday;
    private final long height;
    private final int weight;

    public Person(LocalDate birthday, long height, int weight) throws InvalidDataException {
        if (birthday == null) {
            throw new InvalidDataException("Дата рождения не может быть null");
        }
        if (height <= 0) {
            throw new InvalidDataException("Рост должен быть больше 0");
        }
        if (weight <= 0) {
            throw new InvalidDataException("Вес должен быть больше 0");
        }
        this.birthday = birthday;
        this.height = height;
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "Person{birthday=" + birthday + ", height=" + height + ", weight=" + weight + '}';
    }

    public String toCSV() {
        return birthday.toString() + "," + height + "," + weight;
    }

    public static Person fromCSV(String csv) throws InvalidDataException {
        String[] parts = csv.split(",");
        if (parts.length < 3) {
            throw new InvalidDataException("Неверный формат данных Person: " + csv);
        }
        LocalDate birthday = LocalDate.parse(parts[0]);
        long height = Long.parseLong(parts[1]);
        int weight = Integer.parseInt(parts[2]);
        return new Person(birthday, height, weight);
    }
}
