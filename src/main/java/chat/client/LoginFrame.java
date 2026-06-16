package chat.client;

import chat.database.UserDAO;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginFrame() {

        setTitle("Login");
        setSize(350, 250);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(4, 1));

        usernameField = new JTextField();
        passwordField = new JPasswordField();

        JButton loginBtn = new JButton("Login");
        JButton registerBtn = new JButton("Register");

        add(new JLabel("Username"));
        add(usernameField);

        add(new JLabel("Password"));
        add(passwordField);

        JPanel panel = new JPanel();
        panel.add(loginBtn);
        panel.add(registerBtn);

        add(panel);

        loginBtn.addActionListener(e -> login());
        registerBtn.addActionListener(e -> openRegister());

        setVisible(true);
    }

    private void login() {

        String user = usernameField.getText();
        String pass = new String(passwordField.getPassword());

        if (UserDAO.login(user, pass)) {

            dispose();
            new ChatFrame(user);

        } else {
            JOptionPane.showMessageDialog(this, "Ошибка входа");
        }
    }

    private void openRegister() {
        new RegisterFrame();
    }
}