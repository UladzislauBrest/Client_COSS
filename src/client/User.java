package client;

import java.util.Objects;

public class User {
    private Long id_user;
    private String login;
    private String password;
    private String role;

    User(String id_user, String login, String password, String role) {
        this.id_user = Long.valueOf(id_user);
        this.login = login;
        this.password = password;
        this.role = role;
    }

    Long getId_user() {
        return id_user;
    }
    String getLogin() {
        return login;
    }
    String getPassword() {
        return password;
    }
    Boolean isRole() {
        return Objects.equals(role, "1");
    }

    public String toString() {
        return id_user + "#" + login + "#" + password + "#" + role + "#";
    }
}
