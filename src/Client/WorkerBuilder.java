package Client;

import Command.modelall.input.InputManager;
import Command.modelall.model.Worker;
import Command.modelall.commands.WorkerCreator;

/** Интерактивное построение Worker, переиспользует WorkerCreator из команд. */
public final class WorkerBuilder {

    private final InputManager input = new InputManager();   // читает из System.in

    /** Создаём готовый Worker, все вопросы задаёт WorkerCreator. */
    public Worker build() throws Exception {
        return WorkerCreator.createWorker(input);
    }
}
