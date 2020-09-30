package client;

import javax.swing.*;
import java.io.IOException;
import java.util.Objects;

public class EditUserDialog extends JDialog{
    private JPanel panel;
    private JTextField loginTextField;
    private JTextField passwordTextField;
    private JButton backButton;
    private JButton saveButton;
    private JCheckBox roleCheckBox;
    private String idUser = null;

    EditUserDialog(String nameButton) {
        setSize(300, 300);
        setLocationRelativeTo(null);
        // Диалог в модальном режиме - только он активен
        setModal(true);
        setContentPane(panel);
        saveButton.setText(nameButton);

        saveButton.addActionListener(e -> saveActionListener());
        backButton.addActionListener(e -> backActionListener());
    }

    EditUserDialog(User user, String nameButton) {
        setSize(300, 300);
        setLocationRelativeTo(null);
        initFields(user);
        // Диалог в модальном режиме - только он активен
        setModal(true);
        setContentPane(panel);
        saveButton.setText(nameButton);

        saveButton.addActionListener(e -> saveActionListener());
        backButton.addActionListener(e -> backActionListener());
    }

    private void saveActionListener() {
        if(Objects.equals(loginTextField.getText(), "") || Objects.equals(passwordTextField.getText(), "")) {
            JOptionPane.showMessageDialog(null, "Ошибка ввода. Заполнены не все поля или заполнены не верно.");
            return;
        }

        if(Objects.equals(saveButton.getText(), "Добавить")) {
            try {
                ClientTCP.clientObjectOutputStream.writeObject("19"); // отправка на сервер
                ClientTCP.clientObjectOutputStream.writeObject(new String[] {
                        loginTextField.getText(),
                        passwordTextField.getText(),
                        roleCheckBox.isSelected() ? "1" : "0"});

                String success_fail = (String) ClientTCP.clientObjectInputStream.readObject();
                if(Objects.equals(success_fail, "success")) {
                    JOptionPane.showMessageDialog(null, "Новый пользователь добавлен.");
                    loginTextField.setText("");
                    passwordTextField.setText("");
                    roleCheckBox.setSelected(false);
                }
                else
                    JOptionPane.showMessageDialog(null, "Ошибка добавления пользователя. Данные введены не корректно.");
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        else {
            try {
                ClientTCP.clientObjectOutputStream.writeObject("20"); // отправка на сервер
                ClientTCP.clientObjectOutputStream.writeObject(new String[]{
                        idUser,
                        loginTextField.getText(),
                        passwordTextField.getText(),
                        roleCheckBox.isSelected() ? "1" : "0"});
                String success_fail = (String) ClientTCP.clientObjectInputStream.readObject();
                if (Objects.equals(success_fail, "success"))
                    JOptionPane.showMessageDialog(null, "Данные о пользователе успешно изменены.");
                else
                    JOptionPane.showMessageDialog(null, "Ошибка редактирования пользователя. Данные введены не корректно.");
                dispose();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void backActionListener() {
        dispose();
    }

    private void initFields(User user) {
        idUser = String.valueOf(user.getId_user());
        loginTextField.setText(user.getLogin());
        passwordTextField.setText(user.getPassword());
        roleCheckBox.setSelected(user.isRole());
    }
}
