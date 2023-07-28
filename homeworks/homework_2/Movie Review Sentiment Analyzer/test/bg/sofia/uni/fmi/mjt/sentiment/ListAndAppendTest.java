package bg.sofia.uni.fmi.mjt.sentiment;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.Reader;
import java.io.Writer;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;

import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ListAndAppendTest {

    File stopWordsFile;

    File reviewsFile;

    Path stopWordsPath;

    Path reviewsPath;

    MovieReviewSentimentAnalyzer analyzer;

    Reader stopwordsReader;

    Reader reviewsReader;

    Writer reviewsWriter;

    @TempDir
    Path temp;

    @BeforeEach
    public void createFiles() {

        try {
            stopWordsPath = temp.resolve("stopwords.txt");
            reviewsPath = temp.resolve("movieReviews.txt");
        } catch ( InvalidPathException e) {
            throw new RuntimeException("Invalid path", e);
        }

        stopWordsFile = stopWordsPath.toFile();
        reviewsFile = reviewsPath.toFile();

        try (Writer enter = new FileWriter(reviewsPath.toString())) {
            enter.write("""
                    4 Today we celebrate new Year! \t
                    3 My new name is Bozhidar .\t
                    1 We celebrate my new cake which is delicious .\t""");
        } catch (IOException e) {
            throw new RuntimeException("Error in entering text in reviewFile", e);
        }

        try (Writer enter = new FileWriter(stopWordsPath.toString())) {
            enter.write("""
                   we
                   which
                   is""");
        } catch (IOException e) {
            throw new RuntimeException("Error in entering text in stopwordsFile", e);
        }

        try {
            stopwordsReader = new FileReader(stopWordsPath.toString());
            reviewsReader = new FileReader(reviewsPath.toString());
            reviewsWriter = new FileWriter(reviewsPath.toString(), true);
        }
        catch (IOException e) {
            throw new RuntimeException("Error opening files", e);
        }

        analyzer = new MovieReviewSentimentAnalyzer(stopwordsReader, reviewsReader , reviewsWriter);
    }

    @AfterEach
    void deleteFiles() {
        try {
            reviewsReader.close();
            stopwordsReader.close();
            reviewsWriter.close();
            reviewsFile.delete();
            stopWordsFile.delete();
        } catch (IOException e ) {
            throw new RuntimeException("Error closing files", e);
        }
    }

    @Test
    void testListFrequency() {
        List<String> newList = new LinkedList<>();
        final int number = 3;
        newList.add("new");
        newList.add("celebrate");
        newList.add("my");
        assertIterableEquals(newList, analyzer.getMostFrequentWords(number),
                "List have two elements which are not the same");
    }

    @Test
    void testAddToFrequencyList() {
        final int number = 3;
        assertThrows(UnsupportedOperationException.class, () -> analyzer.getMostFrequentWords(number).add("gg"),
                "List should be unmodifiable");
    }

    @Test
    void testListMostPositiveWords() {
        List<String> newList = new LinkedList<>();
        final int number = 4;
        newList.add("year");
        newList.add("today");
        newList.add("bozhidar");
        newList.add("name");
        assertIterableEquals(newList, analyzer.getMostPositiveWords(number),
                "List have two elements which are not the same");
    }

    @Test
    void testAddToMostPositiveWordsList() {
        final int number = 3;
        assertThrows(UnsupportedOperationException.class, () -> analyzer.getMostPositiveWords(number).add("gg"),
                "List should be unmodifiable");
    }

    @Test
    void testListMostNegativeWords() {
        List<String> newList = new LinkedList<>();
        final int number = 2;
        newList.add("cake");
        newList.add("delicious");
        assertIterableEquals(newList, analyzer.getMostNegativeWords(number),
                "List have two elements which are not the same");
    }

    @Test
    void testListMostNegativeWordsDefendantAskedNumber() {
        List<String> newList = new LinkedList<>();
        final int number = 4;
        newList.add("cake");
        newList.add("delicious");
        newList.add("my");
        newList.add("celebrate");
        assertIterableEquals(newList, analyzer.getMostNegativeWords(number),
                "List have two elements which are not the same");
    }

    @Test
    void testAddToMostNegativeWordsList() {
        final int number = 3;
        assertThrows(UnsupportedOperationException.class, () -> analyzer.getMostNegativeWords(number).add("gg"),
                "List should be unmodifiable");
    }

    @Test
    void testAppendReview() {
        final int number = 4;
        assertTrue(analyzer.appendReview("Koko is dancing .\t", number),
                "append a review should be successful");
        List<String> newList = new LinkedList<>();
        newList.add("dancing");
        newList.add("year");
        newList.add("today");
        newList.add("koko");
        assertIterableEquals(newList, analyzer.getMostPositiveWords(number),
                "List have two elements which are not the same");
    }
}
