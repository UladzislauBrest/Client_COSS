package client;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ViewStudentTableModel extends AbstractTableModel
{
    // Список загловков для колонок в таблице
    private static final String[] headers = {"ID", "ФИО", "Средний балл", "Научная работа", "Культурная деятельность", "Социальная помощь", "Профком", "БРСМ", "Общежитие", "Штрафы", "Стипендия", "Размер стипендии"};
    // Список студентов, для отображать в таблице
    final List<Student> students = new ArrayList<>();

    ViewStudentTableModel(String students) {
        String masStudents[] = students.split("#");
        for(int i = 0; i < masStudents.length;)
            this.students.add(new Student(
                    masStudents[i++], masStudents[i++], masStudents[i++], masStudents[i++], masStudents[i++], masStudents[i++],
                    masStudents[i++], masStudents[i++], masStudents[i++], masStudents[i++], masStudents[i++]
            ));
    }

    Student getStudent(Long id_student) {
        for(Student student : students)
            if(Objects.equals(student.getId_student(), id_student))
                return student;
        return null;
    }

    @Override
    // Получить количество строк в таблице - у нас это размер коллекции
    public int getRowCount() {
        return students.size();
    }

    @Override
    // Получить количество столбцов
    public int getColumnCount() {
        return headers.length;
    }

    @Override
    // Вернуть заголовок колонки - мы его берем из массива headers
    public String getColumnName(int col) {
        return headers[col];
    }

    @Override
    // Получить объект для тображения в кокнретной ячейке таблицы
    // В данном случае мы отдаем строковое представление поля
    public Object getValueAt(int row, int col) {
        Student student = students.get(row);
        // В зависимости от номера колонки возвращаем то или иное поле контакта
        switch (col) {
            case 0: return student.getId_student();
            case 1: return student.getFio();
            case 2: return student.getAverage_score();
            case 3: return student.isScientific_work() ? "да" : "нет";
            case 4: return student.isCultural_activities() ? "да" : "нет";
            case 5: return student.getSocial_help();
            case 6: return student.isPayment_for_trade_union_committee() ? "да" : "нет";
            case 7: return student.isPayment_for_the_BRYU() ? "да" : "нет";
            case 8: return student.getPayment_for_a_hostel();
            case 9: return student.getAmount_of_fines();
            case 10: return student.isScholarship() ? "да" : "нет";
            default: return student.getValueScholarship();
        }
    }

    void deleteFilter(boolean[] filter, boolean[] filter2, String fio, double fromAverageScore, double tillAverageScore) {
        Student student;
        for(int i = 0; i < students.size(); i++) {
            student = students.get(i);
            if(!student.getFio().contains(fio) || student.getAverage_score() < fromAverageScore ||
                    student.getAverage_score() > tillAverageScore ||
                    (filter2[0] && filter[0] != student.isScientific_work()) ||
                    (filter2[1] && filter[1] != student.isCultural_activities()) ||
                    (filter2[2] && (filter[2] ? student.getSocial_help()==0 : student.getSocial_help()!=0)) ||
                    (filter2[3] && filter[3] != student.isPayment_for_trade_union_committee()) ||
                    (filter2[4] && filter[4] != student.isPayment_for_the_BRYU()) ||
                    (filter2[5] && (filter[5] ? student.getPayment_for_a_hostel()==0 : student.getPayment_for_a_hostel()!=0)) ||
                    (filter2[6] && (filter[6] ? student.getAmount_of_fines()==0 : student.getAmount_of_fines()!=0)) ||
                    (filter2[7] && filter[7] != student.isScholarship())
                    ) {
                this.students.remove(i);
                i--;
            }
        }
    }
}
