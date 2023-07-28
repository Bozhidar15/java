package bg.sofia.uni.fmi.mjt.vault.server;

import bg.sofia.uni.fmi.mjt.vault.Account;
import bg.sofia.uni.fmi.mjt.vault.ParametersLogin;
import bg.sofia.uni.fmi.mjt.vault.exception.SendMessageException;
import bg.sofia.uni.fmi.mjt.vault.exception.ShutDownServerException;
import bg.sofia.uni.fmi.mjt.vault.exception.InvalidEnteredDataException;
import bg.sofia.uni.fmi.mjt.vault.exception.DataIsNotEqualException;
import bg.sofia.uni.fmi.mjt.vault.exception.NoValidOperationException;
import bg.sofia.uni.fmi.mjt.vault.exception.UsernameIsAlreadyTakenException;
import bg.sofia.uni.fmi.mjt.vault.exception.ErrorsInRequestOrResponseException;
import bg.sofia.uni.fmi.mjt.vault.exception.HttpResponseException;
import bg.sofia.uni.fmi.mjt.vault.exception.CompromisedException;
import bg.sofia.uni.fmi.mjt.vault.exception.PasswordIsNotInTheDataBaseExseption;
import bg.sofia.uni.fmi.mjt.vault.exception.IncorrectUsernameOrPasswordException;


import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class ServerSignUpMessages {
    private final SocketChannel clientChannel;
    private static final int BUFFER_SIZE = 1024;
    private ByteBuffer buffer = ByteBuffer.allocateDirect(BUFFER_SIZE);
    private final AccountsForServer serverData;
    private Account currentClientData;

    public Account getCurrentClientData() {
        return currentClientData;
    }

    private void sendMessage(String message) {
        buffer.clear();
        buffer.put(message.getBytes());
        buffer.flip();
        try {
            clientChannel.write(buffer);
        } catch (IOException e) {
            throw new SendMessageException("Error while sending message" + System.lineSeparator());
        }

    }

    public ServerSignUpMessages(SocketChannel channel, AccountsForServer data) throws IOException {
        serverData = data;
        this.clientChannel = channel;
    }

    private ParametersLogin returnParameters(String line, int numberOfElements) {
        final int passwordNumber = 2;
        final int userNumber = 1;
        final int passwordNumberTwo = 3;
        final int optionOne = 3;
        String[] words = line.split(" ");
        if (words.length != numberOfElements) {
            throw new InvalidEnteredDataException("Invalid number of parameters!");
        }
        if (numberOfElements == optionOne) {
            return new ParametersLogin(words[userNumber], words[passwordNumber], null);
        }
        return new ParametersLogin(words[userNumber], words[passwordNumber], words[passwordNumberTwo]);
    }

    public boolean getOption(String sent) {
        if (sent.equals("/?")) {
            return showOptions();
        } else if (sent.startsWith("register")) {
            return registerAccount(sent);
        } else if (sent.startsWith("login")) {
            return loginAccount(sent);
        } else if (sent.equals("disconnect")) {
            sendMessage("Have a nice day!" + System.lineSeparator());
            return false;
        } else if (sent.equals("save_disconnect_server")) {
            Thread save = new Thread(serverData::saveAccounts);
            save.start();
            throw new ShutDownServerException("Shut Down Server!");
        }
        final String helpOption = "If you want to see all options in the server type -> /?" + System.lineSeparator();
        throw new NoValidOperationException(helpOption + "No valid operation has been written"
                + System.lineSeparator());
    }

    private boolean showOptions() {
        final String options = "register <user> <password> <password-repeat>" + System.lineSeparator() +
                "login <user> <password>" + System.lineSeparator() +
                "disconnect" + System.lineSeparator();
        sendMessage(options);
        return false;
    }

    private boolean registerAccount(String line) { // register koko kk kk
        final int numberOfElements = 4;
        ParametersLogin parameters;
        try {
            parameters = returnParameters(line, numberOfElements);
        }
        catch (InvalidEnteredDataException e) {
            sendMessage(e.getMessage() + System.lineSeparator());
            return false;
        }
        try {
            currentClientData = serverData.register(parameters.username(), parameters.password(),
                    parameters.passwordTwo());
        } catch (InvalidEnteredDataException | DataIsNotEqualException | UsernameIsAlreadyTakenException e) {
            sendMessage(e.getMessage() + System.lineSeparator());
            return false;
        } catch (ErrorsInRequestOrResponseException | HttpResponseException e) {
            sendMessage("We couldn't check if your password is compromised!" + System.lineSeparator() +
                    "Please try again later!" + System.lineSeparator());
            return false;
        } catch (CompromisedException e) {
            sendMessage(e.getMessage());
            return false;
        } catch (PasswordIsNotInTheDataBaseExseption e) {
            final String message = "Your password is very strong!" + System.lineSeparator();
            sendMessage(message);
            currentClientData = serverData.successful(parameters.username(), parameters.password());
        }
        return true;
    }

    public boolean changePassword(String line) {
        final int numberOfElements = 4;
        ParametersLogin parametersLogin;
        try {
            parametersLogin = returnParameters(line, numberOfElements);
        }
        catch (InvalidEnteredDataException e) {
            sendMessage(e.getMessage() + System.lineSeparator());
            return false;
        }
        try {
            serverData.changePassword(parametersLogin.username(), parametersLogin.password(),
                    parametersLogin.passwordTwo());
            currentClientData = new Account(parametersLogin.username(), parametersLogin.passwordTwo());
        } catch (InvalidEnteredDataException | IncorrectUsernameOrPasswordException e) {
            sendMessage(e.getMessage() + System.lineSeparator());
            return false;
        }
        return true;
    }
    private boolean loginAccount(String line) {
        final int numberOfElements = 3;
        ParametersLogin parametersLogin;
        try {
            parametersLogin = returnParameters(line, numberOfElements);
            currentClientData = serverData.login(parametersLogin.username(), parametersLogin.password());
        } catch (InvalidEnteredDataException | IncorrectUsernameOrPasswordException e) {
            sendMessage(e.getMessage() + System.lineSeparator());
            return false;
        }

        return true;
    }


}
