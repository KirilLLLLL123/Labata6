package Server;
/** ждёт TCP-клиента */

import Command.modelall.commands.CommandHandler;
import Command.modelall.service.WorkerService;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

/** Главный цикл сервера, один поток, неблокирующий NIO. */
public class ConnectionListener {

    private static final int PORT = 5555;
    private static final int BUF  = 64 * 1024;

    private final CollectionManager cm;
    private final CommandDispatcher dispatcher =
            new CommandDispatcher(new CommandHandler(new WorkerService(null), null));
    private final ResponseSender sender = new ResponseSender();

    public ConnectionListener(CollectionManager cm) { this.cm = cm; }

    public void start() throws Exception {
        try (ServerSocketChannel server = ServerSocketChannel.open()) {
            server.bind(new InetSocketAddress(PORT));
            server.configureBlocking(false);

            Selector sel = Selector.open();
            server.register(sel, SelectionKey.OP_ACCEPT);

            ByteBuffer buf = ByteBuffer.allocate(BUF);
            System.out.println("TCP-сервер на порту " + PORT);

            while (true) {
                sel.select();
                Iterator<SelectionKey> it = sel.selectedKeys().iterator();
                while (it.hasNext()) {
                    SelectionKey key = it.next(); it.remove();

                    if (key.isAcceptable()) {
                        SocketChannel ch = server.accept();
                        ch.configureBlocking(false);
                        ch.register(sel, SelectionKey.OP_READ,
                                new RequestReader());
                    } else if (key.isReadable()) {
                        SocketChannel ch = (SocketChannel) key.channel();
                        RequestReader rr = (RequestReader) key.attachment();
                        var req = rr.read(ch, buf);
                        if (req == null) continue;          // не дочитали
                        rr.reset();

                        var resp = dispatcher.dispatch(req);
                        sender.send(ch, resp);
                        ch.close();
                    }
                }
            }
        }
    }
}