package Command.modelall.exceptions;

/**
 * Исключение, выбрасываемое при отмене операции пользователем (например, при вводе "cancel").
 */
public class OperationCancelledException extends Exception {
    public OperationCancelledException(String message) {
        super(message);
    }
}
