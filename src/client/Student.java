package client;

import java.io.IOException;
import java.util.Objects;

public class Student
{
    private Long id_student;
    private String fio;
    private String average_score;
    private String scientific_work;
    private String cultural_activities;
    private String social_help;
    private String payment_for_trade_union_committee;
    private String payment_for_the_BRYU;
    private String payment_for_a_hostel;
    private String amount_of_fines;
    private String scholarship;

    Student(String id_student, String fio, String average_score, String scientific_work, String cultural_activities,
            String social_help, String payment_for_trade_union_committee, String payment_for_the_BRYU,
            String payment_for_a_hostel, String amount_of_fines, String scholarship) {
        this.id_student = Long.valueOf(id_student);
        this.fio = fio;
        this.average_score = average_score;
        this.scientific_work = scientific_work;
        this.cultural_activities = cultural_activities;
        this.social_help = social_help;
        this.payment_for_trade_union_committee = payment_for_trade_union_committee;
        this.payment_for_the_BRYU = payment_for_the_BRYU;
        this.payment_for_a_hostel = payment_for_a_hostel;
        this.amount_of_fines = amount_of_fines;
        this.scholarship = scholarship;
    }

    Long getId_student() {
        return id_student;
    }
    String getFio() {
        return fio;
    }
    double getAverage_score() {
        return Double.parseDouble(average_score);
    }
    boolean isScientific_work() {
        return Objects.equals(scientific_work, "1");
    }
    boolean isCultural_activities() {
        return Objects.equals(cultural_activities, "1");
    }
    double getSocial_help() {
        return Double.parseDouble(social_help);
    }
    boolean isPayment_for_trade_union_committee() {
        return Objects.equals(payment_for_trade_union_committee, "1");
    }
    boolean isPayment_for_the_BRYU() {
        return Objects.equals(payment_for_the_BRYU, "1");
    }
    double getPayment_for_a_hostel() {
        return Double.parseDouble(payment_for_a_hostel);
    }
    double getAmount_of_fines() {
        return Double.parseDouble(amount_of_fines);
    }
    boolean isScholarship() {
        return Objects.equals(scholarship, "1");
    }

    double getValueScholarship() {
        try {
            ClientTCP.clientObjectOutputStream.writeObject("25");
            String[] setting = (String[]) ClientTCP.clientObjectInputStream.readObject();
            if(!isScholarship())
                setting[0] = "0";
            double fellowshipMultiplier;
            if(getAverage_score() < 5)
                fellowshipMultiplier = 0;
            else if(getAverage_score() < 6)
                fellowshipMultiplier = 1;
            else if(getAverage_score() < 7)
                fellowshipMultiplier = Double.parseDouble(setting[1]);
            else if(getAverage_score() < 8)
                fellowshipMultiplier = Double.parseDouble(setting[2]);
            else if(getAverage_score() < 9)
                fellowshipMultiplier = Double.parseDouble(setting[3]);
            else
                fellowshipMultiplier = Double.parseDouble(setting[4]);
            double scientificWork = 1, cultActiv = 1;
            if(isScientific_work())
                scientificWork = Double.parseDouble(setting[5]);
            if(isCultural_activities())
                cultActiv = Double.parseDouble(setting[6]);
            double scholarship = Double.parseDouble(setting[0]) *
                    fellowshipMultiplier *
                    scientificWork *
                    cultActiv +
                    getSocial_help() -
                    (isPayment_for_trade_union_committee()?Double.parseDouble(setting[7]):0) -
                    (isPayment_for_the_BRYU()?Double.parseDouble(setting[8]):0) -
                    getPayment_for_a_hostel() -
                    getAmount_of_fines();
            return Math.rint(100.0 * scholarship) / 100.0;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public String toString() {
        return id_student + "#" + fio + "#" + average_score + "#" + scientific_work + "#" + cultural_activities + "#" +
                social_help + "#" + payment_for_trade_union_committee + "#" + payment_for_the_BRYU + "#" +
                payment_for_a_hostel + "#" + amount_of_fines + "#" + scholarship + "#" + getValueScholarship() + "#";
    }
}