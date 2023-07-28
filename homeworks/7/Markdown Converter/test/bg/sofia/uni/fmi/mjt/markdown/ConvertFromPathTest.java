package bg.sofia.uni.fmi.mjt.markdown;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.*;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConvertFromPathTest {

    private MarkdownConverter markdown = new MarkdownConverter();

    File enterFile;
    Path enterPath;
    @TempDir
    Path temp;

    @BeforeEach
    void createFiles(){
        try {
            enterPath = temp.resolve( "temp.md" );
        }
        catch( InvalidPathException e) {
            throw new IllegalArgumentException("Invalid path", e);
        }
        enterFile = enterPath.toFile();
    }

    @AfterEach
    void deleteFiles(){
        enterFile.delete();
    }
    private String forAllPath(String inside){
        try (Writer enter = new FileWriter(enterPath.toString())) {
            enter.write(inside);//for every case
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Path pathTo = temp.resolve( "temp.html");
        markdown.convertMarkdown(enterPath, pathTo);
        try(BufferedReader read = new BufferedReader(new FileReader(
                pathTo.toString()))){
            return read.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testConvertFromPath1() {
        assertEquals("<code>.close()</code> <em>your</em> <strong>eyes</strong>", forAllPath(
                "`.close()` *your* **eyes**").substring(12));
    }

    @Test
    public void testEditBold() {
        assertEquals("I just love <strong>bold text</strong>.", forAllPath("I just love **bold text**.")
                .substring(12));
    }

    @Test
    public void testEditItalic1() {
        assertEquals("Italicized text is the <em>cat's meow</em>.", forAllPath(
                "Italicized text is the *cat's meow*.").substring(12));
    }

    @Test
    public void testWithInvalidDirectoryExpectedRuntime(){
        assertThrows(RuntimeException.class, () -> markdown.convertAllMarkdownFiles(enterPath, null));
    }
}
