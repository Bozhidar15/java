package bg.sofia.uni.fmi.mjt.vault.users;

import bg.sofia.uni.fmi.mjt.vault.Account;
import bg.sofia.uni.fmi.mjt.vault.ParametersWebsite;
import bg.sofia.uni.fmi.mjt.vault.api.Compromised;
import bg.sofia.uni.fmi.mjt.vault.api.Exposure;
import bg.sofia.uni.fmi.mjt.vault.exception.ReadingDataFromFileException;
import bg.sofia.uni.fmi.mjt.vault.exception.SendMessageException;
import bg.sofia.uni.fmi.mjt.vault.exception.DisconnectOrLogoutException;
import bg.sofia.uni.fmi.mjt.vault.exception.NoValidOperationException;
import bg.sofia.uni.fmi.mjt.vault.exception.InvalidEnteredDataException;
import bg.sofia.uni.fmi.mjt.vault.exception.CompromisedException;
import bg.sofia.uni.fmi.mjt.vault.exception.ErrorsInRequestOrResponseException;
import bg.sofia.uni.fmi.mjt.vault.exception.HttpResponseException;
import bg.sofia.uni.fmi.mjt.vault.exception.PasswordIsNotInTheDataBaseExseption;
import bg.sofia.uni.fmi.mjt.vault.exception.ChangePasswordException;
import bg.sofia.uni.fmi.mjt.vault.exception.EncryptDecryptException;
import bg.sofia.uni.fmi.mjt.vault.exception.NoRegistrationInThisWebsiteException;
import bg.sofia.uni.fmi.mjt.vault.exception.NoPasswordException;
import bg.sofia.uni.fmi.mjt.vault.exception.NoSuchAUserInThisWebsiteException;

import java.io.File;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.file.Files;
import java.nio.file.Path;




public class UserProfile {
    private Account userParameters;
    private SocketChannel clientChannel;
    private UserDataAccounts userData;
    private static final int BUFFER_SIZE = 1024;
    private ByteBuffer buffer = ByteBuffer.allocateDirect(BUFFER_SIZE);
    private final Path pathOfFile;
    private final File yourFile;

    private void readDataFromUserFile(Path pathOfFile) {
        synchronized (UserDataAccounts.class) {
            try (var objectInputStream = new ObjectInputStream(Files.newInputStream(pathOfFile))) {

                Object userDataObject;
                if ((userDataObject = objectInputStream.readObject()) != null) {
                    userData = (UserDataAccounts) userDataObject;
                } else {
                    throw new ReadingDataFromFileException("Object is null!");
                }

            } catch (IOException | ClassNotFoundException ex) {
                throw new ReadingDataFromFileException("Error while read client data from file!");
            }
        }
    }

    private void writeDataToFile() {
        synchronized (UserDataAccounts.class) {
            try (var objectOutputStream = new ObjectOutputStream(Files.newOutputStream(pathOfFile))) {
                objectOutputStream.writeObject(userData);
                objectOutputStream.flush();
            } catch (IOException e) {
                throw new IllegalStateException("A problem occurred while writing to a file" +
                        System.lineSeparator(), e);
            }
        }
    }

    public UserProfile(Account account, SocketChannel channel) {
        userParameters = account;
        clientChannel = channel;
        pathOfFile = Path.of(userParameters.username() + ".txt");
        yourFile = new File(pathOfFile.toString());
        try {
            if (!yourFile.createNewFile()) {
                readDataFromUserFile(pathOfFile);
            } else {
                userData = new UserDataAccounts();
            }
        }
        catch (IOException e) {
            throw new ReadingDataFromFileException("Error check for created file!" + System.lineSeparator());
        }


    }

    private void sendMessage(String message) {
        buffer.clear();
        buffer.put(message.getBytes());
        buffer.flip();
        try {
            clientChannel.write(buffer);
        } catch (IOException e) {
            throw new SendMessageException("Error while sending message" + System.lineSeparator(), e);
        }

    }

    public String getOption(String sent) {
        if (sent.equals("/?")) {
            return showOptions();
        } else if (sent.startsWith("retrieve-credentials")) {
            return retrieveCredentials(sent);
        } else if (sent.startsWith("generate-password")) {
            return generatePassword(sent);
        } else if (sent.startsWith("add-password")) {
            return addPassword(sent);
        } else if (sent.startsWith("remove-password")) {
            return removePassword(sent);
        } else if (sent.startsWith("change-server-password")) {
            return changePassword(sent);
        } else if (sent.equals("show")) {
            return show();
        } else if (sent.equals("disconnect") || sent.equals("logout")) {
            sendMessage("Bye... Have a nice day!" + System.lineSeparator());
            Thread save = new Thread(this::writeDataToFile);
            save.start();
            throw new DisconnectOrLogoutException("Client want to disconnect or log out!");
        }
        final String helpOption = "If you want to see all options in the server type -> /?" + System.lineSeparator();
        throw new NoValidOperationException(helpOption + "No valid operation has been written"
                + System.lineSeparator());
    }

    private String changePassword(String line) {
        final int numberOfElements = 3;
        ParametersWebsite parameters;
        String message;
        String enteredPassword = "";
        String enteredNewPassword = "";
        String name;
        try {
            parameters = returnParameters(line, numberOfElements);
            enteredPassword = parameters.website();
            enteredNewPassword = parameters.username();
            if (enteredPassword.equals(userParameters.password())) {
                if (enteredPassword.equals(enteredNewPassword)) {
                    sendMessage("You entered the same password! Use another password (:" + System.lineSeparator());
                    return "You entered the same password! Use another password (:" + System.lineSeparator();
                }
                Compromised.isItCompromised(Exposure.getNewsByParameters(enteredNewPassword));
            } else {
                sendMessage("You entered wrong current password!" + System.lineSeparator());
                return "You entered wrong current password!" + System.lineSeparator();
            }
        } catch (InvalidEnteredDataException e) {
            message = e.getMessage() + System.lineSeparator();
            sendMessage(message);
            return message;
        } catch (ErrorsInRequestOrResponseException | HttpResponseException e) {
            message = "We couldn't check if your new password is compromised!" + System.lineSeparator() +
                    "Please try again later!" + System.lineSeparator();
            sendMessage(message);
            return message;
        } catch (CompromisedException e) {
            sendMessage(e.getMessage());
            return e.getMessage();
        } catch (PasswordIsNotInTheDataBaseExseption e) {
            name = userParameters.username();
        }
        name = userParameters.username();
        userParameters = new Account(name, enteredNewPassword);
        message = "change-password " + userParameters.username() + " " +  enteredPassword + " " + enteredNewPassword;
        throw new ChangePasswordException(message);
    }

    private String show() {
        var allUserWebsitesAccounts =
                userData.getWebsiteAccounts().entrySet();
        if (allUserWebsitesAccounts.size() == 0) {
            String mess = "No registered websites and accounts!" + System.lineSeparator();
            sendMessage(mess);
            return mess;
        }
        var iterator = allUserWebsitesAccounts.iterator();
        StringBuilder output = new StringBuilder();
        while (iterator.hasNext()) { //websites
            var data = iterator.next();
            var setOfAccounts = data.getValue().entrySet();
            output.append(data.getKey()).append(System.lineSeparator());
            for (var current : setOfAccounts) {
                output.append(current.getKey()).append(" : ");
                if (current.getValue() == null) {
                    output.append("This account has no password saved!").append(System.lineSeparator());
                } else {

                    try {
                        output.append(current.getValue());
                        Compromised.isItCompromised(Exposure.getNewsByParameters(current.getValue().password()));
                    } catch (ErrorsInRequestOrResponseException | HttpResponseException e) {
                        output.append("  Couldn't check this pass if it is vulnerable!");
                    } catch (PasswordIsNotInTheDataBaseExseption e) {
                        output.append("  OK");
                    } catch (CompromisedException e) {
                        output.append("  Your password is now compromised!");
                    } catch (EncryptDecryptException e) {
                        output.append(e.getMessage());
                    }
                    output.append(System.lineSeparator());
                }
            }
            output.append(System.lineSeparator());
        }
        sendMessage(output.toString());
        return output.toString();
    }

    private String showOptions() {
        final String options = "retrieve-credentials <website> <user>" + System.lineSeparator() +
                "generate-password <website> <user>" + System.lineSeparator()
                + "add-password <website> <user> <password>" + System.lineSeparator()
                + "remove-password <website> <user>" + System.lineSeparator()
                + "change-server-password <oldPassword> <NewPassword>" + System.lineSeparator()
                + "\"show\" - show all websites with registrations" + System.lineSeparator()
                + "logout" + System.lineSeparator() +
                "disconnect" + System.lineSeparator();
        sendMessage(options);
        return options;
    }

    private void checkEnteredData(String... data) {
        for (String current : data) {
            if (current == null || current.isEmpty() || current.isBlank()) {
                throw new InvalidEnteredDataException("One of the parameters is invalid!");
            }
        }
    }

    private ParametersWebsite returnParameters(String line, int numberOfElements) {
        final int websiteNumber = 1;
        final int userNumber = 2;
        final int passwordNumber = 3;
        final int optionOne = 3;
        String[] words = line.split(" ");
        if (words.length != numberOfElements) {
            throw new InvalidEnteredDataException("Invalid number of parameters!");
        }
        if (numberOfElements == optionOne) {
            checkEnteredData(words[websiteNumber], words[userNumber]);
            return new ParametersWebsite(words[websiteNumber], words[userNumber], null);
        }
        checkEnteredData(words[websiteNumber], words[userNumber], words[passwordNumber]);
        return new ParametersWebsite(words[websiteNumber], words[userNumber], words[passwordNumber]);
    }

    private String removePassword(String line) {
        final int numberOfElements = 3;
        ParametersWebsite parameters;
        String message;
        try {
            parameters = returnParameters(line, numberOfElements);
            userData.removePassword(parameters.website(), parameters.username());
        } catch (InvalidEnteredDataException | NoRegistrationInThisWebsiteException |
                 NoSuchAUserInThisWebsiteException | NoPasswordException e) {
            message = e.getMessage() + System.lineSeparator();
            sendMessage(message);
            return message;
        }
        message = "Password was successfully removed!" + System.lineSeparator();
        sendMessage(message);
        return message;
    }
    private String addPassword(String line) {
        final int numberOfElements = 4;
        ParametersWebsite parameters;
        Account generated;
        String message;
        try {
            parameters = returnParameters(line, numberOfElements);
            Compromised.isItCompromised(Exposure.getNewsByParameters(parameters.password()));
            generated = userData.addPassword(parameters.website(), parameters.username(), parameters.password());
        } catch (InvalidEnteredDataException e) {
            message = e.getMessage() + System.lineSeparator();
            sendMessage(message);
            return message;
        } catch (ErrorsInRequestOrResponseException | HttpResponseException e) {
            sendMessage("We couldn't check if your password is compromised!" + System.lineSeparator() +
                    "Please try again later!" + System.lineSeparator());
            return e.getMessage();
        } catch (PasswordIsNotInTheDataBaseExseption e) {
            parameters = returnParameters(line, numberOfElements);
            generated = userData.addPassword(parameters.website(), parameters.username(), parameters.password());
            message = "Your password is very strong!" + System.lineSeparator() + generated.toString();
            sendMessage(message);
            return e.getMessage() + message;
        } catch (CompromisedException e) {
            sendMessage(e.getMessage());
            return e.getMessage();
        } catch (EncryptDecryptException e) {
            message = "Couldn't add your password now. Please try again later!" + System.lineSeparator();
            sendMessage(message);
            return e.getMessage() + message;
        }
        sendMessage(generated.toString());
        return generated.toString();
    }

    private String generatePassword(String line) {
        final int numberOfElements = 3;
        ParametersWebsite parameters;
        Account generated;
        try {
            parameters = returnParameters(line, numberOfElements);
            generated = userData.generatePassword(parameters.website(), parameters.username());
        } catch (InvalidEnteredDataException e) {
            String message = e.getMessage() + System.lineSeparator();
            sendMessage(message);
            return message;
        } catch (EncryptDecryptException e) {
            String message = "Couldn't generate you a password now. Please try again later!" + System.lineSeparator();
            sendMessage(message);
            return e.getMessage() + message;
        }
        sendMessage(generated.toString());
        return generated.toString();
    }

    private String retrieveCredentials(String line) {
        final int numberOfElements = 3;
        ParametersWebsite parameters;
        Account retrieved;
        String message;
        try {
            parameters = returnParameters(line, numberOfElements);
            retrieved = userData.retrieveCredentials(parameters.website(), parameters.username());
        } catch (InvalidEnteredDataException | NoSuchAUserInThisWebsiteException |
                 NoRegistrationInThisWebsiteException e) {
            message = e.getMessage() + System.lineSeparator();
            sendMessage(message);
            return message;
        } catch (EncryptDecryptException e) {
            message = "Couldn't access your password now. Please try again later!" + System.lineSeparator();
            sendMessage(message);
            return e.getMessage() + message;
        }
        sendMessage(retrieved.toString());
        return retrieved.toString();
    }

}
