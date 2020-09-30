package client;

import javax.swing.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;

public class WorkWithTheListOfStudent extends JFrame {
    private JPanel panel;
    private JTable viewStudentTable;
    private JButton reportButton;
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private JCheckBox scientificWorkCheckBox;
    private JCheckBox culturalActivitiesCheckBox;
    private JCheckBox socialHelpCheckBox;
    private JCheckBox BRYUCheckBox;
    private JCheckBox tradeUnionCommitteeCheckBox;
    private JCheckBox finesCheckBox;
    private JButton backButton;
    private JButton filterButton;
    private JCheckBox dormitoryCheckBox;
    private JTextField FIOTextField;
    private JTextField tillTextField;
    private JTextField fromTextField;
    private JButton refreshButton;
    private JCheckBox checkBox1;
    private JCheckBox checkBox2;
    private JCheckBox checkBox3;
    private JCheckBox checkBox4;
    private JCheckBox checkBox5;
    private JCheckBox checkBox6;
    private JCheckBox checkBox7;
    private JCheckBox scholarshipCheckBox;
    private JCheckBox checkBox8;
    private String admin_user;
    private ViewStudentTableModel viewStudentTableModel;

    WorkWithTheListOfStudent(String admin_user) {
        setSize(1366, 730);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setContentPane(panel);
        viewStudentTable.setAutoCreateRowSorter(true);
        this.admin_user = admin_user;

        if(Objects.equals(admin_user, "user")) {
            addButton.setEnabled(false);
            editButton.setEnabled(false);
            deleteButton.setEnabled(false);
        }

        viewStudentTable.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);

        reload();

        refreshButton.addActionListener(e -> refreshActionListener());
        addButton.addActionListener(e -> addActionListener());
        editButton.addActionListener(e -> editActionListener());
        deleteButton.addActionListener(e -> deleteActionListener());
        reportButton.addActionListener(e -> reportActionListener());
        filterButton.addActionListener(e -> filterActionListener());
        backButton.addActionListener(e -> backActionListener());
    }

    private void refreshActionListener() {
        try {
            ClientTCP.clientObjectOutputStream.writeObject("23"); // отправка на сервер
            reload();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addActionListener() {
        // Создаем диалог для ввода данных
        EditStudentDialog editStudentDialog = new client.EditStudentDialog("Добавить");
        editStudentDialog.setVisible(true);
        refreshActionListener();
    }

    private void editActionListener() {
        // Получаем выделенную строку
        int selectedRow = viewStudentTable.getSelectedRow();
        // если строка выделена - можно ее редактировать
        if (selectedRow != -1) {
            // Получаем ID студента
            Long id = Long.parseLong(viewStudentTable.getModel().getValueAt(selectedRow, 0).toString());
            // Создаем диалог для ввода данных и передаем туда студента
            EditStudentDialog editStudentDialog = new EditStudentDialog(viewStudentTableModel.getStudent(id), "Редактировать");
            editStudentDialog.setVisible(true);
            refreshActionListener();
        } else {
            // Если строка не выделена - сообщаем об этом
            JOptionPane.showMessageDialog(this, "Вы должны выделить строку для редактирования");
        }
    }

    private void deleteActionListener() {
        // Получаем выделеннуб строку
        int selectedRow = viewStudentTable.getSelectedRow();
        if (selectedRow != -1) {
            // Получаем ID контакта
            Long id_student = Long.parseLong(viewStudentTable.getModel().getValueAt(selectedRow, 0).toString());
            // Удаляем контакт
            try {
                ClientTCP.clientObjectOutputStream.writeObject("12");
                ClientTCP.clientObjectOutputStream.writeObject(id_student);
                String success_fail = (String) ClientTCP.clientObjectInputStream.readObject();
                if (Objects.equals(success_fail, "success"))
                    JOptionPane.showMessageDialog(null, "Студент успешно удалён.");
                else
                    JOptionPane.showMessageDialog(null, "Ошибка удаления студента.");

            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            // перегружаем список студентов
            refreshActionListener();
        } else {
            JOptionPane.showMessageDialog(this, "Вы должны выделить строку для удаления");
        }
    }

    private void reportActionListener() {
        try {
            ClientTCP.clientObjectOutputStream.writeObject("13"); // отправка на сервер
            FileWriter file = new FileWriter("Отчёт. Список данных о студентах.txt", false);
            Student student;
            for(int i = 0; i < viewStudentTableModel.students.size(); i++) {
                student = viewStudentTableModel.students.get(i);
                file.write(student.getId_student() + ". " + student.getFio() +
                        "\r\n\tСредний балл: " + student.getAverage_score() +
                        "\r\n\tНаучная работа: " + (student.isScientific_work() ? "да" : "нет") +
                        "\r\n\tКультурная деятельность: " + (student.isCultural_activities() ? "да" : "нет") +
                        "\r\n\tСоциальная помощь: " + student.getSocial_help() +
                        "\r\n\tПрофком: " + (student.isPayment_for_trade_union_committee() ? "да" : "нет") +
                        "\r\n\tБРСМ: " + (student.isPayment_for_the_BRYU() ? "да" : "нет") +
                        "\r\n\tОбщежитие: " + student.getPayment_for_a_hostel() +
                        "\r\n\tШтрафы: " + student.getAmount_of_fines() +
                        "\r\n\tСтипендия: " + (student.isScholarship() ? "да" : "нет") +
                        "\r\n\tРазмер стипендии: " + student.getValueScholarship() + "\r\n\r\n");
            }
            file.close();
            JOptionPane.showMessageDialog(this, "Отчёт успешно сформирован в файле \"Отчёт. Список данных о студентах.txt\".");
        }
        catch(IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void filterActionListener() {
        boolean filter2[] = new boolean[8];
        filter2[0] = checkBox1.isSelected();
        filter2[1] = checkBox2.isSelected();
        filter2[2] = checkBox3.isSelected();
        filter2[3] = checkBox4.isSelected();
        filter2[4] = checkBox5.isSelected();
        filter2[5] = checkBox6.isSelected();
        filter2[6] = checkBox7.isSelected();
        filter2[7] = checkBox8.isSelected();

        String FIO = FIOTextField.getText();
        String from = fromTextField.getText();
        String till = tillTextField.getText();
        boolean filter[] = new boolean[8];
        filter[0] = scientificWorkCheckBox.isSelected();
        filter[1] = culturalActivitiesCheckBox.isSelected();
        filter[2] = socialHelpCheckBox.isSelected();
        filter[3] = tradeUnionCommitteeCheckBox.isSelected();
        filter[4] = BRYUCheckBox.isSelected();
        filter[5] = dormitoryCheckBox.isSelected();
        filter[6] = finesCheckBox.isSelected();
        filter[7] = scholarshipCheckBox.isSelected();
        double fromAverageScore = 0, tillAverageScore = 10;
        try {
            if(!Objects.equals(from, ""))
                fromAverageScore = Double.parseDouble(from);
            if(!Objects.equals(till, ""))
                tillAverageScore = Double.parseDouble(till);
        } catch (Exception ignored) {
            JOptionPane.showMessageDialog(this, "Поля выборки по среднему баллу содержат некорректные данные");
            return;
        }
        if(fromAverageScore < 0 || fromAverageScore > 10 || tillAverageScore < 0 || tillAverageScore > 10) {
            JOptionPane.showMessageDialog(this, "Поля выборки по среднему баллу содержат недопустимые значения");
            return;
        }

        try {
            ClientTCP.clientObjectOutputStream.writeObject("14"); // отправка на сервер
            String students = (String) ClientTCP.clientObjectInputStream.readObject(); // принимаем сообщение от сервера
            // Создаем модель, которой передаем полученный список
            viewStudentTableModel = new ViewStudentTableModel(students);
            // Корректируем модель, используя фильтр
            viewStudentTableModel.deleteFilter(filter, filter2, FIO, fromAverageScore, tillAverageScore);
            // Передаем нашу модель таблице - и она может ее отображать
            viewStudentTable.setModel(viewStudentTableModel);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void backActionListener() {
        try {
            ClientTCP.clientObjectOutputStream.writeObject("16"); // отправка на сервер
            if(Objects.equals(admin_user, "admin")) {
                AdminActions adminActions = new AdminActions();
                adminActions.setVisible(true);
            }
            else {
                LoginToTheSystem loginToTheSystem = new LoginToTheSystem();
                loginToTheSystem.setVisible(true);
            }
            this.dispose();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    private void reload() {
        try {
            String students = (String) ClientTCP.clientObjectInputStream.readObject(); // принимаем сообщение от сервера
            // Создаем модель, которой передаем полученный список
            viewStudentTableModel = new ViewStudentTableModel(students);
            // Передаем нашу модель таблице - и она может ее отображать
            viewStudentTable.setModel(viewStudentTableModel);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
