package Server;
/** выполняет команду */

import Command.modelall.commands.CommandHandler;
import Command.modelall.commands.CommandName;
import Command.modelall.Request;
import Command.modelall.Response;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/** Получает Request, вызывает вашу логику команд и формирует Response. */
public class CommandDispatcher {

    private final CommandHandler handler;

    public CommandDispatcher(CommandHandler handler) {
        this.handler = handler;
    }

    public Response dispatch(Request req) {
        try {
            Object res;

            if (req.workerCsv() != null) {
                // команды insert, update
                var worker = Command.modelall.model.Worker.fromCsv(req.workerCsv());
                res = handler.execute(req.command(), worker, req.argument());
            } else {
                res = handler.execute(req.command(), req.argument());
            }

            /* если вернули коллекцию — отсортировать и отдать как CSV-строки */
            List<String> payload = null;
            if (res instanceof Map<?,?> map) {
                map = new TreeMap<>(map);
                payload = map.values().stream()
                        .map(o -> ((Command.modelall.model.Worker)o).toCsv())
                        .toList();
            } else if (res != null) {
                payload = List.of(String.valueOf(res));
            }
            return new Response(true, "OK", payload);
        } catch (Exception e) {
            return new Response(false, e.getMessage(), null);
        }
    }
}

