package bg.sofia.uni.fmi.mjt.mail;

import bg.sofia.uni.fmi.mjt.mail.exceptions.AccountAlreadyExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class OutlookAddAccountTest {

    Outlook testOutlook;

    @BeforeEach
    void create () {
        testOutlook = new Outlook();
    }

    @Test
    void testAddingAccountWhenNameIsEmpty() {
        assertThrows(IllegalArgumentException.class, () -> testOutlook.addNewAccount("","fgdf"),
                "name is empty");
    }

    @Test
    void testAddingAccountWhenNameIsBlank() {
        assertThrows(IllegalArgumentException.class, () -> testOutlook.addNewAccount(" ","fgdf"),
                "name is blank");
    }

    @Test
    void testAddingAccountWhenEmailIsEmpty() {
        assertThrows(IllegalArgumentException.class, () -> testOutlook.addNewAccount("Bozhidar",""),
                "email is empty");
    }

    @Test
    void testAddingAccountWhenEmailIsBlank() {
        assertThrows(IllegalArgumentException.class, () -> testOutlook.addNewAccount("Bozhidar"," "),
                "email is blank");
    }

    @Test
    void testAddingAccountWhenThereIsNoSuch() {
        testOutlook.addNewAccount("Bozhidar", "boe@gmail.com");
        assertThrows(AccountAlreadyExistsException.class, () -> testOutlook.addNewAccount("Bozhidar",
                "bojidaruse@gmail.com"), "same name exist");
    }

    @Test
    //@Disabled
    void testAddingAccountWhenThereIsTheSameEmail() {
        testOutlook.addNewAccount("Pavel", "bojidaruse@gmail.com");
        assertThrows(AccountAlreadyExistsException.class, () -> testOutlook.addNewAccount("Bozhidar",
                "bojidaruse@gmail.com"));
    }

    @Test
    void testAddingAccountWhenThereIsOne() {
        assertEquals("Bozhidar",  testOutlook.addNewAccount("Bozhidar",
                "bojidaruse@gmail.com").name(), "name is not the same");
    }


}
