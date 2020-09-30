package client;

import javax.swing.*;
import java.io.IOException;
import java.util.Objects;

public class WorkWithTheUserBase extends JFrame {
    private JPanel panel;
    private JTable viewUserTable;
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private JButton backButton;
    private JButton refreshButton;
    private ViewUserTableModel viewUserTableModel;

    WorkWithTheUserBase() {
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setContentPane(panel);
        viewUserTable.setAutoCreateRowSorter(true);

        viewUserTable.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        reload();

        refreshButton.addActionListener(e -> refreshActionListener());
        addButton.addActionListener(e -> addActionListener());
        editButton.addActionListener(e -> editActionListener());
        deleteButton.addActionListener(e -> deleteActionListener());
        backButton.addActionListener(e -> backActionListener());
    }

    private void refreshActionListener() {
        try {
            ClientTCP.clientObjectOutputStream.writeObject("24"); // отправка на сервер
            reload();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addActionListener() {
        // Создаем диалог для ввода данных
        EditUserDialog editUserDialog = new client.EditUserDialog("Добавить");
        editUserDialog.setVisible(true);
        refreshActionListener();
    }

    private void editActionListener() {
        // Получаем выделеннуб строку
        int selectedRow = viewUserTable.getSelectedRow();
        // если строка выделена - можно ее редактировать
        if (selectedRow != -1) {
            // Получаем ID пользователя
            Long id = Long.parseLong(viewUserTable.getModel().getValueAt(selectedRow, 0).toString());
            // Создаем диалог для ввода данных и передаем туда пользователя
            EditUserDialog editUserDialog = new EditUserDialog(viewUserTableModel.getUser(id), "Редактировать");
            editUserDialog.setVisible(true);
            refreshActionListener();
        } else {
            // Если строка не выделена - сообщаем об этом
            JOptionPane.showMessageDialog(this, "Вы должны выделить строку для редактирования");
        }
    }

    private void deleteActionListener() {
        // Получаем выделеннуб строку
        int selectedRow = viewUserTable.getSelectedRow();
        if (selectedRow != -1) {
            // Получаем ID пользователя
            Long id_user = Long.parseLong(viewUserTable.getModel().getValueAt(selectedRow, 0).toString());
            // Удаляем пользователя
            try {
                ClientTCP.clientObjectOutputStream.writeObject("21");
                ClientTCP.clientObjectOutputStream.writeObject(id_user);
                String success_fail = (String) ClientTCP.clientObjectInputStream.readObject();
                if (Objects.equals(success_fail, "success"))
                    JOptionPane.showMessageDialog(null, "Пользователь успешно удалён.");
                else
                    JOptionPane.showMessageDialog(null, "Ошибка удаления пользователя.");
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            // перегружаем список студентов
            refreshActionListener();
        } else {
            JOptionPane.showMessageDialog(this, "Вы должны выделить строку для удаления");
        }
    }

    private void backActionListener() {
        try {
            ClientTCP.clientObjectOutputStream.writeObject("22"); // отправка на сервер
            AdminActions adminActions = new AdminActions();
            adminActions.setVisible(true);
            this.dispose();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    private void reload() {
        try {
            String users = (String) ClientTCP.clientObjectInputStream.readObject(); // принимаем сообщение от сервера
            // Создаем модель, которой передаем полученный список
            viewUserTableModel = new ViewUserTableModel(users);
            // Передаем нашу модель таблице - и она может ее отображать
            viewUserTable.setModel(viewUserTableModel);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
