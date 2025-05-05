package Server;

import Command.modelall.Request;
import Command.modelall.Response;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/** Однопоточный, самый простой TCP-сервер. */
public class ConnectionListener {

    private static final int PORT = 5555;

    private final CommandDispatcher dispatcher;

    public ConnectionListener(CollectionManager cm) {
        this.dispatcher = new CommandDispatcher(cm);   // <<–– инициализация тут
    }

    public void start() throws Exception {
        try (ServerSocket ss = new ServerSocket(PORT)) {
            System.out.println("Сервер слушает порт " + PORT);

            while (true) {
                try (Socket s = ss.accept();
                     ObjectInputStream  in  = new ObjectInputStream(s.getInputStream());
                     ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream())) {

                    Request  req  = (Request) in.readObject();
                    Response resp = dispatcher.dispatch(req);
                    out.writeObject(resp);

                } catch (Exception e) {
                    // печатаем в консоль сервера, чтобы видеть ошибки
                    e.printStackTrace();
                }
            }
        }
    }
}
