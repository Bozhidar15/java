package bg.sofia.uni.fmi.mjt.smartfridge;

import bg.sofia.uni.fmi.mjt.smartfridge.comparator.ItemByDateBothComparator;
import bg.sofia.uni.fmi.mjt.smartfridge.comparator.ItemByDateComparator;
import bg.sofia.uni.fmi.mjt.smartfridge.exception.FridgeCapacityExceededException;
import bg.sofia.uni.fmi.mjt.smartfridge.exception.InsufficientQuantityException;
import bg.sofia.uni.fmi.mjt.smartfridge.ingredient.DefaultIngredient;
import bg.sofia.uni.fmi.mjt.smartfridge.ingredient.Ingredient;
import bg.sofia.uni.fmi.mjt.smartfridge.recipe.Recipe;
import bg.sofia.uni.fmi.mjt.smartfridge.storable.Storable;

import java.util.*;

public class SmartFridge implements SmartFridgeAPI {

    private int capacity = 0;
    private int usedSpace = 0;

    List<DefaultIngredient<?>> insideTheFridge;

    public SmartFridge(int totalCapacity) {
        capacity = totalCapacity;
        insideTheFridge = new LinkedList<>();
    }

    @Override
    public <E extends Storable> void store(E item, int quantity) throws FridgeCapacityExceededException {
        if (item == null || quantity <= 0) {
            throw new IllegalArgumentException("item is null or quantity is not positive");
        } else if (usedSpace + quantity > capacity) {
            throw new FridgeCapacityExceededException("there is no free space in the fridge");
        }
        DefaultIngredient<?> temp = new DefaultIngredient<>(item, quantity);
        insideTheFridge.add(temp);
        usedSpace += quantity;
    }

    @Override
    public List<? extends Storable> retrieve(String itemName) {
        if (itemName == null || itemName.isBlank() || itemName.isEmpty()) {
            throw new IllegalArgumentException("invalid item name");
        }
        List<Storable> toReturn = new ArrayList<>();
        boolean check = true;
        for (int i = 0; i < insideTheFridge.size(); i++) {
            DefaultIngredient<?> e = insideTheFridge.get(i);
            if (e.item().getName().equals(itemName)) {
                for (int z = 0; z < e.quantity(); z++) {
                    toReturn.add(e.item());
                }
                check = false;
                usedSpace -= e.quantity();
                insideTheFridge.remove(e);
                i--;
            }
        }
        if (check) {
            return new ArrayList<>(0);
        }
        toReturn.sort(new ItemByDateComparator());
        return toReturn;
    }

    @Override
    public List<? extends Storable> retrieve(String itemName, int quantity) throws InsufficientQuantityException {
        if (itemName == null || itemName.isBlank() || itemName.isEmpty() || quantity <= 0) {
            throw new IllegalArgumentException("invalid arguments (item name or quantity)");
        }
        List<DefaultIngredient<?>> allOfTypeItemName = new LinkedList<>();
        List<Storable> toReturn = new LinkedList<>();
        int sizeOfAllQuantity = 0;
        for (DefaultIngredient<?> e : insideTheFridge) {
            if (e.item().getName().equals(itemName)) {
                allOfTypeItemName.add(e);
                sizeOfAllQuantity += e.quantity();
            }
        }
        if (sizeOfAllQuantity < quantity) {
            throw new InsufficientQuantityException("There are no such an elements with " +
                    "that kind of name or not enough quantity");
        } else {
            allOfTypeItemName.sort(new ItemByDateBothComparator());
        }
        boolean end = false;
        for (int p = 0; p < allOfTypeItemName.size(); p++) {
            DefaultIngredient<?> e = allOfTypeItemName.get(p);
            int counter = 0;
            for (int i = 0; i < e.quantity(); i++) {
                counter++;
                quantity--;
                toReturn.add(e.item());
                if (quantity == 0) {
                    if (e.quantity() - counter == 0) {
                        usedSpace -= e.quantity();
                        insideTheFridge.remove(e);
                    } else {
                        DefaultIngredient<?> temp = new DefaultIngredient<>(e.item(), e.quantity() - counter);
                        usedSpace -= counter;
                        insideTheFridge.remove(e);
                        insideTheFridge.add(temp);
                    }
                    end = true;
                    break;
                }
            }
            if (end) {
                break;
            }
            usedSpace -= e.quantity();
            insideTheFridge.remove(e);
            p--;
        }
        return toReturn;
    }

    @Override
    public int getQuantityOfItem(String itemName) {
        if (itemName == null || itemName.isBlank() || itemName.isEmpty()) {
            throw new IllegalArgumentException("invalid name");
        }
        int sizeCount = 0;
        for (DefaultIngredient<?> e : insideTheFridge) {
            if (e.item().getName().equals(itemName)) {
                sizeCount += e.quantity();
            }
        }
        return sizeCount;
    }

    @Override
    public Iterator<Ingredient<? extends Storable>> getMissingIngredientsFromRecipe(Recipe recipe) {
        if (recipe == null) {
            throw new IllegalArgumentException("recipe is invalid ");
        }

        Set<Ingredient<?>> fromRecipe = recipe.getIngredients();
        List<Ingredient<?>> needed = new ArrayList<>();

        for (Ingredient<?> current : fromRecipe) {
            int counter = 0;
            for (DefaultIngredient<?> inFridge : insideTheFridge) {
                if (inFridge.item().getName().equals(current.item().getName()) && !inFridge.item().isExpired()) {
                    counter += inFridge.quantity();
                    if (counter >= current.quantity()) {
                        break;
                    }
                }
            }
            if (counter < current.quantity()) {
                Ingredient<?> temp = new DefaultIngredient<>(current.item(), current.quantity() - counter );
                needed.add(temp);
            }
        }
        return needed.iterator();
    }

    @Override
    public List<? extends Storable> removeExpired() {
        List<Storable> allExpired = new ArrayList<>();
        for (int i = 0; i < insideTheFridge.size(); i++) {
            DefaultIngredient<?> e = insideTheFridge.get(i);
            if (e.item().isExpired()) {
                for (int j = 0; j < e.quantity(); j++) {
                    allExpired.add(e.item());
                }
                usedSpace -= e.quantity();
                insideTheFridge.remove(e);
                i--;
            }
        }
        return allExpired;
    }
}
