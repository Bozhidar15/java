package bg.sofia.uni.fmi.mjt.vault.password;

import java.security.SecureRandom;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;


public class GeneratePassword {
    private static Stream<Character> makeCombination(int length, int from, int to) {
        Stream<Character> streamToReturn;
        Random random = new SecureRandom();
        IntStream numberStream = random.ints(length, from, to);
        streamToReturn = numberStream.mapToObj(element -> (char) element);
        return streamToReturn;
    }

    public static String createNewPassword() {
        final int lengthOfAll = 5;
        final int intFrom = 48;
        final int intTo = 57;
        final int specialFrom = 33;
        final int specialTo = 45;
        Stream<Character> randomSymbols = Stream.concat(makeCombination(lengthOfAll, intFrom, intTo),
                Stream.concat(makeCombination(lengthOfAll, specialFrom, specialTo),
                        Stream.concat(makeCombination(lengthOfAll, 'a', 'z'),
                                makeCombination(lengthOfAll, 'A', 'Z'))));

        List<Character> listOfAllElements = new java.util.ArrayList<>(randomSymbols.toList());
        Collections.shuffle(listOfAllElements);


        return listOfAllElements.stream()
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .toString();
    }
}
