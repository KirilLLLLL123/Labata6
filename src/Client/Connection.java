package Client;



import Command.modelall.Request;
import Command.modelall.Response;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/** Один запрос — одно TCP-соединение, Object( de )serialization. */
public class Connection implements AutoCloseable {

    private final Socket socket;
    private final ObjectOutputStream out;
    private final ObjectInputStream  in;

    public Connection(String host, int port) throws Exception {
        socket = new Socket();
        socket.connect(new InetSocketAddress(host, port), 1500); // timeout 1.5 c
        out = new ObjectOutputStream(socket.getOutputStream());
        in  = new ObjectInputStream (socket.getInputStream());
    }

    /** отправляем запрос, ждём ответ */
    public Response request(Request req) throws Exception {
        out.writeObject(req);
        out.flush();
        return (Response) in.readObject();
    }

    @Override public void close() throws Exception { socket.close(); }
}
