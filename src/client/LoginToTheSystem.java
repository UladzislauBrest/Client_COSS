package client;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Objects;

public class LoginToTheSystem extends JFrame {
    private JTextField loginTextField;
    private JTextField passwordTextField;
    private JButton entryButton;
    private JButton registrationButton;
    private JButton exitButton;
    private JPanel panel;
    private JTextField portTextField;
    private JButton connectButton;
    private JLabel loginLabel;
    private JLabel passwordLabel;
    private JLabel portLabel;
    private static boolean inSystem = false;

    LoginToTheSystem() throws IOException {
        setSize(500, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setContentPane(panel);

        loginTextField.setEnabled(false);
        passwordTextField.setEnabled(false);
        loginLabel.setEnabled(false);
        passwordLabel.setEnabled(false);
        entryButton.setEnabled(false);
        registrationButton.setEnabled(false);

        portTextField.setText(String.valueOf(4000));

        entryButton.addActionListener(e -> entryActionListener());
        registrationButton.addActionListener(e -> registrationActionListener());
        exitButton.addActionListener(e -> exitActionListener());
        connectButton.addActionListener(e -> connectionActionListener());
    }

    private void connectionActionListener() {
        try {
            int port = Integer.parseInt(portTextField.getText());
            if (port >= 0 && port <= 6000)
                connection(port);
            else
                JOptionPane.showMessageDialog(this, "Порт введён не корректно. Введите число от 1 до 6000.");
        } catch (Exception ignored) {
            JOptionPane.showMessageDialog(this, "Порт введён не корректно. Введите число от 1 до 6000.");
        }
    }

    private void connection(int port) {
        try {
            Socket clientSocket = new Socket("127.0.0.1", port);
            ClientTCP.clientObjectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            ClientTCP.clientObjectInputStream = new ObjectInputStream(clientSocket.getInputStream());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Сервер на заданном порту не найдет. Попробуйте ещё раз.");
            return;
        }

        inSystem = true;

        loginTextField.setEnabled(true);
        passwordTextField.setEnabled(true);
        loginLabel.setEnabled(true);
        passwordLabel.setEnabled(true);
        entryButton.setEnabled(true);
        registrationButton.setEnabled(true);

        connectButton.setEnabled(false);
        portTextField.setEnabled(false);
        portLabel.setEnabled(false);

        //loginTextField.setText("admin");
        //passwordTextField.setText("admin");
    }

    private void entryActionListener() {
        try {
            String login = loginTextField.getText(), password = passwordTextField.getText();
            if(Objects.equals(login, "") || Objects.equals(password, "")) {
                JOptionPane.showMessageDialog(null, "Неполные данные!");
                return;
            }

            ClientTCP.clientObjectOutputStream.writeObject("2"); // отправка на сервер
            ClientTCP.clientObjectOutputStream.writeObject(login); // отправка на сервер
            ClientTCP.clientObjectOutputStream.writeObject(password); // отправка на сервер

            String admin_user_nobody = (String) ClientTCP.clientObjectInputStream.readObject(); // принимаем сообщение от сервера
            if(Objects.equals(admin_user_nobody, "nobody"))
                JOptionPane.showMessageDialog(null, "Неправильно введён(ы) логин и/или пароль!");
            else if(Objects.equals(admin_user_nobody, "user")) {
                WorkWithTheListOfStudent workWithTheListOfStudent = new WorkWithTheListOfStudent("user");
                workWithTheListOfStudent.setVisible(true);
                this.dispose();
            }
            else {
                AdminActions adminActions = new AdminActions();
                adminActions.setVisible(true);
                this.dispose();
            }
        } catch (IOException | ClassNotFoundException e1) {
            e1.printStackTrace();
        }
    }

    private void registrationActionListener() {
        try {
            ClientTCP.clientObjectOutputStream.writeObject("1"); // отправка на сервер
            Registration registration = new Registration();
            registration.setVisible(true);
            this.dispose();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    private void exitActionListener() {
        try {
            if(inSystem)
                ClientTCP.clientObjectOutputStream.writeObject("3"); // отправка на сервер
            this.dispose();
            System.exit(0);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
}
