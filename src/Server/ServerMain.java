package Server;
/** точка входа сервера */

import java.io.File;

public class ServerMain {
    public static void main(String[] args) {
        String file = "workers.csv";                      // можно заменить на args[0]

        try {
            // если файла нет — создаём пустой
            new File(file).createNewFile();

            var storage = new CSVStorage(file);
            var cm      = new CollectionManager(storage);

            try {
                cm.load();                                // читаем коллекцию
            } catch (Exception e) {                      // повреждённый CSV
                System.err.println("WARNING: " + e.getMessage());
            }

            // сохраняем при выключении
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try { cm.save(); } catch (Exception ignored) {}
            }));

            new ConnectionListener().start();          // старт сервера
        } catch (Exception e) {
            System.err.println("SERVER: " + e.getMessage());
        }
    }
}