package bg.sofia.uni.fmi.mjt.myfitnesspal.diary;

import bg.sofia.uni.fmi.mjt.myfitnesspal.nutrition.NutritionInfo;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class FoodEntryTest {

    @Test
    public void testFoodEntryWhenFoodNameIsNull() {
        assertThrows(IllegalArgumentException.class, () -> new FoodEntry(null, 2,
                new NutritionInfo(20, 30, 50)));
    }

    @Test
    public void testFoodEntryWhenFoodNameIsBlank() {
        assertThrows(IllegalArgumentException.class, () -> new FoodEntry("", 2,
                new NutritionInfo(20, 30, 50)));
    }

    @Test
    public void testFoodEntryWhenNutritionIsNull() {
        assertThrows(IllegalArgumentException.class, () -> new FoodEntry("pasta", 2, null));
    }

    @Test
    public void testFoodEntryWhenServiceSizeIsNegative() {
        assertThrows(IllegalArgumentException.class, () -> new FoodEntry("pasta", -2,
                new NutritionInfo(20, 30, 50)));
    }
}
