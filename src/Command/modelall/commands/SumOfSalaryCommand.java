package Command.modelall.commands;

import Command.modelall.service.WorkerService;
import Command.modelall.exceptions.OperationCancelledException;
import Command.modelall.exceptions.InvalidCommandArgumentsException;

public class SumOfSalaryCommand implements Command {
    private WorkerService service;

    public SumOfSalaryCommand() {
        // Пустой конструктор
    }

    public void setWorkerService(WorkerService service) {
        this.service = service;
    }

    @Override
    public void execute(String args) throws OperationCancelledException, InvalidCommandArgumentsException {
        service.sumOfSalary();
    }

    @Override
    public String toString() {
        return "sum_of_salary";
    }
}
