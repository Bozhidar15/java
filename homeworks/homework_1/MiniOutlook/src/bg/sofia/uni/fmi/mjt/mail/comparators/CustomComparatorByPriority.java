package bg.sofia.uni.fmi.mjt.mail.comparators;
import bg.sofia.uni.fmi.mjt.mail.Rule;

import java.util.Comparator;
public class CustomComparatorByPriority implements Comparator<Rule> {

    @Override
    public int compare(Rule first, Rule second) {
        return Integer.compare(first.getPriority(), second.getPriority());
    }

}
