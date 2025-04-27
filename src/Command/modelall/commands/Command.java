package Command.modelall.commands;

import Command.modelall.exceptions.InvalidCommandArgumentsException;
import Command.modelall.exceptions.OperationCancelledException;

public interface Command {
    void execute(String args) throws OperationCancelledException, InvalidCommandArgumentsException;
}
