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

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class AnalyzerWithoutReturnListTest {

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
                    4 This quiet , introspective and entertaining independent is worth seeking .\t
                    3 The great film aims to be lot funny , uplifting and moving , sometimes all at once .\t
                    2 At a brief 42 minutes , we need more X and less blab .\t
                    3 Uneven but a lot of fun .\t
                    3 The Spalding Gray equivalent of a teen gross-out aims less comedy .\t
                    1 There's a lot great deal of corny dialogue and preposterous moments to aims.\t
                    2 The result might look like Vulgar .\t
                    0 tree .\t
                    4 book .\t
                    1 horse ?\t""");
        } catch (IOException e) {
            throw new RuntimeException("Error in entering text in reviewFile", e);
        }

        try (Writer enter = new FileWriter(stopWordsPath.toString())) {
            enter.write("""
                    a
                    at
                    but
                    am
                    an
                    and
                    any
                    is
                    of
                    to
                    be
                    have
                    this
                    all
                    the
                    it
                    you
                    for
                    we
                    more""");
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
    void testGetMostFrequentWordsInvalid() {
        assertThrows(IllegalArgumentException.class,
                () -> analyzer.getMostFrequentWords(-1),
                "Count cannot be negative");
    }

    @Test
    void testGetMostNegativeWordsInvalid() {
        assertThrows(IllegalArgumentException.class,
                () -> analyzer.getMostNegativeWords(-1),
                "Count cannot be negative");
    }

    @Test
    void testGetMostPositiveWordsInvalid() {
        assertThrows(IllegalArgumentException.class,
                () -> analyzer.getMostPositiveWords(-1),
                "Count cannot be negative");
    }

    @Test
    void testWithInvalidReviewNull() {
        final int sentNumber = 3;
        assertThrows(IllegalArgumentException.class, () -> analyzer.appendReview(null, sentNumber ),
                "Append a review when review is null");
    }

    @Test
    void testWithInvalidReviewBlank() {
        final int sentNumber = 3;
        assertThrows(IllegalArgumentException.class, () -> analyzer.appendReview(" ", sentNumber),
                "Append a review when review is blank");
    }

    @Test
    void testWithInvalidReviewEmpty() {
        final int sentNumber = 3;
        assertThrows(IllegalArgumentException.class, () -> analyzer.appendReview("", sentNumber),
                "Append a review when review is empty");
    }

    @Test
    void testWithInvalidSentimentalScoreDown() {
        final int sentWrongNumber = -1;
        assertThrows(IllegalArgumentException.class, () -> analyzer.appendReview("Bobi Borisov.", sentWrongNumber),
                "Append a review when review is empty");
    }

    @Test
    void testWithInvalidSentimentalScoreUp() {
        final int sentNumber = 5;
        assertThrows(IllegalArgumentException.class, () -> analyzer.appendReview("Bobi Borisov.", sentNumber),
                "Append a review when review is empty");
    }

    @Test
    void testReviewSentimentWithNotExistedReview() {
        final double delta = 0.01;
        assertEquals(-1.0, analyzer.getReviewSentiment("Bozhidar made this .\t"), delta,
                "test with not existed review failed");
    }

    @Test
    void testReviewSentimentWithReviewKeyLot() {
        final double delta = 0.01;
        final double expected = 2.77;
        assertEquals(expected, analyzer.getReviewSentiment("Uneven but a lot of fun .\t"), delta,
                "Review is not equal");
    }

    @Test
    void testReviewSentimentWithReviewKeyLess() {
        final double delta = 0.01;
        final double expected = 2.08;
        assertEquals(expected, analyzer.getReviewSentiment("At a brief 42 minutes ," +
                " we need more X and less blab .\t"), delta, "Review is not equal");
    }

    @Test
    void testReviewSentimentAsNameWithReviewKeyLot() {
        assertEquals("somewhat positive", analyzer.getReviewSentimentAsName("Uneven but a lot of fun .\t"),
                "Review is not equal");
    }

    @Test
    void testReviewSentimentAsNameWithReviewKeyOnlyLot() {
        assertEquals("neutral", analyzer.getReviewSentimentAsName(" lot"),
                "Review is not equal");
    }

    @Test
    void testReviewSentimentAsNameWithReviewKeyLotAndIntrospective() {
        final double delta = 0.01;
        final double expected = 3.16;
        assertEquals(expected, analyzer.getReviewSentiment(" lot !introspective"),
                delta, "Review is not equal");
    }

    @Test
    void testReviewSentimentAsNameWithReviewKeyTree() {
        assertEquals("negative", analyzer.getReviewSentimentAsName("tree .\t"),
                "Review is not equal");
    }

    @Test
    void testGetReviewSentimentAsNamePositive() {
        assertEquals("neutral", analyzer.getReviewSentimentAsName("You like it @? very !"),
                "neutral expected");
    }

    @Test
    void testReviewSentimentAsNameWithReviewKeyBook() {
        assertEquals("positive", analyzer.getReviewSentimentAsName("book .\t"),
                "Review is not equal");
    }

    @Test
    void testReviewSentimentAsNameWithReviewKeyHorse() {
        assertEquals("somewhat negative", analyzer.getReviewSentimentAsName("horse ?\t"),
                "Review is not equal");
    }

    @Test
    void testReviewSentimentAsNameWithReviewKeyUnknown() {
        assertEquals("unknown", analyzer.getReviewSentimentAsName("Pggg .\t"),
                "Review is not equal");
    }

    @Test
    void testReviewSentimentAsNameWithReviewKeyLess() {
        assertEquals("neutral", analyzer.getReviewSentimentAsName("At a brief 42 minutes ," +
                " we need more X and less blab .\t"), "Review is not equal");
    }

    @Test
    void testWordSentimentAims() {
        final double delta = 0.01;
        final double expected = 2.33;
        assertEquals(expected, analyzer.getWordSentiment("aims"),
                delta, "Sentiment of word aims is not right");
    }

    @Test
    void testNotExistedWordSentiment() {
        final double delta = 0.01;
        final double expected = -1.0;
        assertEquals(expected, analyzer.getWordSentiment("Bozhidar"),
                delta, "test with incorrect word failed");
    }

    @Test
    void testWordSentimentResultRight() {
        final double delta = 0.01;
        final double expected = 2.0;
        assertEquals(expected, analyzer.getWordSentiment("result"),
                delta, "Sentiment of word result is not right");
    }

    @Test
    void testWordSentimentResultWrong() {
        final double delta = 0.01;
        final double expected = 2.0;
        assertEquals(expected, analyzer.getWordSentiment("reSult"),
                delta, "Sentiment of word result is not right");
    }

    @Test
    void testWordFrequencyRight() {
        final int expected = 2;
        assertEquals(expected, analyzer.getWordFrequency("great"),
                "Frequency of word great is not right");
    }

    @Test
    void testWordFrequencyUnknown() {
        final int expected = 0;
        assertEquals(expected, analyzer.getWordFrequency("train"),
                "Frequency of word great is not right");
    }

    @Test
    void testWordFrequencyWrong() {
        final int expected = 2;
        assertEquals(expected, analyzer.getWordFrequency("greAt"),
                "Frequency of word great is not right");
    }

    @Test
    void testIsTheWordBeStopwordRight() {
        assertTrue(analyzer.isStopWord("be"), "be is a stopword");
    }

    @Test
    void testIsTheWordBeStopwordWrong() {
        assertTrue(analyzer.isStopWord("bE"), "be is a stopword");
    }

    @Test
    void testIsTheWordForStopwordRight() {
        assertTrue(analyzer.isStopWord("for"), "for is a stopword");
    }

    @Test
    void testIsTheWordForStopwordWrong() {
        assertTrue(analyzer.isStopWord("For"), "for is a stopword");
    }

    @Test
    void testIsTheWordForStopwordFalse() {
        assertFalse(analyzer.isStopWord("double"), "double is a stopword");
    }

    @Test
    void testDictionarySize() {
        final int expected = 44;
        assertEquals(expected, analyzer.getSentimentDictionarySize(),
                "Dictionary size is wrong");
    }
}
