package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ClientTCP {
    static ObjectOutputStream clientObjectOutputStream;
    static ObjectInputStream clientObjectInputStream;

    public static void main(String[] arg) {
        try {
            LoginToTheSystem loginToTheSystem = new LoginToTheSystem();
            loginToTheSystem.setVisible(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
