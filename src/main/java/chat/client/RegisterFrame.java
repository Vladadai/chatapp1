package chat.client;

import chat.database.UserDAO;

import javax.swing.*;
import java.awt.*;

public class RegisterFrame extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;

    public RegisterFrame() {

        setTitle("Register");
        setSize(350, 250);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(3, 2));

        usernameField = new JTextField();
        passwordField = new JPasswordField();

        JButton createBtn = new JButton("Create");

        add(new JLabel("Username"));
        add(usernameField);

        add(new JLabel("Password"));
        add(passwordField);

        add(new JLabel());
        add(createBtn);

        createBtn.addActionListener(e -> register());

        setVisible(true);
    }

    private void register() {

        String user = usernameField.getText();
        String pass = new String(passwordField.getPassword());

        if (UserDAO.register(user, pass)) {

            JOptionPane.showMessageDialog(this, "Аккаунт создан");
            dispose();

        } else {
            JOptionPane.showMessageDialog(this, "Ошибка регистрации");
        }
    }
}