package bg.sofia.uni.fmi.mjt.markdown;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import java.io.*;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;

//@ExtendWith(MockitoExtension.class)
public class EditTextTest {

    private MarkdownConverter markdown = new MarkdownConverter();

    File enterFile;
    File returnFile;
    Path enterPath;
    Path returnPath;
    @TempDir
    Path temp;

    @BeforeEach
    void createFiles(){
        try {
            enterPath = temp.resolve( "temp.md" );
            returnPath = temp.resolve( "temp.html" );
        }
        catch( InvalidPathException e) {
            throw new IllegalArgumentException("Invalid path", e);
        }
        enterFile = enterPath.toFile();
        returnFile = returnPath.toFile();
    }

    @AfterEach
    void deleteFiles(){
        returnFile.delete();
        enterFile.delete();
    }
    private String forAllTests (String inside) {

        try (Writer enter = new FileWriter(enterPath.toString())) {
            enter.write(inside);//for every case
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try(BufferedReader read = new BufferedReader(new FileReader(
                enterPath.toString()))){
            try (Writer enter = new FileWriter(returnPath.toString())) {
                markdown.convertMarkdown(read, enter);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try (BufferedReader read = new BufferedReader(new FileReader(
                returnPath.toString()))){
            return read.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testEditHeading4() {
        assertEquals("<h4>Heading level 4</h4>", forAllTests("#### Heading level 4")
                .substring(12));
    }

    @Test
    public void testEditHeading6() {
        assertEquals("<h6>Heading level 6</h6>", forAllTests("###### Heading level 6")
                .substring(12));
    }

    @Test
    public void testEditBold() {
        assertEquals("I just love <strong>bold text</strong>.", forAllTests("I just love **bold text**.")
                .substring(12));
    }

    @Test
    public void testEditItalic1() {
        assertEquals("Italicized text is the <em>cat's meow</em>.", forAllTests(
                "Italicized text is the *cat's meow*.").substring(12));
    }

    @Test
    public void testEditCode() {
        assertEquals("Always <code>.close()</code> your streams", forAllTests(
                "Always `.close()` your streams").substring(12));
    }

    @Test
    public void testEditMixed() {
        assertEquals("<code>.close()</code> <em>your</em> <strong>eyes</strong>", forAllTests(
                "`.close()` *your* **eyes**").substring(12));
    }

    @Test
    public void testConvertFromPath1() {
        assertEquals("<code>.close()</code> <em>your</em> <strong>eyes</strong>", forAllTests(
                "`.close()` *your* **eyes**").substring(12));
    }
}
