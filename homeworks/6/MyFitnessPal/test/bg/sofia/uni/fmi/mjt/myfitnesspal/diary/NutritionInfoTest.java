package bg.sofia.uni.fmi.mjt.myfitnesspal.diary;

import bg.sofia.uni.fmi.mjt.myfitnesspal.nutrition.NutritionInfo;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class NutritionInfoTest {

    @Test
    public void testNutritionInfoAllisNot100() {
        assertThrows(IllegalArgumentException.class, () -> new NutritionInfo(20, 30, 30));
    }

    @Test
    public void testNutritionInfoCarbohydratesAreNegative() {
        assertThrows(IllegalArgumentException.class, () -> new NutritionInfo(-20, 50, 30),
                "Carbohydrates must be positive");
    }

    @Test
    public void testNutritionInfoFatsAreNegative() {
        assertThrows(IllegalArgumentException.class, () -> new NutritionInfo(20, -50, 30),
                "Fats must be positive");
    }

    @Test
    public void testNutritionInfoProteinsAreNegative() {
        assertThrows(IllegalArgumentException.class, () -> new NutritionInfo(20, 50, -30),
                "Proteins must be positive");
    }

}
