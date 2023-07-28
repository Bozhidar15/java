import java.security.SecureRandom;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;


public class GeneratePassword {


    public static void main(String[] args) {
        String pass = createNewPassword();


        System.out.println("Password generated is:" + pass);
    }
    private static Stream<Character> makeCombination(int length, int from, int to) {
        Stream<Character> streamToReturn;
        Random random = new SecureRandom();
        IntStream numberStream = random.ints(length, from, to);
        streamToReturn = numberStream.mapToObj(a -> (char) a);
        return streamToReturn;
    }

    public static String createNewPassword() {
        // generate a string having 2 numbers, 2 special chars, 2 upper case letters, and 2 lower case letters
        final int lengthOfAll = 3;
        final int intFrom = 48;
        final int intTo = 57;
        final int specialFrom = 33;
        final int specialTo = 45;
        Stream<Character> randomSymbols = Stream.concat(makeCombination(lengthOfAll,intFrom,intTo),
                Stream.concat(makeCombination(lengthOfAll, specialFrom, specialTo),
                        Stream.concat(makeCombination(lengthOfAll, 'a', 'z'),
                                makeCombination(lengthOfAll, 'A', 'Z'))));

        //List<Character> listOfAllElements = randomSymbols.collect(Collectors.toList());
        //List<Character> listOfAllElements = randomSymbols.toList();
        List<Character> listOfAllElements = new java.util.ArrayList<>(randomSymbols.toList());
        Collections.shuffle(listOfAllElements);


        return listOfAllElements.stream()
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .toString();
    }
}