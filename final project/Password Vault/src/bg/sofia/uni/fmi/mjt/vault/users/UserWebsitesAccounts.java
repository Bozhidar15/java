package bg.sofia.uni.fmi.mjt.vault.users;

import bg.sofia.uni.fmi.mjt.vault.Account;

public interface UserWebsitesAccounts {
    Account retrieveCredentials(String website, String username);

    Account generatePassword(String website, String username);

    Account addPassword(String website, String username, String password);

    void removePassword(String website, String username);


}
