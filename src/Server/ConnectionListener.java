package Server;



import Command.modelall.Request;
import Command.modelall.Response;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class ConnectionListener {

    private static final int PORT = 5555;
    private final CommandDispatcher dispatcher = new CommandDispatcher();

    public void start() throws Exception {
        try (ServerSocket ss = new ServerSocket(PORT)) {
            System.out.println("Сервер слушает порт " + PORT);
            while (true) {
                try (Socket s = ss.accept();
                     ObjectInputStream  in  = new ObjectInputStream(s.getInputStream());
                     ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream())) {

                    Request req  = (Request) in.readObject();
                    Response resp = dispatcher.dispatch(req);
                    out.writeObject(resp);

                } catch (Exception e) {                     // любое исключение → ERROR
                    new ObjectOutputStream(
                            ss.accept().getOutputStream())
                            .writeObject(new Response(false, e.getMessage(), List.of()));
                }
            }
        }
    }
}
