package Server;
/** шлёт Response */

import Command.modelall.Response;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public final class ResponseSender {

    public void send(SocketChannel ch, Response resp) throws Exception {
        for (String s : resp.toLines()) writeLine(ch, s);
        writeLine(ch, "END");
    }

    private void writeLine(SocketChannel ch, String s) throws Exception {
        byte[] bytes = (s + "\r\n").getBytes(StandardCharsets.UTF_8);
        ByteBuffer out = ByteBuffer.wrap(bytes);
        while (out.hasRemaining()) ch.write(out);
    }
}
