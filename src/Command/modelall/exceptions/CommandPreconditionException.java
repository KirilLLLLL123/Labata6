package Command.modelall.exceptions;

/**
 * Исключение, выбрасываемое при невозможности выполнить команду из-за нарушенных условий.
 */
public class CommandPreconditionException extends Exception {

    public CommandPreconditionException(String message) {
        super(message);
    }
}
