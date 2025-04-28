package Command.modelall;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Ответ, который Сервер шлёт Клиенту
 * 1-я строка:  OK|ERROR,<message>
 * 2-я строка:  <count payload-lines>
 * далее:  payload-lines
 * END:  маркер конца
 */
public class Response implements Serializable {
    private static final long serialVersionUID = 1L;

    public final boolean ok;
    public final String  message;      // читается в ClientMain
    public final List<String> payload; // строки-данные (может быть пусто)

    public Response(boolean ok, String message, List<String> payload) {
        this.ok      = ok;
        this.message = message == null ? "" : message;
        this.payload = payload == null ? List.of() : List.copyOf(payload);
    }

    /* кодирование */
    public List<String> toLines() {
        List<String> lines = new ArrayList<>();
        lines.add(CSVUtil.toCsvLine(List.of(ok ? "OK" : "ERROR", message)));
        lines.add(String.valueOf(payload.size()));
        lines.addAll(payload);
        return lines;
    }

    /* декодирование */
    public static Response fromLines(List<String> lines) {
        if (lines.size() < 2)
            throw new IllegalArgumentException("Сломанный ответ сервера");

        var head = CSVUtil.parseCsvLine(lines.get(0));
        boolean ok = "OK".equals(head.get(0));
        String  msg = head.size() > 1 ? head.get(1) : "";
        int cnt  = Integer.parseInt(lines.get(1));

        if (lines.size() < 2 + cnt)
            throw new IllegalArgumentException("Payload size mismatch");

        return new Response(ok, msg, lines.subList(2, 2 + cnt));
    }
}
