package client;

import javax.swing.*;
import java.io.IOException;
import java.util.Objects;

public class EditStudentDialog extends JDialog {
    private JPanel panel;
    private JTextField FIOTextField;
    private JTextField averageScoreTextField;
    private JTextField socialHelpTextField;
    private JTextField paymentForAHostelTextField;
    private JTextField amountOfFinesTextField;
    private JButton saveButton;
    private JButton backButton;
    private JCheckBox scientificWorkCheckBox;
    private JCheckBox culturalActivitiesCheckBox;
    private JCheckBox paymentForTradeUnionCommitteeCheckBox;
    private JCheckBox paymentForTheBRYUCheckBox;
    private JCheckBox scholarshipCheckBox;
    private String idStudent = null;

    EditStudentDialog(String nameButton) {
        setSize(400, 500);
        setLocationRelativeTo(null);
        // Диалог в модальном режиме - только он активен
        setModal(true);
        setContentPane(panel);
        saveButton.setText(nameButton);

        saveButton.addActionListener(e -> saveActionListener());
        backButton.addActionListener(e -> backActionListener());
    }

    EditStudentDialog(Student student, String nameButton) {
        setSize(500, 500);
        initFields(student);
        // Диалог в модальном режиме - только он активен
        setModal(true);
        setContentPane(panel);
        saveButton.setText(nameButton);

        saveButton.addActionListener(e -> saveActionListener());
        backButton.addActionListener(e -> backActionListener());
    }

    private void saveActionListener() {
        try {
            if (Objects.equals(FIOTextField.getText(), "") ||
                    Objects.equals(averageScoreTextField.getText(), "") ||
                    Double.parseDouble(averageScoreTextField.getText()) < 0 ||
                    Double.parseDouble(averageScoreTextField.getText()) > 10 ||
                    Objects.equals(socialHelpTextField.getText(), "") ||
                    Double.parseDouble(socialHelpTextField.getText()) < 0 ||
                    Objects.equals(paymentForAHostelTextField.getText(), "") ||
                    Double.parseDouble(paymentForAHostelTextField.getText()) < 0 ||
                    Objects.equals(amountOfFinesTextField.getText(), "") ||
                    Double.parseDouble(amountOfFinesTextField.getText()) < 0) {
                JOptionPane.showMessageDialog(null, "Ошибка ввода. Заполнены не все поля или заполнены не верно.");
                return;
            }
        } catch (Exception e){
            JOptionPane.showMessageDialog(null, "Ошибка ввода. Заполнены не все поля или заполнены не верно.");
            return;
        }

        if(Objects.equals(saveButton.getText(), "Добавить")) {
            try {
                ClientTCP.clientObjectOutputStream.writeObject("10");
                ClientTCP.clientObjectOutputStream.writeObject(new String[]{
                        FIOTextField.getText(),
                        averageScoreTextField.getText(),
                        scholarshipCheckBox.isSelected() ? "1" : "0",
                        scientificWorkCheckBox.isSelected() ? "1" : "0",
                        culturalActivitiesCheckBox.isSelected() ? "1" : "0",
                        socialHelpTextField.getText(),
                        paymentForTradeUnionCommitteeCheckBox.isSelected() ? "1" : "0",
                        paymentForTheBRYUCheckBox.isSelected() ? "1" : "0",
                        paymentForAHostelTextField.getText(),
                        amountOfFinesTextField.getText()});

                String success_fail = (String) ClientTCP.clientObjectInputStream.readObject();
                if (Objects.equals(success_fail, "success")) {
                    JOptionPane.showMessageDialog(null, "Новый студент добавлен.");
                    FIOTextField.setText("");
                    averageScoreTextField.setText("");
                    scholarshipCheckBox.setSelected(false);
                    scientificWorkCheckBox.setSelected(false);
                    culturalActivitiesCheckBox.setSelected(false);
                    socialHelpTextField.setText("");
                    paymentForTradeUnionCommitteeCheckBox.setSelected(false);
                    paymentForTheBRYUCheckBox.setSelected(false);
                    paymentForAHostelTextField.setText("");
                    amountOfFinesTextField.setText("");
                }
                else
                    JOptionPane.showMessageDialog(null, "Ошибка добавления студента. Данные введены не корректно.");
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        else {
            try {
                ClientTCP.clientObjectOutputStream.writeObject("11"); // отправка на сервер
                ClientTCP.clientObjectOutputStream.writeObject(new String[]{
                        idStudent,
                        FIOTextField.getText(),
                        averageScoreTextField.getText(),
                        scholarshipCheckBox.isSelected() ? "1" : "0",
                        scientificWorkCheckBox.isSelected() ? "1" : "0",
                        culturalActivitiesCheckBox.isSelected() ? "1" : "0",
                        socialHelpTextField.getText(),
                        paymentForTradeUnionCommitteeCheckBox.isSelected() ? "1" : "0",
                        paymentForTheBRYUCheckBox.isSelected() ? "1" : "0",
                        paymentForAHostelTextField.getText(),
                        amountOfFinesTextField.getText()});
                String success_fail = (String) ClientTCP.clientObjectInputStream.readObject();
                if (Objects.equals(success_fail, "success"))
                    JOptionPane.showMessageDialog(null, "Данные о студенте изменены.");
                else
                    JOptionPane.showMessageDialog(null, "Ошибка редактирования студента. Данные введены не корректно.");
                dispose();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void backActionListener() {
        dispose();
    }

    private void initFields(Student student) {
        idStudent = String.valueOf(student.getId_student());
        FIOTextField.setText(student.getFio());
        averageScoreTextField.setText(String.valueOf(student.getAverage_score()));
        scientificWorkCheckBox.setSelected(student.isScientific_work());
        culturalActivitiesCheckBox.setSelected(student.isCultural_activities());
        socialHelpTextField.setText(String.valueOf(student.getSocial_help()));
        paymentForTradeUnionCommitteeCheckBox.setSelected(student.isPayment_for_trade_union_committee());
        paymentForTheBRYUCheckBox.setSelected(student.isPayment_for_the_BRYU());
        paymentForAHostelTextField.setText(String.valueOf(student.getPayment_for_a_hostel()));
        amountOfFinesTextField.setText(String.valueOf(student.getAmount_of_fines()));
        scholarshipCheckBox.setSelected(student.isScholarship());
    }
}
