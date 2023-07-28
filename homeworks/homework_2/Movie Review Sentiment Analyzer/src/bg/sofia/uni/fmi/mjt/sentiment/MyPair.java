package bg.sofia.uni.fmi.mjt.sentiment;

public class MyPair {
    private double sentimentScore = 0;
    private double countOfAllScores = 0;
    private double contains = 0;
    private final String word;

    MyPair(String word) {
        this.word = word;
    }

    public void add(double value) {
        contains++;
        countOfAllScores += value;
        sentimentScore = countOfAllScores / contains;
    }

    public double getSentimentScore() {
        return sentimentScore;
    }

    public double getContains() {
        return contains;
    }

    public String getWord() {
        return word;
    }
}
