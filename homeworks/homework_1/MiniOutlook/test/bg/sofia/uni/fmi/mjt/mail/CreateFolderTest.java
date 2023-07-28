package bg.sofia.uni.fmi.mjt.mail;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CreateFolderTest {
    Outlook testOutlook;

    @BeforeEach
    void create () {
        testOutlook = new Outlook();
        testOutlook.addNewAccount("Bozhidar", "bojidaruse@gmail.com");
    }

    @Test
    void testCreatingFolderWhenNameIsEmpty() {
        assertThrows(IllegalArgumentException.class, () -> testOutlook.createFolder("","fgdf"),
                "name is empty");
    }

    @Test
    void testCreatingFolderWhenNameIsBlank() {
        assertThrows(IllegalArgumentException.class, () -> testOutlook.createFolder(" ","fgdf"),
                "name is blank");
    }

    @Test
    void testCreatingFolderWhenEmailIsEmpty() {
        assertThrows(IllegalArgumentException.class, () -> testOutlook.createFolder("fgdf",""),
                "path is empty");
    }

    @Test
    void testCreatingFolderWhenEmailIsBlank() {
        assertThrows(IllegalArgumentException.class, () -> testOutlook.createFolder("asdgas"," "),
                "path is blank");
    }

    @Test
    void testAddingAccountWhenThereIsOne() {
        testOutlook.createFolder("Bozhidar",
                "/inbox/new");
        assertEquals("/new",  testOutlook.getUsers().get("Bozhidar").getFolders()
                .get("/inbox").get(0), "folder is not the same");
    }
}
