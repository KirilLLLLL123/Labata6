package Command.modelall;

import java.io.Serializable;
import java.util.List;

public class Request implements Serializable {
    private final CommandName name;
    private final List<String> args;   // строковые аргументы
    private final Object payload;      // Worker, Long, List<Worker> …

    public Request(CommandName name, List<String> args, Object payload) {
        this.name = name;
        this.args = args;
        this.payload = payload;
    }

    public CommandName getName()   { return name; }
    public List<String> getArgs()  { return args; }
    public Object getPayload()     { return payload; }
}
