package chat.server;

import chat.service.ChatService;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public String username;

    public ClientHandler(Socket socket) {

        try {
            this.socket = socket;

            in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream())
            );

            out = new PrintWriter(socket.getOutputStream(), true);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {

        try {

            out.println("ENTER_USERNAME");
            username = in.readLine();

            ChatServer.broadcast("JOIN|" + username);
            sendUsers();

            String msg;

            while ((msg = in.readLine()) != null) {

                // PRIVATE MESSAGE
                if (msg.startsWith("PRIV|")) {

                    String[] p = msg.split("\\|", 3);

                    String target = p[1];
                    String text = p[2];

                    for (ClientHandler c : ChatServer.clients) {

                        if (c.username.equals(target)) {
                            c.send("PRIV|" + username + "|" + text);
                        }
                    }

                    continue;
                }

                // NORMAL MESSAGE
                ChatService.saveMessage(username, msg);

                ChatServer.broadcast("MSG|" + username + "|" + msg);
            }

        } catch (Exception e) {

            System.out.println(username + " disconnected");

        } finally {

            ChatServer.remove(this);

            ChatServer.broadcast("LEAVE|" + username);

            sendUsers();

            try {
                socket.close();
            } catch (Exception ignored) {}
        }
    }

    private void sendUsers() {

        StringBuilder sb = new StringBuilder();

        for (ClientHandler c : ChatServer.clients) {
            sb.append(c.username).append(",");
        }

        ChatServer.broadcast("USERS|" + sb);
    }

    public void send(String msg) {
        out.println(msg);
    }
}