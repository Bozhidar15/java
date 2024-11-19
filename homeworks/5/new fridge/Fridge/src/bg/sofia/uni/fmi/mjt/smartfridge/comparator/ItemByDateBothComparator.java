package bg.sofia.uni.fmi.mjt.smartfridge.comparator;

import java.util.Comparator;

import bg.sofia.uni.fmi.mjt.smartfridge.ingredient.DefaultIngredient;
import bg.sofia.uni.fmi.mjt.smartfridge.storable.Storable;

public class ItemByDateBothComparator implements Comparator<DefaultIngredient<?>> {

    @Override
    public int compare(DefaultIngredient<?> first, DefaultIngredient<?> second) {
        return first.item().getExpiration().compareTo(second.item().getExpiration());
    }

}