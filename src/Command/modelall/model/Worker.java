package Command.modelall.model;

import Command.modelall.exceptions.InvalidDataException;
import java.time.ZonedDateTime;

/**
 * Класс Worker представляет работника.
 * Поля:
 * - id: значение > 0, уникально, генерируется автоматически.
 * - name: не может быть null или пустым.
 * - coordinates: объект Coordinates, не может быть null.
 * - creationDate: дата создания, не может быть null, генерируется автоматически.
 * - salary: не может быть null, > 0.
 * - position: объект Position, может быть null.
 * - status: объект Status, может быть null.
 * - person: объект Person, не может быть null.
 * Реализует сортировку по зарплате (сравнение по полю salary).
 */
public class Worker implements Comparable<Worker> {
    private static long idCounter = 1;
    private final long id;
    private final String name;
    private final Coordinates coordinates;
    private final ZonedDateTime creationDate;
    private final Double salary;
    private final Position position;
    private final Status status;
    private final Person person;

    public Worker(String name, Coordinates coordinates, Double salary, Position position, Status status, Person person) throws InvalidDataException {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidDataException("Имя не может быть пустым");
        }
        if (coordinates == null) {
            throw new InvalidDataException("Координаты не могут быть null");
        }
        if (salary == null || salary <= 0) {
            throw new InvalidDataException("Зарплата должна быть больше 0");
        }
        if (person == null) {
            throw new InvalidDataException("Person не может быть null");
        }

        this.id = idCounter++;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = ZonedDateTime.now();
        this.salary = salary;
        this.position = position;
        this.status = status;
        this.person = person;
    }

    @Override
    public int compareTo(Worker other) {
        return this.salary.compareTo(other.salary);
    }

    @Override
    public String toString() {
        return "Worker{id=" + id + ", name='" + name + "', coordinates=" + coordinates +
                ", creationDate=" + creationDate + ", salary=" + salary +
                ", position=" + position + ", status=" + status + ", person=" + person + "}";
    }

    public long getId() {
        return id;
    }

    public Double getSalary() {
        return salary;
    }

    public Position getPosition() {
        return position;
    }

    public String toCSV() {
        String positionStr = (position != null) ? position.name() : "";
        String statusStr = (status != null) ? status.name() : "";

        return id + "," +
                name + "," +
                coordinates.toCSV() + "," +
                salary + "," +
                positionStr + "," +
                statusStr + "," +
                person.toCSV();
    }


    public static Worker fromCSV(String csv) throws InvalidDataException {
        String[] parts = csv.split(",");
        if (parts.length < 10) {
            throw new InvalidDataException("Неверный формат CSV строки: " + csv);
        }
        String name = parts[1];
        Coordinates coordinates = Coordinates.fromCSV(parts[2] + "," + parts[3]);
        Double salary = Double.parseDouble(parts[4]);
        Position position = parts[5].isEmpty() ? null : Position.valueOf(parts[5]);
        Status status = parts[6].isEmpty() ? null : Status.valueOf(parts[6]);
        Person person = Person.fromCSV(parts[7] + "," + parts[8] + "," + parts[9]);
        return new Worker(name, coordinates, salary, position, status, person);
    }

    public Object toCsv() {
    }
}
