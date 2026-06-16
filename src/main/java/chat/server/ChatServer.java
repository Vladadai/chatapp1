package chat.server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ChatServer {

    public static final int PORT = 5000;

    public static List<ClientHandler> clients = new ArrayList<>();

    public static void main(String[] args) {

        System.out.println("Server started on port " + PORT);

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {

            while (true) {

                Socket socket = serverSocket.accept();

                ClientHandler handler = new ClientHandler(socket);

                clients.add(handler);

                new Thread(handler).start();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void broadcast(String msg) {
        for (ClientHandler c : clients) {
            c.send(msg);
        }
    }

    public static void remove(ClientHandler c) {
        clients.remove(c);
    }
}