package client;

import javax.swing.*;
import java.io.IOException;
import java.util.Objects;

public class AdjustmentOfTheCalculationOfScholarships extends JFrame {
    private JPanel panel;
    private JButton saveButton;
    private JButton backButton;
    private JTextField basicScholarshipTextField;
    private JTextField fellowshipMultiplier3TextField;
    private JTextField paymentForTradeUnionCommitteeTextField;
    private JTextField paymentForTheBRYUTextField;
    private JTextField fellowshipMultiplier1TextField;
    private JTextField fellowshipMultiplier2TextField;
    private JTextField fellowshipMultiplier4TextField;
    private JTextField multiplierForScientificWorkTextField;
    private JTextField multiplierForCulturalActivitiesTextField;

    AdjustmentOfTheCalculationOfScholarships() {
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setContentPane(panel);

        try {
            String settings[] = (String[]) ClientTCP.clientObjectInputStream.readObject();
            basicScholarshipTextField.setText(settings[0]);
            fellowshipMultiplier1TextField.setText(settings[1]);
            fellowshipMultiplier2TextField.setText(settings[2]);
            fellowshipMultiplier3TextField.setText(settings[3]);
            fellowshipMultiplier4TextField.setText(settings[4]);
            multiplierForScientificWorkTextField.setText(settings[5]);
            multiplierForCulturalActivitiesTextField.setText(settings[6]);
            paymentForTradeUnionCommitteeTextField.setText(settings[7]);
            paymentForTheBRYUTextField.setText(settings[8]);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        saveButton.addActionListener(e -> saveActionListener());
        backButton.addActionListener(e -> backActionListener());
    }

    private void saveActionListener() {
        String[] settings = new String[] {
                basicScholarshipTextField.getText(),
                fellowshipMultiplier1TextField.getText(),
                fellowshipMultiplier2TextField.getText(),
                fellowshipMultiplier3TextField.getText(),
                fellowshipMultiplier4TextField.getText(),
                multiplierForScientificWorkTextField.getText(),
                multiplierForCulturalActivitiesTextField.getText(),
                paymentForTradeUnionCommitteeTextField.getText(),
                paymentForTheBRYUTextField.getText()};
        try {
            if (Double.parseDouble(settings[0]) < 0 || Double.parseDouble(settings[1]) < 1 || Double.parseDouble(settings[2]) < 1 ||
                    Double.parseDouble(settings[3]) < 1 || Double.parseDouble(settings[4]) < 1 ||
                    Double.parseDouble(settings[5]) < 1 || Double.parseDouble(settings[6]) < 1 ||
                    Double.parseDouble(settings[7]) < 0 || Double.parseDouble(settings[8]) < 0) {
                JOptionPane.showMessageDialog(null, "Ошибка изменения настроек. Базовая стипендия и отчисления должны быть неотрицательными, а множители >=1 ");
                return;
            }
        } catch(Exception e) {
            JOptionPane.showMessageDialog(null, "Данные введены не корректно.");
            return;
        }

        try {
            ClientTCP.clientObjectOutputStream.writeObject("17"); // отправка на сервер
            ClientTCP.clientObjectOutputStream.writeObject(settings);
            String success_fail = (String) ClientTCP.clientObjectInputStream.readObject();
            if(Objects.equals(success_fail, "success")) {
                JOptionPane.showMessageDialog(null, "Настройки успешно изменены.");
                backActionListener();
            }
            else
                JOptionPane.showMessageDialog(null, "Ошибка изменения настроек.");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void backActionListener() {
        try {
            ClientTCP.clientObjectOutputStream.writeObject("18"); // отправка на сервер
            AdminActions adminActions = new AdminActions();
            adminActions.setVisible(true);
            this.dispose();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
}
