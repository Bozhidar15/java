package bg.sofia.uni.fmi.mjt.vault.users;

import bg.sofia.uni.fmi.mjt.vault.Account;
import bg.sofia.uni.fmi.mjt.vault.crypting.EncryptAndDecrypt;
import bg.sofia.uni.fmi.mjt.vault.crypting.PasswordAndDecryptingKey;
import bg.sofia.uni.fmi.mjt.vault.exception.NoRegistrationInThisWebsiteException;
import bg.sofia.uni.fmi.mjt.vault.exception.NoSuchAUserInThisWebsiteException;
import bg.sofia.uni.fmi.mjt.vault.exception.EncryptDecryptException;
import bg.sofia.uni.fmi.mjt.vault.exception.NoPasswordException;
import bg.sofia.uni.fmi.mjt.vault.password.GeneratePassword;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class UserDataAccounts implements UserWebsitesAccounts, Serializable {

    private Map<String, Map<String, PasswordAndDecryptingKey>> websiteAccounts;


    UserDataAccounts() {
        websiteAccounts = new HashMap<>();
    }

    public Map<String, Map<String, PasswordAndDecryptingKey>> getWebsiteAccounts() {
        return websiteAccounts;
    }



    @Override
    public Account retrieveCredentials(String website, String username) {
        if (!websiteAccounts.containsKey(website)) {
            throw new NoRegistrationInThisWebsiteException("No registration available on this site!");
        } else if (!websiteAccounts.get(website).containsKey(username)) {
            throw new NoSuchAUserInThisWebsiteException("This username on this website is not in the system!");
        }
        Account toReturn;
        try {
            if (websiteAccounts.get(website).get(username) == null) {
                return new Account(username, "This account has no password saved!");
            }
            toReturn = new Account(username,
                    EncryptAndDecrypt.decrypt(websiteAccounts.get(website).get(username)));
        } catch (Exception e) {
            throw new EncryptDecryptException("Error while decrypt your password!" + System.lineSeparator());
        }
        return toReturn;
    }

    @Override
    public Account generatePassword(String website, String username) {
        if (!websiteAccounts.containsKey(website)) {
            websiteAccounts.put(website, new HashMap<>());
        }
        websiteAccounts.get(website).remove(username);
        Account current = new Account(username, GeneratePassword.createNewPassword());
        try {
            websiteAccounts.get(website).put(username, EncryptAndDecrypt.encrypt(current.password()));
        } catch (Exception e) {
            throw new EncryptDecryptException("Error while encrypting your password!" + System.lineSeparator());
        }
        return current;
    }

    @Override
    public Account addPassword(String website, String username, String password) {
        if (!websiteAccounts.containsKey(website)) {
            websiteAccounts.put(website, new HashMap<>());
        }
        websiteAccounts.get(website).remove(username);
        try {
            websiteAccounts.get(website).put(username, EncryptAndDecrypt.encrypt(password));
        } catch (Exception e) {
            throw new EncryptDecryptException("Error while encrypting your password!" + System.lineSeparator());
        }
        return new Account(username, password);
    }

    @Override
    public void removePassword(String website, String username) {
        if (!websiteAccounts.containsKey(website)) {
            throw new NoRegistrationInThisWebsiteException("No registration available on this site!");
        } else if (!websiteAccounts.get(website).containsKey(username)) {
            throw new NoSuchAUserInThisWebsiteException("This username on this website is not in the system!");
        }
        if (websiteAccounts.get(website).get(username) != null) {
            websiteAccounts.get(website).remove(username);
            websiteAccounts.get(website).put(username, null);
        } else {
            throw new NoPasswordException("This account doesn't have password!");
        }
    }

}
