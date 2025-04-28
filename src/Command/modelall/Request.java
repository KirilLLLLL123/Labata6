package Command.modelall;

import java.io.Serializable;
import java.util.List;

/**
 * Объект-запрос, который Клиент шлёт Серверу
 * 1-я строка протокола:  command,argument
 * 2-я:  worker-CSV
 * END:  маркер конца
 */
public class Request implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String command;      // имя команды
    private final String argument;     // «простой» аргумент или null
    private final String workerCsv;    // строка Worker или null

    public Request(String command, String argument, String workerCsv) {
        this.command   = command;
        this.argument  = argument;
        this.workerCsv = workerCsv;
    }


    public String command()      { return command; }
    public String argument()     { return argument; }
    public String workerCsv()    { return workerCsv; }

    /* кодирование */
    public String toFirstLine() {
        return CSVUtil.toCsvLine(List.of(command,
                argument == null ? "" : argument));
    }

    /* декодирование */
    public static Request fromLines(List<String> lines) {
        if (lines.isEmpty())
            throw new IllegalArgumentException("Пустой запрос");

        List<String> head = CSVUtil.parseCsvLine(lines.get(0));
        String cmd = head.get(0);
        String arg = head.size() > 1 && !head.get(1).isEmpty() ? head.get(1) : null;
        String wrk = lines.size() == 2 ? lines.get(1) : null;

        return new Request(cmd, arg, wrk);
    }
}
