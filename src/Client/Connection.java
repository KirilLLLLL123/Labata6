package Client;

import Command.modelall.Request;
import Command.modelall.Response;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Connection implements AutoCloseable {

    private static final int BUF = 64 * 1024;

    private final SocketChannel channel;
    private final ByteBuffer    byteBuf = ByteBuffer.allocate(BUF);

    public Connection(String host, int port) throws Exception {
        channel = SocketChannel.open(new InetSocketAddress(host, port));
        channel.configureBlocking(true);                 // блокирующий режим
    }

    /* ---------------- публичный метод ---------------- */

    public Response request(Request r) throws Exception {
        /* ----- отправка ----- */
        writeLine(r.toFirstLine());
        if (r.workerCsv() != null) writeLine(r.workerCsv());
        writeLine("END");

        /* ----- приём ----- */
        List<String> lines = new ArrayList<>();
        while (true) {
            String ln = readLine();
            if ("END".equals(ln)) break;
            lines.add(ln);
        }
        return Response.fromLines(lines);
    }

    /* ---------------- util ---------------- */

    private void writeLine(String s) throws Exception {
        byte[] bytes = (s + "\r\n").getBytes(StandardCharsets.UTF_8);
        ByteBuffer out = ByteBuffer.wrap(bytes);
        while (out.hasRemaining()) channel.write(out);
    }

    /** корректно читает UTF-8 строку, пока не встретит \n */
    private String readLine() throws Exception {
        ByteBuffer lineBuf = ByteBuffer.allocate(1024);

        while (true) {
            int read = channel.read(byteBuf);
            if (read == -1) throw new java.io.EOFException();

            byteBuf.flip();
            while (byteBuf.hasRemaining()) {
                byte b = byteBuf.get();
                if (b == '\n') {                       // конец строки
                    int len = lineBuf.position();
                    if (len > 0 && lineBuf.get(len - 1) == '\r') len--;
                    byte[] arr = new byte[len];
                    lineBuf.flip();
                    lineBuf.get(arr);
                    byteBuf.compact();                 // готовим buf к следующему чтению
                    return new String(arr, StandardCharsets.UTF_8);
                } else {
                    if (!lineBuf.hasRemaining()) {     // расширяем буфер при необходимости
                        ByteBuffer bigger = ByteBuffer.allocate(lineBuf.capacity() * 2);
                        lineBuf.flip(); bigger.put(lineBuf); lineBuf.clear(); lineBuf.put(bigger);
                        lineBuf = bigger;
                    }
                    lineBuf.put(b);
                }
            }
            byteBuf.clear();
        }
    }

    @Override public void close() throws Exception { channel.close(); }
}
