import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class Server {

    public static void main(String[] args) throws IOException{
        final ServerSocketChannel serverChannel = ServerSocketChannel.open();
        serverChannel.bind(new InetSocketAddress("localhost", 54445));
        while (true) {
            try (SocketChannel socketChannel = serverChannel.accept()){
                final ByteBuffer inputBuffer = ByteBuffer.allocate(2 << 10);
                while (socketChannel.isConnected()) {
                    int bytesCount = socketChannel.read(inputBuffer);
                    if (bytesCount == -1) break;
                    final String msg = new String(inputBuffer.array(), 0, bytesCount, StandardCharsets.UTF_8);
                    inputBuffer.clear();
                    String answerToClient = Arrays.toString(msg.split(" "));
                    socketChannel.write(ByteBuffer.wrap(answerToClient.getBytes(StandardCharsets.UTF_8)));
                }
            }catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
}
