package chat.client;

import java.io.*;
import java.net.Socket;

public class ClientConnection {

    private BufferedReader in;
    private PrintWriter out;

    private MessageListener listener;

    public interface MessageListener {
        void onMessage(String msg);
    }

    public ClientConnection(String host, int port) throws Exception {

        Socket socket = new Socket(host, port);

        in = new BufferedReader(
                new InputStreamReader(socket.getInputStream())
        );

        out = new PrintWriter(socket.getOutputStream(), true);

        new Thread(() -> {

            try {

                String msg;

                while ((msg = in.readLine()) != null) {
                    if (listener != null) {
                        listener.onMessage(msg);
                    }
                }

            } catch (Exception e) {
                System.out.println("Connection lost");
            }

        }).start();
    }

    public void send(String msg) {
        out.println(msg);
    }

    public void setListener(MessageListener listener) {
        this.listener = listener;
    }
}