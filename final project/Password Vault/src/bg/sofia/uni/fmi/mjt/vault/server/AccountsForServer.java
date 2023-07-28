package bg.sofia.uni.fmi.mjt.vault.server;


import bg.sofia.uni.fmi.mjt.vault.Account;
import bg.sofia.uni.fmi.mjt.vault.api.Compromised;
import bg.sofia.uni.fmi.mjt.vault.api.Exposure;
import bg.sofia.uni.fmi.mjt.vault.exception.InvalidEnteredDataException;
import bg.sofia.uni.fmi.mjt.vault.exception.DataIsNotEqualException;
import bg.sofia.uni.fmi.mjt.vault.exception.UsernameIsAlreadyTakenException;
import bg.sofia.uni.fmi.mjt.vault.exception.IncorrectUsernameOrPasswordException;
import bg.sofia.uni.fmi.mjt.vault.hash.Hash;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class AccountsForServer implements ServerAccounts {
    private Map<String, String> accounts;
    final private Path path = Path.of("ServerAccount.txt");
    final static String SHA256_FOR_HASH = "SHA-256";
    final static int LENGTH_OF_SHA256 = 64;
    final File thisFile;

    private void readFromFile(Path file) {
        synchronized (AccountsForServer.class) {
            try (BufferedReader bufferedReader = Files.newBufferedReader(file)) {
                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    int index = line.indexOf(':');
                    accounts.put(line.substring(0, index), line.substring(index + 1));
                }
            } catch (IOException e) {
                throw new IllegalStateException("A problem occurred while reading from a file", e);
            }
        }
    }

    public AccountsForServer() throws IOException {
        accounts = new HashMap<>();
        thisFile = new File(path.toString());
        if (!thisFile.createNewFile()) {
            readFromFile(path);
        }
    }

    private void valid(String username, String password, String passwordTwo) {
        if (username == null || username.isEmpty() || username.isBlank() ||
                password == null || password.isEmpty() || password.isBlank()
                || passwordTwo == null || passwordTwo.isEmpty() || passwordTwo.isBlank()) {
            throw new InvalidEnteredDataException("Parameters are invalid!");
        }
    }

    @Override
    public Account register(String username, String password, String passwordRe) {
        valid(username, password, passwordRe);
        if (!password.equals(passwordRe)) {
            throw new DataIsNotEqualException("Passwords are not the same!");
        } else if (accounts.containsKey(username)) {
            throw new UsernameIsAlreadyTakenException("This username already exits in the system!");
        }
        Compromised.isItCompromised(Exposure.getNewsByParameters(password));
        return successful(username, password);
    }

    public Account successful(String username, String password) {
        accounts.put(username, Hash.makeHash(password, SHA256_FOR_HASH, LENGTH_OF_SHA256));
        Thread save = new Thread(this::saveAccounts);
        save.start();
        return new Account(username, password);
    }

    @Override
    public Account login(String username, String password) {
        valid(username, password, password);
        if (!accounts.containsKey(username) || !accounts.get(username)
                .equals(Hash.makeHash(password, SHA256_FOR_HASH, LENGTH_OF_SHA256))) {
            throw new IncorrectUsernameOrPasswordException("Username or password is wrong.");
        }

        return new Account(username, password);
    }

    @Override
    public Account changePassword(String username, String password, String newPassword) {
        valid(username, password, newPassword);
        return successful(username, newPassword);
    }

    @Override
    public void saveAccounts() {
        synchronized (AccountsForServer.class) {
            Set<Map.Entry<String, String>> toSave = accounts.entrySet();
            try (var bufferedWriter = Files.newBufferedWriter(path)) {
                for (var current : toSave) {
                    bufferedWriter.write(current.getKey() + ':' + current.getValue() + System.lineSeparator());
                    bufferedWriter.flush();
                }
            } catch (IOException e) {
                throw new IllegalStateException("A problem occurred while saving accounts to a file", e);
            }
        }
    }


}
