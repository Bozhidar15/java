package bg.sofia.uni.fmi.mjt.smartfridge.comparator;

import java.util.Comparator;
import bg.sofia.uni.fmi.mjt.smartfridge.storable.Storable;

public class ItemByDateComparator implements Comparator<Storable> {

    @Override
    public int compare(Storable first, Storable second) {
        return first.getExpiration().compareTo(second.getExpiration());
    }

}