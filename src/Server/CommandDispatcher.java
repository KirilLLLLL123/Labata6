package Server;

import Command.modelall.Request;
import Command.modelall.Response;

import java.util.List;

/**
 * Простая заглушка: подтверждает получение любой команды.
 */
public class CommandDispatcher {

    public Response dispatch(Request req) {
        StringBuilder sb = new StringBuilder("Команда «")
                .append(req.command()).append('»');

        if (req.argument() != null && !req.argument().isBlank())
            sb.append(" (").append(req.argument()).append(')');
        if (req.workerCsv() != null)
            sb.append(" + WorkerCSV");

        return new Response(true, sb.toString(), List.of());
    }
}
