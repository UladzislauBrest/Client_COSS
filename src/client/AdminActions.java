package client;

import javax.swing.*;
import java.io.IOException;

public class AdminActions extends JFrame {
    private JPanel panel;
    private JButton workWithTheListOfStudentButton;
    private JButton adjustmentOfTheCalculationOfScholarshipsButton;
    private JButton workWithTheUserBaseButton;
    private JButton backButton;

    AdminActions() {
        setSize(400, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setContentPane(panel);

        workWithTheListOfStudentButton.addActionListener(e -> workWithTheListOfStudentActionListener());
        adjustmentOfTheCalculationOfScholarshipsButton.addActionListener(e -> adjustmentOfTheCalculationOfScholarshipsActionListener());
        workWithTheUserBaseButton.addActionListener(e -> workWithTheUserBaseActionListener());
        backButton.addActionListener(e -> backActionListener());
    }

    private void workWithTheListOfStudentActionListener() {
        try {
            ClientTCP.clientObjectOutputStream.writeObject("6"); // отправка на сервер
            WorkWithTheListOfStudent workWithTheListOfStudent = new WorkWithTheListOfStudent("admin");
            workWithTheListOfStudent.setVisible(true);
            this.dispose();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    private void adjustmentOfTheCalculationOfScholarshipsActionListener() {
        try {
            ClientTCP.clientObjectOutputStream.writeObject("7"); // отправка на сервер
            AdjustmentOfTheCalculationOfScholarships adjustmentOfTheCalculationOfScholarships = new AdjustmentOfTheCalculationOfScholarships();
            adjustmentOfTheCalculationOfScholarships.setVisible(true);
            this.dispose();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    private void workWithTheUserBaseActionListener() {
        try {
            ClientTCP.clientObjectOutputStream.writeObject("8"); // отправка на сервер
            WorkWithTheUserBase workWithTheUserBase = new WorkWithTheUserBase();
            workWithTheUserBase.setVisible(true);
            this.dispose();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    private void backActionListener() {
        try {
            ClientTCP.clientObjectOutputStream.writeObject("9"); // отправка на сервер
            LoginToTheSystem loginToTheSystem = new LoginToTheSystem();
            loginToTheSystem.setVisible(true);
            this.dispose();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
}
