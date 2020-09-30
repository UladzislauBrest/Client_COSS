package client;

import javax.swing.*;
import java.io.IOException;
import java.util.Objects;

public class Registration extends JFrame {
    private JTextField loginTextField;
    private JTextField passwordTextField;
    private JButton registerButton;
    private JButton backButton;
    private JPanel panel;

    Registration() {
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setContentPane(panel);

        registerButton.addActionListener(e -> registerActionListener());
        backButton.addActionListener(e -> backActionListener());
    }

    private void registerActionListener() {
        try {
            String login = loginTextField.getText(), password = passwordTextField.getText();
            if(Objects.equals(login, "") || Objects.equals(password, "")) {
                JOptionPane.showMessageDialog(null, "Неполные данные!");
                return;
            }

            ClientTCP.clientObjectOutputStream.writeObject("4"); // отправка на сервер
            ClientTCP.clientObjectOutputStream.writeObject(login); // отправка на сервер
            ClientTCP.clientObjectOutputStream.writeObject(password); // отправка на сервер

            String success_fail = (String) ClientTCP.clientObjectInputStream.readObject(); // принимаем сообщение от сервера
            if(Objects.equals(success_fail, "success")) {
                JOptionPane.showMessageDialog(null, "Регистрация прошла успешно.");
                backActionListener();
            }
            else
                JOptionPane.showMessageDialog(null, "Ошибка регистрации. Возможно введённый логин занят");
        } catch (IOException | ClassNotFoundException e1) {
            e1.printStackTrace();
        }
    }

    private void backActionListener() {
        try {
            ClientTCP.clientObjectOutputStream.writeObject("5"); // отправка на сервер
            LoginToTheSystem loginToTheSystem = new LoginToTheSystem();
            loginToTheSystem.setVisible(true);
            this.dispose();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
}
