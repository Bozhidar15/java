package bg.sofia.uni.fmi.mjt.smartfridge.recipe;

import bg.sofia.uni.fmi.mjt.smartfridge.ingredient.Ingredient;
import bg.sofia.uni.fmi.mjt.smartfridge.storable.Storable;

import java.util.*;

public class CurrentRecipe implements Recipe {

    Set<Ingredient<?>> ingredients;

    public CurrentRecipe(HashSet<Ingredient<?>> other) {
        ingredients = other;
    }

    @Override
    public Set<Ingredient<? extends Storable>> getIngredients() {
        return ingredients;
    }

    @Override
    public void addIngredient(Ingredient<? extends Storable> ingredient) {
        if (ingredient == null) {
            throw new IllegalArgumentException("Ingredient is null");
        }
        ingredients.add(ingredient);
    }

    @Override
    public void removeIngredient(String itemName) {
        if (itemName == null || itemName.isEmpty() || itemName.isBlank()) {
            throw new IllegalArgumentException("Given name is incorrect");
        }
        for (Ingredient<?> e : ingredients) {
            if (e.item().getName().equals(itemName)) {
                ingredients.remove(e);
                break;
            }
        }
    }
}
