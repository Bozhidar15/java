package bg.sofia.uni.fmi.mjt.sentiment;

import java.io.Writer;
import java.io.Reader;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Comparator;

public class MovieReviewSentimentAnalyzer implements SentimentAnalyzer {

    private Map<String, MyPair> words;
    private Set<String> stopWordsSet;
    private Writer reviewsWriter;
    private double currentReviewRating;

    private void enterStopWordsSet(Reader stopwordsIn) {
        try (BufferedReader bufferedReader = new BufferedReader(stopwordsIn)) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stopWordsSet.add(line.toLowerCase());
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("A problem occurred while reading from a stopwordsIn file", e);
        }
    }

    private boolean validCharacter(String line, int i) {
        String getApostrophe = "p'";
        char apostrophe = getApostrophe.charAt(1);
        return i + 1 == line.length() || !((line.charAt(i) >= 'a' && line.charAt(i) <= 'z') ||
                (line.charAt(i) >= 'A' && line.charAt(i) <= 'Z') ||
                (line.charAt(i) >= '0' && line.charAt(i) <= '9') || line.charAt(i) == apostrophe);
    }
    private void valuatingWords(String line, double value, boolean check) {
        double countOfWords = 0;
        double finalSentimentalScore = 0;
        int start = 0;
        int end = 0;
        for (int i = 0; i < line.length(); i++) {
            if (validCharacter(line, i)) {
                end = i;
                if (end - start >= 2 ) {
                    String newWord;
                    if (i + 1 == line.length()) {
                        newWord = line.substring(start).toLowerCase();
                    } else {
                        newWord = line.substring(start, end).toLowerCase();
                    }

                    if (stopWordsSet.contains(newWord)) {

                    } else if (check && words.containsKey(newWord) ) {
                        countOfWords++;
                        finalSentimentalScore += words.get(newWord).getSentimentScore();
                    } else if (words.containsKey(newWord)) {
                        words.get(newWord).add(value);
                    } else {
                        MyPair curr = new MyPair(newWord);
                        curr.add(value);
                        words.put(newWord, curr);
                    }
                }
                start = i + 1;
            }
        }
        if (check) {
            if (countOfWords != 0) {
                currentReviewRating = finalSentimentalScore / countOfWords;
            } else {
                currentReviewRating = -1.0;
            }
        }
    }

    private void enterReviews(Reader reviewsIn) {
        try (BufferedReader bufferedReader = new BufferedReader(reviewsIn)) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String reviewText;
                reviewText = line.substring(2);
                double currentDouble = Double.parseDouble(String.valueOf(line.charAt(0)));
                valuatingWords(reviewText, currentDouble, false);
            }
        } catch (IOException e) {
            throw new IllegalStateException("A problem occurred while reading from a reviewsIn file", e);
        }
    }

    public MovieReviewSentimentAnalyzer(Reader stopwordsIn, Reader reviewsIn, Writer reviewsOut) {
        words = new HashMap<>();
        stopWordsSet = new HashSet<>();
        reviewsWriter = reviewsOut;
        enterStopWordsSet(stopwordsIn);
        enterReviews(reviewsIn);
    }

    @Override
    public double getReviewSentiment(String review) {
        valuatingWords(review, 0, true);
        return currentReviewRating;
    }

    @Override
    public String getReviewSentimentAsName(String review) {
        valuatingWords(review, 0, true);
        final double downBorder = 0.0;
        final double upBorder = 4.0;
        final double negative = 0.5;
        final double somewhatNegative = 1.5;
        final double neutral = 2.5;
        final double somewhatPositive = 3.5;
        if (currentReviewRating >= downBorder && currentReviewRating < negative) {
            return "negative";
        } else if (currentReviewRating >= negative && currentReviewRating < somewhatNegative) {
            return "somewhat negative";
        } else if (currentReviewRating >= somewhatNegative && currentReviewRating < neutral) {
            return "neutral";
        } else if (currentReviewRating >= neutral && currentReviewRating < somewhatPositive) {
            return "somewhat positive";
        } else if (currentReviewRating >= somewhatPositive && currentReviewRating <= upBorder) {
            return "positive";
        } else {
            return "unknown";
        }
    }

    @Override
    public double getWordSentiment(String word) {
        if (words.containsKey(word.toLowerCase())) {
            return words.get(word.toLowerCase()).getSentimentScore();
        } else {
            return -1.0;
        }
    }

    @Override
    public int getWordFrequency(String word) {
        if (words.containsKey(word.toLowerCase())) {
            return (int) words.get(word.toLowerCase()).getContains();
        } else {
            return 0;
        }
    }

    @Override
    public List<String> getMostFrequentWords(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("N can't be negative!");
        }
        return words.values().stream().sorted(Comparator.comparing(MyPair::getContains).reversed())
                .limit(n).map(MyPair::getWord).toList();
    }

    @Override
    public List<String> getMostPositiveWords(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("N can't be negative!");
        }
        return words.values().stream().sorted(Comparator.comparing(MyPair::getSentimentScore)
                .reversed()).limit(n).map(MyPair::getWord).toList();
    }

    @Override
    public List<String> getMostNegativeWords(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("N can't be negative!");
        }
        return words.values().stream().sorted(Comparator.comparing(MyPair::getSentimentScore))
                .limit(n).map(MyPair::getWord).toList();
    }

    @Override
    public boolean appendReview(String review, int sentiment) {
        final int sentimenLower = 0;
        final int sentimenMax = 4;
        if (review == null || review.isEmpty() || review.isBlank() || sentiment < sentimenLower
                || sentiment > sentimenMax) {
            throw new IllegalArgumentException("review or sentiment is invalid");
        }
        String toAdd = Integer.toString(sentiment) + ' ' + review + System.lineSeparator();
        try (var bufferedWriter = new BufferedWriter(reviewsWriter)) {
            bufferedWriter.write(toAdd);
            bufferedWriter.flush();
            valuatingWords(review, sentiment, false);
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    @Override
    public int getSentimentDictionarySize() {
        return words.size();
    }

    @Override
    public boolean isStopWord(String word) {
        return stopWordsSet.contains(word.toLowerCase());
    }
}
