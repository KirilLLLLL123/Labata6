package Command.modelall.model;

import Command.modelall.exceptions.InvalidDataException;

public class Coordinates {
    private final double x;
    private final Long y;

    public Coordinates(double x, Long y) throws InvalidDataException {
        if (x > 311) {
            throw new InvalidDataException("Значение x не может быть больше 311");
        }
        if (y == null) {
            throw new InvalidDataException("Значение y не может быть null");
        }
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "Coordinates{x=" + x + ", y=" + y + '}';
    }

    public String toCSV() {
        return x + "," + y;
    }

    public static Coordinates fromCSV(String csv) throws InvalidDataException {
        String[] parts = csv.split(",");
        if (parts.length < 2) {
            throw new InvalidDataException("Неверный формат координат: " + csv);
        }
        double x = Double.parseDouble(parts[0]);
        Long y = Long.parseLong(parts[1]);
        return new Coordinates(x, y);
    }
}
