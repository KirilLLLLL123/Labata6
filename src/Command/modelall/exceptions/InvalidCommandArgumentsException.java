package Command.modelall.exceptions;

/**
 * Исключение, выбрасываемое, если аргументы команды некорректны.
 */
public class InvalidCommandArgumentsException extends Exception {
    public InvalidCommandArgumentsException(String message) {
        super(message);
    }
}
