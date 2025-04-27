package Client;

import Command.modelall.Request;
import Command.modelall.Response;
import Command.modelall.model.Worker;

import java.util.Scanner;

public class ClientMain {
    public static void main(String[] args) {
        String host = args.length>0 ? args[0] : "localhost";
        int    port = 5555;

        try (var sc   = new Scanner(System.in);
             var conn = new Connection(host, port)) {

            var console = new ConsoleReader(sc);
            var builder = new WorkerBuilder();

            while (true) {
                String line = console.readLine();
                if (line == null || line.isBlank()) continue;
                if (line.equalsIgnoreCase("exit")) break;

                String[] parts = line.split("\\s+",2);
                String cmd = parts[0];
                String arg = parts.length==2 ? parts[1] : null;

                String wCsv = switch (cmd) {
                    case "insert","update" -> builder.build().toCsv();
                    default -> null;
                };

                Request  req  = new Request(cmd, arg, wCsv);
                Response resp = conn.request(req);

                System.out.println(resp.message);
                resp.payload.forEach(System.out::println);
            }
        } catch (Exception e) {
            System.err.println("CLIENT: " + e.getMessage());
        }
    }
}

