package Command.modelall.exceptions;

/**
 * Исключение, выбрасываемое при ошибках доступа к файлам.
 */
public class StorageAccessException extends Exception {
    public StorageAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
