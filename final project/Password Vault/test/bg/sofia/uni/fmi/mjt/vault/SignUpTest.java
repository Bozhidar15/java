package bg.sofia.uni.fmi.mjt.vault;

import bg.sofia.uni.fmi.mjt.vault.exception.NoValidOperationException;
import bg.sofia.uni.fmi.mjt.vault.exception.ShutDownServerException;
import bg.sofia.uni.fmi.mjt.vault.server.AccountsForServer;
import bg.sofia.uni.fmi.mjt.vault.server.ServerSignUpMessages;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class SignUpTest {

    private AccountsForServer accountsForServer;

    private ServerSignUpMessages serverSignUpMessages;

    private String send;

    @Mock
    private SocketChannel socketChannel;

    @BeforeEach
    public void before() throws IOException {
        accountsForServer = new AccountsForServer();
        serverSignUpMessages = new ServerSignUpMessages(socketChannel, accountsForServer);

    }

    @Test
    public void testChangePasswordAllCorrect() {
        final int len = 7;
        byte[] arr = new byte[len];
        new Random().nextBytes(arr);
        String generated = new String(arr, Charset.forName("UTF-8"));
        send = "register " + generated + " $123#654!? $123#654!?";
        serverSignUpMessages.getOption(send);
        send = "change-password " + generated + " $123#654!? $843&#?!734";
        assertTrue(serverSignUpMessages.changePassword(send),
                "Should return true because current user accomplish changing-password");
        send = "change-password " + generated + " $843&#?!734 $123#654!?";
        assertTrue(serverSignUpMessages.changePassword(send),
                "Should return true because current user accomplish changing-password");
    }

    @Test
    public void testInvalidOperation() {
        send = "what to do";
        assertThrows(NoValidOperationException.class, () -> serverSignUpMessages.getOption(send),
                "Should throw exception for not valid operation");

    }

    @Test
    public void testShowAllOptions() {
        send = "/?";
        assertFalse(serverSignUpMessages.getOption(send),
                "Should return false because current couldn't user accomplish registration or login");

    }

    @Test
    public void testRegisterNewAccountWithInvalidNumberOfParametersTooMuch() {
        send = "register bozhko 123456 123456 someMore";
        assertFalse(serverSignUpMessages.getOption(send),
                "Should return false because current couldn't user accomplish registration or login");

    }

    @Test
    public void testRegisterNewAccountWithInvalidNumberOfParametersTooLittle() {
        send = "register bozhko 123456";
        assertFalse(serverSignUpMessages.getOption(send),
                "Should return false because current couldn't user accomplish registration or login");

    }

    @Test
    public void testRegisterNewAccountWithNotEqualPasswords() {
        send = "register bozhko 123456 12349999";
        assertFalse(serverSignUpMessages.getOption(send),
                "Should return false because current couldn't user accomplish registration or login");

    }

    @Test
    public void testRegisterNewAccountWithAlreadyTakenUsername() {
        send = "register bozhidar 123456 123456";
        assertFalse(serverSignUpMessages.getOption(send),
                "Should return false because current couldn't user accomplish registration or login");

    }

    @Test
    public void testRegisterNewAccountWithCompromisedPassword() {
        send = "register gery 123456 123456";
        assertFalse(serverSignUpMessages.getOption(send),
                "Should return false because current couldn't user accomplish registration or login");

    }

    @Test
    public void testRegisterNewAccountAllCorrect() {
        final int len = 7;
        byte[] arr = new byte[len];
        new Random().nextBytes(arr);
        String generated = new String(arr, Charset.forName("UTF-8"));
        send = "register " + generated + " $123#654!? $123#654!?";
        assertTrue(serverSignUpMessages.getOption(send),
                "Should return false because current couldn't user accomplish registration or login");

    }

    @Test
    public void testLoginWithTooMuchParameters() {
        send = "login gery $123#654!? $123#654!?";
        assertFalse(serverSignUpMessages.getOption(send),
                "Should return false because current couldn't user accomplish registration or login");

    }

    @Test
    public void testLoginWithTooLittleParameters() {
        send = "login gery";
        assertFalse(serverSignUpMessages.getOption(send),
                "Should return false because current couldn't user accomplish registration or login");

    }

    @Test
    public void testLoginWithNotExistingUsername() {
        send = "login gerY $123#654!?";
        assertFalse(serverSignUpMessages.getOption(send),
                "Should return false because current couldn't user accomplish registration or login");

    }

    @Test
    public void testLoginWithWrongPassword() {
        final int len = 7;
        byte[] arr = new byte[len];
        new Random().nextBytes(arr);
        String generated = new String(arr, Charset.forName("UTF-8"));
        send = "register " + generated + " $123#654!? $123#654!?";
        serverSignUpMessages.getOption(send);
        send = "login " + generated + "randomPassword";
        assertFalse(serverSignUpMessages.getOption(send),
                "Should return false because current couldn't user accomplish registration or login");

    }

    @Test
    public void testLoginAllCorrect() {
        send = "register gery $123#654!? $123#654!?";
        serverSignUpMessages.getOption(send);
        send = "login gery $123#654!?";
        assertTrue(serverSignUpMessages.getOption(send),
                "Should return false because current couldn't user accomplish registration or login");

    }

    @Test
    public void testChangePasswordWithTooMuchParameters() {
        send = "change-server-password gery $123#654!? 6556  51616516514";
        assertFalse(serverSignUpMessages.changePassword(send),
                "Should return false because current couldn't user accomplish registration or login");

    }

    @Test
    public void testChangePasswordWithTooLittleParameters() {
        send = "change-server-password gery";
        assertFalse(serverSignUpMessages.changePassword(send),
                "Should return false because current couldn't user accomplish registration or login");

    }

    @Test
    @Disabled
    public void testChangePasswordWithNoMatchingUsername() {
        send = "change-password gerY $123#654!? 51616516514";
        assertFalse(serverSignUpMessages.getOption(send),
                "Should return false because current couldn't user accomplish registration or login");

    }

    @Test
    @Disabled
    public void testChangePasswordWithWrongOldPassword() {
        send = "change-password gery $123#654!?++ 51616516514";
        assertFalse(serverSignUpMessages.getOption(send),
                "Should return false because current couldn't user accomplish registration or login");

    }

    @Test
    @Disabled
    public void testChangePasswordWithCompromisedNewPassword() {
        send = "change-password gery $123#654!? 123";
        assertFalse(serverSignUpMessages.getOption(send),
                "Should return false because current couldn't user accomplish registration or login");

    }



    @Test
    public void testDisconnect() {
        send = "disconnect";
        assertFalse(serverSignUpMessages.getOption(send),
                "Should return false because current disconnect");

    }

    @Test
    public void testProperlySutDownServer() {
        send = "save_disconnect_server";
        assertThrows(ShutDownServerException.class, () -> serverSignUpMessages.getOption(send),
                "Should return false because current disconnect");

    }
}
