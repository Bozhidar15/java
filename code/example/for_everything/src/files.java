import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class files {
    public static void main(String[] args) {
        Path path = Path.of("current.txt");
        Set<String> toSave = new HashSet<>();
        toSave.add("koko");
        toSave.add("bozhko");
        toSave.add("niki");
        try (var bufferedWriter = Files.newBufferedWriter(path)) {//overrides????
            for (var curr:toSave) {
                bufferedWriter.write(curr + ':' + curr + System.lineSeparator());
                bufferedWriter.flush();
            }
        } catch (IOException e) {
            throw new IllegalStateException("A problem occurred while saving accounts to a file", e);
        }
    }
}
