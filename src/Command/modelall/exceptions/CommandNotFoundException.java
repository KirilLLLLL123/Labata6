package Command.modelall.exceptions;

/**
 * Исключение, выбрасываемое, когда введена неизвестная команда.
 */
public class CommandNotFoundException extends Exception {
    public CommandNotFoundException(String message) {
        super(message);
    }
}
