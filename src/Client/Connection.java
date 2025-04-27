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
    private final InetSocketAddress addr;
    private SocketChannel channel;
    private final ByteBuffer buf = ByteBuffer.allocate(BUF);

    public Connection(String host, int port) throws Exception {
        this.addr = new InetSocketAddress(host, port);
        reconnect();
    }

    private void reconnect() throws Exception {
        if (channel != null && channel.isOpen()) channel.close();
        channel = SocketChannel.open(addr);
        channel.configureBlocking(true);     // клиенту проще блокирующий
    }

    public Response request(Request r) throws Exception {
        try {
            writeLine(r.toFirstLine());
            if (r.workerCsv() != null) writeLine(r.workerCsv());
            writeLine("END");

            List<String> lines = new ArrayList<>();
            while (true) {
                String line = readLine();
                if ("END".equals(line)) break;
                lines.add(line);
            }
            return Response.fromLines(lines);
        } catch (Exception e) {              // сервер мог «упасть» → переподключаемся
            reconnect();
            throw e;
        }
    }

    /* ------------ helpers ------------ */

    private void writeLine(String s) throws Exception {
        byte[] bytes = (s + "\r\n").getBytes(StandardCharsets.UTF_8);
        ByteBuffer out = ByteBuffer.wrap(bytes);
        while (out.hasRemaining()) channel.write(out);
    }

    private String readLine() throws Exception {
        StringBuilder sb = new StringBuilder();
        ByteBuffer one = ByteBuffer.allocate(1);
        while (true) {
            one.clear();
            int r = channel.read(one);
            if (r == -1) throw new java.io.EOFException();
            one.flip();
            char c = (char) one.get();
            if (c == '\n') {
                if (sb.length()>0 && sb.charAt(sb.length()-1)=='\r')
                    sb.setLength(sb.length()-1);
                return sb.toString();
            }
            sb.append(c);
        }
    }

    @Override public void close() throws Exception { channel.close(); }
}

