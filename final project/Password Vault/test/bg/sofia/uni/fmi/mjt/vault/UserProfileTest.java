package bg.sofia.uni.fmi.mjt.vault;


import bg.sofia.uni.fmi.mjt.vault.exception.ChangePasswordException;
import bg.sofia.uni.fmi.mjt.vault.exception.DisconnectOrLogoutException;
import bg.sofia.uni.fmi.mjt.vault.exception.NoValidOperationException;
import bg.sofia.uni.fmi.mjt.vault.users.UserProfile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

@ExtendWith(MockitoExtension.class)
public class UserProfileTest {

    @Mock
    private SocketChannel socketChannel;

    private UserProfile userProfile;


    private final Account account = new Account("pavel", "myPassword");

    @BeforeEach
    public void before() {
        userProfile = new UserProfile(account, socketChannel);
        String toBeAdd = "add-password dalivali.bg koko my123New123Password321";
        userProfile.getOption(toBeAdd);
    }

    @Test
    public void afterTest() {
        final String save = "logout";
        assertThrows(DisconnectOrLogoutException.class, () -> userProfile.getOption(save));
    }

    @Test
    public void invalidOperationTest() {
        String line = "what to do";
        assertThrows(NoValidOperationException.class, () -> userProfile.getOption(line),
                "Entered invalid option");
    }

    @Test
    public void showAllOptionsTest() throws IOException {
        String line = "/?";
        String options = "retrieve-credentials <website> <user>" + System.lineSeparator() +
                "generate-password <website> <user>" + System.lineSeparator()
                + "add-password <website> <user> <password>" + System.lineSeparator()
                + "remove-password <website> <user>" + System.lineSeparator()
                + "change-server-password <oldPassword> <NewPassword>" + System.lineSeparator()
                + "\"show\" - show all websites with registrations" + System.lineSeparator()
                + "logout" + System.lineSeparator() +
                "disconnect" + System.lineSeparator();
        when(socketChannel.write(Mockito.any(ByteBuffer.class))).thenReturn(0);
        assertEquals(options, userProfile.getOption(line), "Expected to show all options and return true");
        afterTest();
    }

    @Test
    public void addPasswordNotEnoughParametersTest() {
        String line = "add-password dalivali.bg koko";
        String toReturn = "Invalid number of parameters!" + System.lineSeparator();
        assertEquals(toReturn, userProfile.getOption(line),
                "no valid number of parameters in sent message");
        afterTest();
    }

    @Test
    public void addPasswordTooMuchParametersTest() {
        String line = "add-password dalivali.bg koko myNewPassword myNewPassword";
        String toReturn = "Invalid number of parameters!" + System.lineSeparator();
        assertEquals(toReturn, userProfile.getOption(line),
                "no valid number of parameters in sent message");
        afterTest();
    }

    @Test
    public void addPasswordWithInvalidParametersTest() {
        String line = "add-password dalivali.bg koko " + null + " myNewPassword";
        String toReturn = "Invalid number of parameters!" + System.lineSeparator();
        assertEquals(toReturn, userProfile.getOption(line),
                "no valid number or element of parameters in sent message");
        afterTest();
    }

    @Test
    public void addStrongPasswordParametersTest() throws IOException {
        String line = "add-password dalivali.bg koko my123New123Password321";
        Account acc = new Account("koko", "my123New123Password321");
        String toReturn = "Could not find password in the data base!" + System.lineSeparator() +
                "Your password is very strong!"
                + System.lineSeparator() + acc;
        when(socketChannel.write(Mockito.any(ByteBuffer.class))).thenReturn(0);
        assertEquals(toReturn, userProfile.getOption(line),
                "no valid number of parameters in sent message");
        afterTest();
    }

    @Test
    public void addCompromisedPasswordParametersTest() throws IOException {
        String line = "add-password dalivali.bg koko 123";
        String toReturn = "Your password is not saved in our system, because it is compromised! " +
                System.lineSeparator() + "We recommend you to add a new one!" + System.lineSeparator();
        when(socketChannel.write(Mockito.any(ByteBuffer.class))).thenReturn(0);
        assertEquals(toReturn, userProfile.getOption(line),
                "no valid number of parameters in sent message");
        afterTest();
    }

    @Test
    public void retrieveCredentialsTooMuchNumberParametersTest() throws IOException {
        String line = "retrieve-credentials dalivali.bg koko something";
        String toReturn = "Invalid number of parameters!" + System.lineSeparator();
        when(socketChannel.write(Mockito.any(ByteBuffer.class))).thenReturn(0);
        assertEquals(toReturn, userProfile.getOption(line),
                "should throw error message due to wrong number of elements");
        afterTest();
    }

    @Test
    public void retrieveCredentialsInvalidNumberParametersTest() throws IOException {
        String line = "retrieve-credentials dalivali.bg ";
        String toReturn = "Invalid number of parameters!" + System.lineSeparator();
        when(socketChannel.write(Mockito.any(ByteBuffer.class))).thenReturn(0);
        assertEquals(toReturn, userProfile.getOption(line),
                "should throw error message due to wrong number of elements");
        afterTest();
    }

    @Test
    public void retrieveCredentialsNotInTheSystemTest() throws IOException {
        String line = "retrieve-credentials dalivali.bg gosho";
        String toReturn = "This username on this website is not in the system!" + System.lineSeparator();
        when(socketChannel.write(Mockito.any(ByteBuffer.class))).thenReturn(0);
        assertEquals(toReturn, userProfile.getOption(line),
                "should throw error message due to username not in the system ");
        afterTest();
    }

    @Test
    public void retrieveCredentialsNotRegisteredWebsiteParametersTest() throws IOException {
        String line = "retrieve-credentials zamunda.bg gosho";
        String toReturn = "No registration available on this site!" + System.lineSeparator();
        when(socketChannel.write(Mockito.any(ByteBuffer.class))).thenReturn(0);
        assertEquals(toReturn, userProfile.getOption(line),
                "should throw error message due to username not in the system ");
        afterTest();
    }

    @Test
    public void retrieveCredentialsAllCorrectParametersTest() throws IOException {
        Account acc = new Account("koko", "my123New123Password321");
        String line = "retrieve-credentials dalivali.bg koko";
        when(socketChannel.write(Mockito.any(ByteBuffer.class))).thenReturn(0);
        assertEquals(acc.toString(), userProfile.getOption(line),
                "should write the account with username and password");
        afterTest();
    }

    @Test
    public void generatePasswordAllCorrectParametersTest() throws IOException {
        String toBeAdd = "generate-password dalivali.bg second";
        String acc = userProfile.getOption(toBeAdd);
        String line = "retrieve-credentials dalivali.bg second";
        when(socketChannel.write(Mockito.any(ByteBuffer.class))).thenReturn(0);
        assertEquals(acc, userProfile.getOption(line),
                "should write the account with username and new generated password");
        afterTest();
    }

    @Test
    public void generatePasswordInvalidParametersTest() throws IOException {
        String toBeAdd = "generate-password dalivali.bg";
        String toReturn = "Invalid number of parameters!" + System.lineSeparator();
        when(socketChannel.write(Mockito.any(ByteBuffer.class))).thenReturn(0);
        assertEquals(toReturn, userProfile.getOption(toBeAdd),
                "should write an error for invalid number of parameters");
        afterTest();
    }

    @Test
    public void generatePasswordTooMuchParametersTest() throws IOException {
        String toBeAdd = "generate-password dalivali.bg second oneMore";
        String toReturn = "Invalid number of parameters!" + System.lineSeparator();
        when(socketChannel.write(Mockito.any(ByteBuffer.class))).thenReturn(0);
        assertEquals(toReturn, userProfile.getOption(toBeAdd),
                "should write an error for invalid number of parameters");
        afterTest();
    }

    @Test
    public void removePasswordTooMuchParametersTest() throws IOException {
        String toBeAdd = "remove-password dalivali.bg second oneMore";
        String toReturn = "Invalid number of parameters!" + System.lineSeparator();
        when(socketChannel.write(Mockito.any(ByteBuffer.class))).thenReturn(0);
        assertEquals(toReturn, userProfile.getOption(toBeAdd),
                "should write an error for invalid number of parameters");
        afterTest();
    }

    @Test
    public void removePasswordInvalidParametersTest() throws IOException {
        String toBeAdd = "remove-password dalivali.bg";
        String toReturn = "Invalid number of parameters!" + System.lineSeparator();
        when(socketChannel.write(Mockito.any(ByteBuffer.class))).thenReturn(0);
        assertEquals(toReturn, userProfile.getOption(toBeAdd),
                "should write an error for invalid number of parameters");
        afterTest();
    }

    @Test
    public void removePasswordWebsiteNotInTheSystemTest() throws IOException {
        String add = "generate-password dalivali.bg second";
        userProfile.getOption(add);
        String toBeAdd = "remove-password bulgaria.bg second";
        String toReturn = "No registration available on this site!" + System.lineSeparator();
        when(socketChannel.write(Mockito.any(ByteBuffer.class))).thenReturn(0);
        assertEquals(toReturn, userProfile.getOption(toBeAdd),
                "should write an error for website not in the system");
        afterTest();
    }

    @Test
    public void removePasswordUsernameNotInTheSystemTest() throws IOException {
        String add = "generate-password dalivali.bg second";
        userProfile.getOption(add);
        String toBeAdd = "remove-password dalivali.bg third";
        String toReturn = "This username on this website is not in the system!" + System.lineSeparator();
        when(socketChannel.write(Mockito.any(ByteBuffer.class))).thenReturn(0);
        assertEquals(toReturn, userProfile.getOption(toBeAdd),
                "should write an error for username not in the system");
        afterTest();
    }

    @Test
    public void removePasswordUsernameDoesntHavePasswordTest() throws IOException {
        String add = "generate-password dalivali.bg second";
        userProfile.getOption(add);
        String toBeAdd = "remove-password dalivali.bg second";
        userProfile.getOption(toBeAdd);
        String toReturn = "This account doesn't have password!" + System.lineSeparator();
        when(socketChannel.write(Mockito.any(ByteBuffer.class))).thenReturn(0);
        assertEquals(toReturn, userProfile.getOption(toBeAdd),
                "should write an error for username not in the system");
        afterTest();
    }

    @Test
    public void removePasswordAllCorrectTest() throws IOException {
        String add = "generate-password dalivali.bg second";
        userProfile.getOption(add);
        String toBeAdd = "remove-password dalivali.bg second";
        String toReturn = "Password was successfully removed!" + System.lineSeparator();
        when(socketChannel.write(Mockito.any(ByteBuffer.class))).thenReturn(0);
        assertEquals(toReturn, userProfile.getOption(toBeAdd),
                "should write a successful message");
        afterTest();
    }

    @Test
    public void showEmptyTest() throws IOException {
        File file = new File(account.username() + ".txt");
        file.delete();
        userProfile = new UserProfile(account, socketChannel);
        String toBeAdd = "show";
        String toReturn = "No registered websites and accounts!" + System.lineSeparator();
        when(socketChannel.write(Mockito.any(ByteBuffer.class))).thenReturn(0);
        assertEquals(toReturn, userProfile.getOption(toBeAdd),
                "should write an error for no registered accounts not in the system, yet");
        afterTest();
    }

    @Test
    public void showAccountsTest() throws IOException {
        String add = "generate-password dalivali.bg second";
        userProfile.getOption(add);
        String toBeAdd = "remove-password dalivali.bg second";
        userProfile.getOption(toBeAdd);
        String show = "show";
        String toReturn = "dalivali.bg" + System.lineSeparator() +
                "koko : password='my123New123Password321  OK" + System.lineSeparator() +
                "second : This account has no password saved!" + System.lineSeparator()
                + System.lineSeparator();
        when(socketChannel.write(Mockito.any(ByteBuffer.class))).thenReturn(0);
        assertEquals(toReturn, userProfile.getOption(show),
                "should write a successful message");
        afterTest();
    }

    @Test
    public void logoutTest() throws IOException {
        String action = "logout";
        when(socketChannel.write(Mockito.any(ByteBuffer.class))).thenReturn(0);
        assertThrows(DisconnectOrLogoutException.class, () -> userProfile.getOption(action),
                "Should throw exception for logout");
    }

    @Test
    public void disconnectTest() throws IOException {
        String action = "disconnect";
        when(socketChannel.write(Mockito.any(ByteBuffer.class))).thenReturn(0);
        assertThrows(DisconnectOrLogoutException.class, () -> userProfile.getOption(action),
                "Should throw exception for logout");
    }

    @Test
    public void changePasswordTooMuchParametersTest() throws IOException {
        String action = "change-server-password koko 1655656fdg dfgsdfgsf";
        String expectedMessage = "Invalid number of parameters!" + System.lineSeparator();
        when(socketChannel.write(Mockito.any(ByteBuffer.class))).thenReturn(0);
        assertEquals(expectedMessage, userProfile.getOption(action),
                "Should throw exception for wrong number of parameters");
    }

    @Test
    public void changePasswordTooLittleParametersTest() throws IOException {
        String action = "change-server-password myNewPassword";
        String expectedMessage = "Invalid number of parameters!" + System.lineSeparator();
        when(socketChannel.write(Mockito.any(ByteBuffer.class))).thenReturn(0);
        assertEquals(expectedMessage, userProfile.getOption(action),
                "Should throw exception for wrong number of parameters");
    }

    @Test
    public void changePasswordWrongCurrentPasswordParametersTest() throws IOException {
        String action = "change-server-password something 55gg66hh!%&";
        String expectedMessage = "You entered wrong current password!" + System.lineSeparator();
        when(socketChannel.write(Mockito.any(ByteBuffer.class))).thenReturn(0);
        assertEquals(expectedMessage, userProfile.getOption(action),
                "Should throw exception for wrong current password");
    }

    @Test
    public void changePasswordCompromisedTest() throws IOException {
        String action = "change-server-password " + account.password() + " 123";
        String expectedMessage = "Your password is not saved in our system, because it is compromised! " +
                System.lineSeparator() + "We recommend you to add a new one!" + System.lineSeparator();
        when(socketChannel.write(Mockito.any(ByteBuffer.class))).thenReturn(0);
        assertEquals(expectedMessage, userProfile.getOption(action),
                "Should throw exception for compromised password");
    }

    @Test
    public void changePasswordStrongPasswordTest() {
        String action = "change-server-password " + account.password() + " My%&NeW321!?Password";
        assertThrows(ChangePasswordException.class, () -> userProfile.getOption(action),
                "Should throw exception for logout");
    }

}
