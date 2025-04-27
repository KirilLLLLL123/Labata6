package Server;
/** выполняет команду */

import Command.modelall.Request;
import Command.modelall.Response;
import Command.modelall.commands.CommandHandler;

import java.util.List;

/** SERVER-side: пока просто подтверждаем получение команды. */
public class CommandDispatcher {

    public CommandDispatcher(CommandHandler commandHandler) {
    }

    public Response dispatch(Request req) {
        // TODO: заменить на настоящую обработку команд, когда будете готовы
        String msg = "Команда \"" + req.command() + "\" получена";
        return new Response(true, msg, List.of());
    }
}


