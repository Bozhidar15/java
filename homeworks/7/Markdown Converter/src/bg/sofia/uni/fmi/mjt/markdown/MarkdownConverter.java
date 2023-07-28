package bg.sofia.uni.fmi.mjt.markdown;

import java.io.*;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MarkdownConverter implements MarkdownConverterAPI {

    public MarkdownConverter() {

    }

    @Override
    public void convertMarkdown(Reader source, Writer output) {
        try (BufferedReader bufferedReader = new BufferedReader(source)) {
            String line;
            output.write("<html>");
            output.write("<body>");
            int check = 0;
            while ((line = bufferedReader.readLine()) != null) {
                StringBuilder newLine = new StringBuilder();
                int counter = 0;
                for (int i = 0; i < line.length(); i++) {
                    if (line.charAt(i) == '#') {
                        while (line.charAt(i) == '#') {
                            i++;
                            counter++;
                        }
                        i++;
                        newLine.append("<h").append(counter).append('>').append(line.substring(i))
                                    .append("</h").append(counter).append('>');
                        break;
                    } else if (line.charAt(i) == '*') {
                        if (line.charAt(i + 1) == '*') {
                            //bold
                            int start;
                            i += 2;
                            start = i;
                            while (line.charAt(i) != '*') {
                                i++;
                            }
                            newLine.append("<strong>").append(line.substring(start, i)).append("</strong>");
                            i++;
                            //continue;
                        } else {
                            //italic
                            int start;
                            i++;
                            start = i;
                            while (line.charAt(i) != '*') {
                                i++;
                            }
                            newLine.append("<em>").append(line.substring(start, i)).append("</em>");
                            //continue;
                        }
                    } else if (line.charAt(i) == '`') {
                        int start;
                        i++;
                        start = i;
                        while (line.charAt(i) != '`') {
                            i++;
                        }
                        newLine.append("<code>").append(line.substring(start, i)).append("</code>");
                    } else {
                        newLine.append(line.charAt(i));
                    }
                }
                newLine.append(System.lineSeparator());
                output.write(newLine.toString());
                output.flush();
            }
            output.write("</body>");
            output.flush();
            output.write("</html>");
            output.flush();
        } catch (IOException e) {
            throw new IllegalStateException("A problem occurred while reading from a file", e);
        }
    }

    @Override
    public void convertMarkdown(Path from, Path to) {
        try (Reader readerFrom = Files.newBufferedReader(from)) {
            try (Writer writerTo = Files.newBufferedWriter(to)) {
                if (!Files.exists(to) && !Files.isDirectory(to)) {
                    File targetFile = new File(to.toString());
                }
                convertMarkdown(readerFrom, writerTo);
            } catch (IOException e) {
                throw new IllegalStateException("A problem occurred while writing in file", e);
            }
        } catch (IOException e) {
            throw new IllegalStateException("A problem occurred while reading from a file", e);
        }
    }

    @Override
    public void convertAllMarkdownFiles(Path sourceDir, Path targetDir) {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(sourceDir, "*.md")) {
            for (Path file: stream) {
                final String newDir =  file.getFileName().toString().substring(0,
                        file.getFileName().toString().length() - 3) + ".html";
                Path newFilePath = targetDir.resolve(newDir);
                convertMarkdown(file, newFilePath);
            }
        } catch (IOException e) {
            throw new RuntimeException("Invalid source path", e);
        }
    }
}
