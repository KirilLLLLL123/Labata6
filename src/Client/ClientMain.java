package Client;

import Command.modelall.Request;
import Command.modelall.Response;

import java.util.Scanner;

public class ClientMain {
    public static void main(String[] args) throws Exception {
        String host = args.length > 0 ? args[0] : "localhost";
        int port = 5555;

        var scanner = new Scanner(System.in);
        var console = new ConsoleReader(scanner);
        var builder = new WorkerBuilder(scanner);

        while (true) {
            String line = console.readLine();
            if (line == null || line.isBlank()) continue;
            if (line.equalsIgnoreCase("exit")) break;

            String[] parts = line.split("\\s+", 2);
            String cmd = parts[0];
            String arg = (parts.length == 2) ? parts[1] : null;

            String wCsv = null;
            if ("insert".equals(cmd) || "update".equals(cmd)) {
                wCsv = builder.build().toCSV();  // Получаем строку CSV для Worker
            }

            Request req = new Request(cmd, arg, wCsv);
            try (Connection conn = new Connection(host, port)) {
                Response resp = conn.request(req);

                System.out.println(resp.message);
                resp.payload.forEach(System.out::println);
            }
        }
    }
}
