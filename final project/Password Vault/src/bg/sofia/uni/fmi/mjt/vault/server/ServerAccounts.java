package bg.sofia.uni.fmi.mjt.vault.server;

import bg.sofia.uni.fmi.mjt.vault.Account;

public interface ServerAccounts {
    Account register(String username, String password, String passwordRe);

    Account login(String username, String password);

    Account changePassword(String username, String password, String newPassword);

    void saveAccounts();


}
