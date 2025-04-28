package Server;

import Command.modelall.Request;
import Command.modelall.Response;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.List;

/** Один поток, неблокирующий NIO. */
public class ConnectionListener {

    private static final int PORT = 5555;
    private static final int BUF  = 64 * 1024;

    private final CommandDispatcher dispatcher = new CommandDispatcher();
    private final ResponseSender    sender     = new ResponseSender();

    public void start() throws Exception {
        try (ServerSocketChannel server = ServerSocketChannel.open()) {
            server.bind(new InetSocketAddress(PORT));
            server.configureBlocking(false);

            Selector sel = Selector.open();
            server.register(sel, SelectionKey.OP_ACCEPT);

            ByteBuffer buf = ByteBuffer.allocate(BUF);
            System.out.println("TCP-сервер слушает порт " + PORT);

            while (true) {
                sel.select();
                Iterator<SelectionKey> it = sel.selectedKeys().iterator();

                while (it.hasNext()) {
                    SelectionKey key = it.next(); it.remove();

                    if (key.isAcceptable()) {                    // подключение
                        SocketChannel ch = server.accept();
                        ch.configureBlocking(false);
                        ch.register(sel, SelectionKey.OP_READ,
                                new RequestReader());
                    }

                    else if (key.isReadable()) {                 // запрос
                        SocketChannel ch  = (SocketChannel) key.channel();
                        RequestReader rr  = (RequestReader)  key.attachment();

                        try {
                            Request req = rr.read(ch, buf);      // может вернуть null
                            if (req == null) continue;

                            rr.reset();
                            Response resp = dispatcher.dispatch(req);
                            sender.send(ch, resp);               // OK-ответ
                        } catch (Exception ex) {
                            // Любая ошибка -> ERROR-ответ
                            sender.send(ch, new Response(false,
                                    ex.getMessage(), List.of()));
                        } finally {
                            ch.close();                          // «1 команда = 1 сокет»
                        }
                    }
                }
            }
        }
    }
}
