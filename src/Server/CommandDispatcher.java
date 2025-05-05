package Server;

import Command.modelall.Request;
import Command.modelall.Response;
import Command.modelall.model.Position;
import Command.modelall.model.Worker;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Выполняет ВСЕ команды коллекции.
 * Хранит историю (max 13), использует CollectionManager, не требует интерактивного ввода.
 */
public class CommandDispatcher {

    private final CollectionManager cm;
    private final Deque<String> history = new ArrayDeque<>();

    public CommandDispatcher(CollectionManager cm) { this.cm = cm; }

    /* ------------------------------------------------------------ */

    public Response dispatch(Request r) {

        try {
            String cmd = r.command().toLowerCase(Locale.ROOT);
            addHistory(cmd);

            return switch (cmd) {
                /* справка */
                case "help"   -> ok(help());

                /* коллекция */
                case "show" -> {
                    List<String> workersCsv = cm.view().values().stream()
                            .map(Worker::toCSV)  // используйте toCSV() с заглавной буквы
                            .collect(Collectors.toList());
                    yield ok(workersCsv);
                }

                case "info"   -> ok(info());
                case "clear"  -> { cm.inner().clear(); yield ok("Коллекция очищена"); }

                /* history */
                case "history"-> ok(new ArrayList<>(history));

                /* вставка / обновление */
                case "insert" -> ok(insert(r));
                case "update" -> ok(update(r));

                /* удаление / замена */
                case "remove_key"        -> ok(removeKey(r));
                case "remove_lower_key"  -> ok(removeLower(r));
                case "replace_if_greater"-> ok(replaceIfGreater(r));

                /* подсчёты */
                case "sum_of_salary" -> {
                    double s = cm.view().values().stream()
                            .mapToDouble(Worker::getSalary).sum();
                    yield ok(String.valueOf(s));
                }
                case "count_greater_than_position" -> ok(countGreater(r));
                case "print_field_ascending_salary" ->
                        ok(cm.view().values().stream()
                                .sorted(Comparator.comparingDouble(Worker::getSalary))
                                .map(w -> String.valueOf(w.getSalary()))
                                .toList());

                /* execute_script / exit клиентские; save серверная */
                case "execute_script", "exit", "save" ->
                        error("Команда «" + cmd + "» недоступна по сети");

                default -> error("Неизвестная команда: " + cmd);
            };

        } catch (Exception e) {                       // любая ошибка → ERROR
            return error(e.getMessage());
        }
    }

    /* ---------------- конкретные операции ---------------- */

    private String insert(Request r) throws Exception {
        requireCsv(r);
        long k = Long.parseLong(r.argument());
        if (cm.inner().containsKey((int) k))
            return "Ключ уже существует, insert пропущен";
        cm.inner().put((int) k, Worker.fromCSV(r.workerCsv()));
        return "Элемент добавлен";
    }

    private String update(Request r) throws Exception {
        requireCsv(r);
        long k = Long.parseLong(r.argument());
        if (!cm.inner().containsKey((int) k))
            return "Ключ не найден";
        cm.inner().put((int) k, Worker.fromCSV(r.workerCsv()));
        return "Элемент обновлён";
    }

    private String removeKey(Request r) {
        long k = Long.parseLong(r.argument());
        return cm.inner().remove((int) k) != null ?
                "Удалён элемент с ключом " + k :
                "Ключ не найден";
    }

    private String removeLower(Request r) {
        long k = Long.parseLong(r.argument());
        cm.inner().keySet().removeIf(key -> key < k);
        return "Удалены элементы с ключом < " + k;
    }

    private String replaceIfGreater(Request r) throws Exception {
        requireCsv(r);
        long k = Long.parseLong(r.argument());
        Worker newW = Worker.fromCSV(r.workerCsv());
        Worker old  = cm.inner().get((int) k);
        if (old == null) return "Ключ не найден";

        if (newW.compareTo(old) > 0) {
            cm.inner().put((int) k, newW);
            return "Заменён, новый элемент больше";
        }
        return "Не заменён: новый элемент не больше старого";
    }

    private String countGreater(Request r) {
        Position p = Position.valueOf(r.argument().toUpperCase());
        long c = cm.view().values().stream()
                .filter(w -> w.getPosition()!=null && w.getPosition().compareTo(p) > 0)
                .count();
        return String.valueOf(c);
    }

    /* ---------------- util ---------------- */

    private void addHistory(String c) {
        history.addLast(c);
        if (history.size() > 13) history.removeFirst();
    }

    private static void requireCsv(Request r) {
        if (r.workerCsv()==null || r.workerCsv().isBlank())
            throw new IllegalArgumentException("Для команды нужен WorkerCSV");
    }

    private List<String> help() {
        return List.of(
                "help                                        - справка",
                "info                                        - информация о коллекции",
                "show                                        - показать все элементы",
                "insert <key> + Worker                      - добавить элемент",
                "update <key> + Worker                      - обновить элемент",
                "remove_key <key>                            - удалить по ключу",
                "remove_lower_key <key>                      - удалить ключи меньше",
                "replace_if_greater <key> + Worker           - заменить, если больше",
                "clear                                       - очистить коллекцию",
                "history                                     - последние 13 команд",
                "count_greater_than_position <pos>           - сколько позиций >",
                "print_field_ascending_salary                - зарплаты по возрастанию",
                "sum_of_salary                               - сумма зарплат",
                "execute_script <file>                       - выполнить скрипт (локально)",
                "exit                                        - завершить клиент"
        );
    }

    private List<String> info() {
        return List.of(
                "Тип: " + cm.view().getClass().getName(),
                "Элементов: " + cm.view().size(),
                "Дата: " + java.time.ZonedDateTime.now()
        );
    }

    private static List<String> toCSV(Collection<Worker> c) {
        return c.stream().map(Worker::toCSV).collect(Collectors.toList()); // исправлено на toCSV()
    }


    private Response ok(String s)            { return ok(List.of(s)); }
    private Response ok(List<String> lines)  { return new Response(true, "OK", lines); }
    private Response error(String m)         { return new Response(false, m, List.of()); }
}
