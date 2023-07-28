package bg.sofia.uni.fmi.mjt.escaperoom.room;

public record Review(int rating, String reviewText) {
public Review{
    if (rating<0||rating>10){
        throw new IllegalArgumentException("Rating in not from 0 - 10");
    }else if (reviewText==null||reviewText.length()>200){
        throw new IllegalArgumentException("reviewText is null or over 200 characters");
    }
}

    /*Потребителите могат да предоставят мнение за дадена стая, което се моделира от record-a Review(int rating,
     String reviewText), чиито компоненти са съответно рейтинг и текст на мнението.
    Review трябва да има компактен конструктор, който да валидира, че рейтингът е число в интервала [0, 10],
    а текстът на мнението не е null и дължината му не надхвърля 200 символа.
    При неуспешна валидация, конструкторът трябва да хвърля IllegalArgumentException.*/
}
