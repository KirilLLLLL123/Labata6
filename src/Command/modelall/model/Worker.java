package Command.modelall.model;

import Command.modelall.exceptions.InvalidDataException;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

public class Worker implements Comparable<Worker>, Serializable {

    private static final long serialVersionUID = 1L;
    private static final AtomicLong ID_GEN = new AtomicLong(1);

    private final long id;
    private final String name;
    private final Coordinates coordinates;
    private final ZonedDateTime creationDate;
    private final Double salary;
    private final Position position;
    private final Status status;
    private final Person person;

    public Worker(String name, Coordinates coordinates, Double salary, Position position, Status status, Person person) throws InvalidDataException {
        if (name == null || name.isBlank()) throw new InvalidDataException("name пустой");
        if (coordinates == null) throw new InvalidDataException("coords null");
        if (salary == null || salary <= 0) throw new InvalidDataException("salary ≤ 0");
        if (person == null) throw new InvalidDataException("person null");

        this.id = ID_GEN.getAndIncrement();
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = ZonedDateTime.now();
        this.salary = salary;
        this.position = position;
        this.status = status;
        this.person = person;
    }

    // Метод для сериализации Worker в CSV
    public String toCSV() {
        String pos = (position != null) ? position.name() : "";  // Проверка на null для Position
        String st  = (status != null) ? status.name() : "";   // Проверка на null для Status
        return id + "," + name + "," +
                coordinates.toCSV() + "," +
                salary + "," + pos + "," + st + "," +
                person.toCSV();
    }

    // Метод для десериализации Worker из CSV
    public static Worker fromCSV(String csv) throws Exception {
        String[] f = csv.split(",", -1);
        if (f.length < 10) {
            throw new IllegalArgumentException("Ожидается 10 полей CSV");
        }

        String name = f[1].trim();
        double x = Double.parseDouble(f[2].trim());
        long y = Long.parseLong(f[3].trim());
        double salary = Double.parseDouble(f[4].trim());
        Position position = f[5].isEmpty() ? null : Position.valueOf(f[5].trim());
        Status status = f[6].isEmpty() ? null : Status.valueOf(f[6].trim());
        LocalDate birthday = LocalDate.parse(f[7].trim());
        long height = Long.parseLong(f[8].trim());
        int weight = Integer.parseInt(f[9].trim());

        Coordinates coords = new Coordinates(x, y);
        Person person = new Person(birthday, height, weight);

        return new Worker(name, coords, salary, position, status, person);
    }

    @Override public int compareTo(Worker o) { return salary.compareTo(o.salary); }
    @Override public String toString() { return toCSV(); }
    @Override public boolean equals(Object o) { return (this == o) || (o instanceof Worker w && id == w.id); }
    @Override public int hashCode() { return Objects.hash(id); }

    // Геттеры
    public long getId() { return id; }
    public Double getSalary() { return salary; }
    public Position getPosition() { return position; }
}
