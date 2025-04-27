package Client;

import Command.modelall.model.Worker;
import Command.modelall.commands.WorkerCreator;

import java.util.Scanner;

/** Интерактивное построение Worker, переиспользует WorkerCreator из команд. */
public final class WorkerBuilder {

    private final WorkerCreator creator;

    public WorkerBuilder(Scanner sc) { this.creator = new WorkerCreator(sc); }

    public Worker build() throws Exception { return creator.build(); }
}

