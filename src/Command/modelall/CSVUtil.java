package Command.modelall;

import Command.modelall.model.Worker;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/** Сборная «швейцарская» для CSV-операций. */
public final class CSVUtil {
    private CSVUtil() {}

    /* ===== работа с коллекциями работников (была в исходнике) ===== */
    public static String toLines(List<Worker> list) {
        return list.stream().map(Worker::toCsv)
                .collect(Collectors.joining("\n"));
    }

    /* ===== вспомогательные методы для сетевого протокола ===== */

    /** Склеиваем поля в одну CSV-строку (с экранированием). */
    public static String toCsvLine(List<String> fields) {
        return fields.stream()
                .map(CSVUtil::escape)
                .collect(Collectors.joining(","));
    }
    /** Разбиваем CSV-строку на поля (без кавычек). */
    public static List<String> parseCsvLine(String line) {
        return Arrays.stream(line.split(",", -1))
                .map(CSVUtil::unescape)
                .toList();
    }

    /* --- tiny helpers --- */
    private static String escape(String s) {
        if (s.contains(",") || s.contains("\""))
            return "\"" + s.replace("\"", "\"\"") + "\"";
        return s;
    }
    private static String unescape(String s) {
        if (s.startsWith("\"") && s.endsWith("\""))
            return s.substring(1, s.length() - 1).replace("\"\"", "\"");
        return s;
    }
}
