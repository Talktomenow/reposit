import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * Created by Will on 2016/2/17.
 */
public class Web {

    public static void main(String[] args) throws Exception

    {

    ServerSocket listener = new ServerSocket(8080);
    try

    {
        while (true) {
            Socket socket = listener.accept();
            try {
                handleRequest(socket);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    finally

    {
        listener.close();
    }
}

    final static String response =
    "HTTP/1.0 200 OK\r\n" +
            "Content-type: text/plain\r\n" +
            "\r\n" +
            "Hello World\r\n";

    public static void handleRequest(Socket socket) throws IOException {
        // Read the input stream, and return “200 OK”
        try {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            System.out.println(in.readLine());

            OutputStream out = socket.getOutputStream();
            out.write(response.getBytes(StandardCharsets.UTF_8));
        } finally {
            socket.close();
        }
    }
}
