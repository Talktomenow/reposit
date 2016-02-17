import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Will on 2016/2/17.
 */
public class Web {

    public static void main(String[] args) throws Exception

    {

    ServerSocket listener = new ServerSocket(8080);

    //for thread pool
    ExecutorService executor = Executors.newFixedThreadPool(4);

    try

    {
        while (true) {
            Socket socket = listener.accept();
            //(1) single thread
            /*try {
                handleRequest(socket);
            } catch (IOException e) {
                e.printStackTrace();
            }
            */
            //(2) multiple=thread, not resource-efficient
            //new Thread(new HandleRequestRunnable(socket)).start();
            //(3) thread pool
            executor.submit( new HandleRequestRunnable(socket) );
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
            System.out.println(Thread.currentThread().getName()+":"+in.readLine());

            OutputStream out = socket.getOutputStream();
            out.write(response.getBytes(StandardCharsets.UTF_8));
        } finally {
            socket.close();
        }
    }
}


class HandleRequestRunnable implements Runnable {

    final Socket socket;

    public HandleRequestRunnable(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            Web.handleRequest(socket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

