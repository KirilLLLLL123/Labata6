package Command.modelall.exceptions;

/**
 * Исключение, выбрасываемое, если данные не удовлетворяют требованиям.
 */
public class InvalidDataException extends Exception {
    public InvalidDataException(String message) {
        super(message);
    }
}
