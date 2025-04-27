package Command.modelall.exceptions;

/**
 * Исключение, выбрасываемое при обнаружении некорректного формата CSV.
 */
public class CSVFormatException extends Exception {
    public CSVFormatException(String message) {
        super(message);
    }
}
