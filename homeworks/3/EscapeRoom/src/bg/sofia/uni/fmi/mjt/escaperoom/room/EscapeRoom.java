package bg.sofia.uni.fmi.mjt.escaperoom.room;

import bg.sofia.uni.fmi.mjt.escaperoom.rating.Ratable;

import java.util.Arrays;
import java.util.Objects;

public class EscapeRoom implements Ratable {
    String name;
    Theme theme;
    Difficulty difficulty;
    int maxTimeToEscape;
    double priceToPlay;
    int maxReviewsCount;
    Review reviews[];
    int CountofReviews=0,ReviewsPut=0;
    double allRatings=0.0;

    boolean IsItFull=false;

    public EscapeRoom(String name, Theme theme, Difficulty difficulty, int maxTimeToEscape, double priceToPlay,
                      int maxReviewsCount){

        this.name=name;
        this.difficulty=difficulty;
        this.maxReviewsCount=maxReviewsCount;
        this.priceToPlay=priceToPlay;
        this.maxTimeToEscape=maxTimeToEscape;
        this.theme=theme;
        //check for maxReviewsCount
        reviews=new Review[maxReviewsCount];

    }
    /**
     * Returns the name of the escape room.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the difficulty of the escape room.
     */
    public Difficulty getDifficulty() {
        return difficulty;
    }

    /**
     * Returns the maximum time to escape the room.
     */
    public int getMaxTimeToEscape() {
        return maxTimeToEscape;
    }

    /**
     * Returns all user reviews stored for this escape room, in the order they have been added.
     */
    public Review[] getReviews() {
        if (IsItFull){
            Review newReviews[]=new Review[maxReviewsCount];
            int z=0;
            for (int i = ReviewsPut; i < maxReviewsCount; i++,z++) {
                newReviews[z]=reviews[i];
            }
            for (int i = 0; i < ReviewsPut; i++,z++) {
                newReviews[z]=reviews[i];
            }
            reviews=newReviews;
            return reviews;
        }else {
            if (ReviewsPut==0){
                return new Review[0];
            }
        }
        return Arrays.copyOfRange(reviews,0,ReviewsPut);
    }
    /**
     * Adds a user review for this escape room.
     * The platform keeps just the latest up to {@code maxReviewsCount} reviews and in case the capacity is full,
     * a newly added review would overwrite the oldest added one, so the platform contains
     * {@code maxReviewsCount} at maximum, at any given time. Note that, despite older reviews may have been
     * overwritten, the rating of the room averages all submitted review ratings, regardless of whether all reviews
     * themselves are still stored in the platform.
     *
     * @param review the user review to add.
     */
    public void addReview(Review review) {
        if (ReviewsPut==reviews.length){
            IsItFull=true;
            ReviewsPut=0;
        }
        reviews[ReviewsPut++]=review;
        CountofReviews++;
        allRatings += review.rating();
    }

    @Override
    public double getRating(){
        if (CountofReviews==0){
            return 0.0;}
        return allRatings/CountofReviews;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EscapeRoom that)) {
            return false;
        }
        return that.name.equals(name) &&
                Double.compare(that.priceToPlay, priceToPlay) == 0 &&
                maxTimeToEscape == that.maxTimeToEscape &&
                maxReviewsCount == that.maxReviewsCount &&
                theme.equals(that.theme) &&
                difficulty.equals(that.difficulty);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name,theme,difficulty, maxTimeToEscape, priceToPlay,maxReviewsCount);
    }

    @Override
    public String toString() {
        return "EscapeRoom{" +
                "name=" + name +
                ", theme=" + theme.toString() +
                ", difficulty=" + difficulty.toString() +
                ", maxTimeToEscape=" + maxTimeToEscape +
                ", priceToPlay=" + priceToPlay +
                ", maxReviewsCount=" + maxReviewsCount +
                '}';
    }
}
