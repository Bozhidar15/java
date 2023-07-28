package bg.sofia.uni.fmi.mjt.markdown;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.*;
import java.nio.file.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AllMarkdownFilesTest {

    File file1;
    File file2;
    File file3;

    Path path1;

    Path path2;

    Path path3;

    @TempDir
    Path temp;
    private MarkdownConverter markdown = new MarkdownConverter();

    @BeforeEach
    void createFiles(){
        try {
            path1 = temp.resolve("temp1.md");
            path2 = temp.resolve("temp2.md");
            path3 = temp.resolve("temp3.md");
        } catch( InvalidPathException e) {
            throw new IllegalArgumentException("Invalid path", e);
        }
        file1 = path1.toFile();
        file2 = path2.toFile();
        file3 = path3.toFile();
        try (Writer enter = new FileWriter(path1.toString())) {
            enter.write("Italicized text is the *cat's meow*.");//for every case
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try (Writer enter = new FileWriter(path2.toString())) {
            enter.write("`.close()` *your* **eyes**");//for every case
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try (Writer enter = new FileWriter(path3.toString())) {
            enter.write("###### Heading level 6");//for every case
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @AfterEach
    void deleteFiles(){
        file1.delete();
        file2.delete();
        file3.delete();
    }

    private int allHtmlFiles(){
        int counter = 0;
        //Path sourceDir = Paths.get("D:\\СУ\\2ри курс\\java\\homeworks\\7\\try");
        markdown.convertAllMarkdownFiles(temp,temp);
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(temp, "*.html")) {
            for (Path file: stream) {
                if (file.getFileName().toString().equals("temp1.html") ||
                        file.getFileName().toString().equals("temp2.html") ||
                        file.getFileName().toString().equals("temp3.html")){
                    counter++;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return counter;
    }

    @Test
    public void testWithThreeFiles(){
        assertEquals(3, allHtmlFiles());
    }

    @Test
    public void testWithInvalidSourse(){
        assertThrows(RuntimeException.class, () -> markdown.convertAllMarkdownFiles(null,null));
    }

}
