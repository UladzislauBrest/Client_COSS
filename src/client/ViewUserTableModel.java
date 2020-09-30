package client;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ViewUserTableModel extends AbstractTableModel {
    // Список загловков для колонок в таблице
    private static final String[] headers = {"ID", "Логин", "Пароль", "Роль"};
    // Список пользователей, для отображать в таблице
    private final List<User> users = new ArrayList<>();

    ViewUserTableModel(String users) {
        String masUser[] = users.split("#");
        for(int i = 0; i < masUser.length;) {
            this.users.add(new User(
                    masUser[i++], masUser[i++], masUser[i++], masUser[i++]
            ));
        }
    }

    User getUser(Long id_user) {
        for(User user : users) {
            if(Objects.equals(user.getId_user(), id_user)) {
                return user;
            }
        }
        return null;
    }

    @Override
    // Получить количество строк в таблице - у нас это размер коллекции
    public int getRowCount() {
        return users.size();
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
        User user = users.get(row);
        // В зависимости от номера колонки возвращаем то или иное поле контакта
        switch (col) {
            case 0: return user.getId_user();
            case 1: return user.getLogin();
            case 2: return user.getPassword();
            default: return user.isRole() ? "admin" : "user";
        }
    }
}
