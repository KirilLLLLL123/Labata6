package Command.modelall;


import Command.modelall.model.Worker;

import java.util.List;
import java.util.stream.Collectors;

/** Сборная «швейцарская» для CSV-операций. */
public final class CSVUtil {
    private CSVUtil() {}
    public static String toLines(List<Worker> list) {
        return list.stream().map(Worker::toCsv)
                .collect(Collectors.joining("\n"));
    }
}
