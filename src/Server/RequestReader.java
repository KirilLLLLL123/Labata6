package Server;
/** считывает Request */

import Command.modelall.Request;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/** Считывает строки до END и строит Request */
public final class RequestReader {

    private final StringBuilder sb = new StringBuilder();
    private final List<String>  lines = new ArrayList<>();

    public Request read(SocketChannel ch, ByteBuffer buf) throws Exception {
        buf.clear();
        int r = ch.read(buf);
        if (r == -1) return null;               // клиент разорвал соединение
        buf.flip();
        while (buf.hasRemaining()) {
            char c = (char) buf.get();
            if (c == '\n') {
                String line = sb.toString().replaceFirst("\\r?$", "");
                sb.setLength(0);
                if ("END".equals(line)) {
                    return Request.fromLines(lines);
                }
                lines.add(line);
            } else sb.append(c);
        }
        return null;    // ещё не всё получили
    }

    public void reset() {
        sb.setLength(0);
        lines.clear();
    }
}

