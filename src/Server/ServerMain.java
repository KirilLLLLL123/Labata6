package Server;
/** точка входа сервера */

public class ServerMain {
    public static void main(String[] args) {
        try {
            var storage = new CSVStorage("workers.csv");
            var cm      = new CollectionManager(storage);
            cm.load();

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try { cm.save(); } catch (Exception ignored) {}
            }));

            new ConnectionListener(cm).start();
        } catch (Exception e) {
            System.err.println("SERVER: " + e.getMessage());
        }
    }
}

