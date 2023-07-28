package bg.sofia.uni.fmi.mjt.vault;

import java.io.Serializable;

public record Account(String username, String password) implements Serializable {
    @Override
    public String toString() {
        return "Account {" + System.lineSeparator() +
                "username = " + username + System.lineSeparator() +
                "password = " + password + System.lineSeparator() +
                '}' + System.lineSeparator();
    }
}
