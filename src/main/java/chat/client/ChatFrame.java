package chat.client;

import javax.swing.*;
import java.awt.*;

public class ChatFrame extends JFrame {

    private JTextField input;
    private JTextArea chatArea;
    private DefaultListModel<String> usersModel;
    private JList<String> usersList;

    private ClientConnection connection;
    private String username;

    public ChatFrame(String username) {

        this.username = username;

        setTitle("Chat - " + username);
        setSize(750, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        // CHAT AREA
        chatArea = new JTextArea();
        chatArea.setEditable(false);

        add(new JScrollPane(chatArea), BorderLayout.CENTER);

        // USERS LIST
        usersModel = new DefaultListModel<>();
        usersList = new JList<>(usersModel);

        add(new JScrollPane(usersList), BorderLayout.EAST);

        // INPUT PANEL
        JPanel bottom = new JPanel(new BorderLayout());

        input = new JTextField();
        JButton send = new JButton("Send");

        bottom.add(input, BorderLayout.CENTER);
        bottom.add(send, BorderLayout.EAST);

        add(bottom, BorderLayout.SOUTH);

        connect();

        send.addActionListener(e -> sendMessage());
        input.addActionListener(e -> sendMessage());

        setVisible(true);
    }

    private void connect() {

        try {

            connection = new ClientConnection("localhost", 5000);

            connection.setListener(this::handleMessage);

            Thread.sleep(200);

            connection.send(username);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleMessage(String msg) {

        SwingUtilities.invokeLater(() -> {

            if (msg.startsWith("MSG|")) {

                String[] p = msg.split("\\|", 3);
                chatArea.append(p[1] + ": " + p[2] + "\n");

            } else if (msg.startsWith("JOIN|")) {

                chatArea.append("[JOIN] " + msg.split("\\|")[1] + "\n");

            } else if (msg.startsWith("LEAVE|")) {

                chatArea.append("[LEAVE] " + msg.split("\\|")[1] + "\n");

            } else if (msg.startsWith("PRIV|")) {

                String[] p = msg.split("\\|", 3);
                chatArea.append("[PRIVATE] " + p[1] + ": " + p[2] + "\n");

            } else if (msg.startsWith("USERS|")) {

                updateUsers(msg.substring(6));
            }
        });
    }

    private void updateUsers(String data) {

        usersModel.clear();

        String[] users = data.split(",");

        for (String u : users) {
            if (!u.isEmpty()) {
                usersModel.addElement(u);
            }
        }
    }

    private void sendMessage() {

        String text = input.getText();

        if (text.isEmpty()) return;

        if (text.startsWith("/msg ")) {

            String[] p = text.split(" ", 3);

            connection.send("PRIV|" + p[1] + "|" + p[2]);

        } else {

            connection.send(text);
        }

        input.setText("");
    }
}