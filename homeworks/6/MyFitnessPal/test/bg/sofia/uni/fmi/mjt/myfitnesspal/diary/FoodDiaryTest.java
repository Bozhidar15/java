package bg.sofia.uni.fmi.mjt.myfitnesspal.diary;

import bg.sofia.uni.fmi.mjt.myfitnesspal.exception.UnknownFoodException;
import bg.sofia.uni.fmi.mjt.myfitnesspal.nutrition.NutritionInfo;
import bg.sofia.uni.fmi.mjt.myfitnesspal.nutrition.NutritionInfoAPI;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(MockitoExtension.class)
public class FoodDiaryTest {

    @Mock
    private NutritionInfoAPI nutritionInfoApi;

    @InjectMocks
    private DailyFoodDiary foodDiary;

    String foodTestChicken = "Chicken";
    NutritionInfo forChicken = new NutritionInfo(40, 20, 40);

    String foodTestRice = "Rice";

    NutritionInfo forRice = new NutritionInfo(52.5, 22.5, 25);

    String foodTestPizza = "Pizza";

    NutritionInfo forPizza = new NutritionInfo(50, 37.2, 12.8);

    @BeforeEach
    public void addItems() throws UnknownFoodException {
        when(nutritionInfoApi.getNutritionInfo(foodTestChicken)).thenReturn(forChicken);
        when(nutritionInfoApi.getNutritionInfo(foodTestRice)).thenReturn(forRice);
        when(nutritionInfoApi.getNutritionInfo(foodTestPizza)).thenReturn(forPizza);

        foodDiary.addFood(Meal.LUNCH, foodTestPizza, 2);
        foodDiary.addFood(Meal.LUNCH, foodTestRice, 2);
        foodDiary.addFood(Meal.DINNER, foodTestChicken, 2);
    }

    @Test
    @Order(1)
    public void testAddFoodWhenMealIsNull() throws UnknownFoodException {

        assertThrows(IllegalArgumentException.class, () -> foodDiary.addFood(null, foodTestChicken, 5),
                "Adding food when meal is null must return IllegalArgumentException");
    }

    @Test
    @Order(2)
    public void testAddFoodWhenFoodIsNull() throws UnknownFoodException {

        assertThrows(IllegalArgumentException.class, () -> foodDiary.addFood(Meal.LUNCH, null, 5),
                "Adding food when food name is null must return IllegalArgumentException");
    }

    @Test
    @Order(3)
    public void testAddFoodWhenFoodIsEmpty() throws UnknownFoodException {

        assertThrows(IllegalArgumentException.class, () -> foodDiary.addFood(Meal.LUNCH, "", 5),
                "Adding food when food name is empty must return IllegalArgumentException");
    }

    @Test
    @Order(4)
    public void testAddFoodWhenFoodIsBlank() throws UnknownFoodException {

        assertThrows(IllegalArgumentException.class, () -> foodDiary.addFood(Meal.LUNCH, " ", 5),
                "Adding food when food is blank must return IllegalArgumentException");
    }

    @Test
    @Order(5)
    public void testAddFoodWhenSizeIsNegative() throws UnknownFoodException {
        assertThrows(IllegalArgumentException.class, () -> foodDiary.addFood(Meal.DINNER, foodTestChicken, -5),
                "Adding meal when size is negative must return IllegalArgumentException");
    }

    @Test
    @Order(6)
    public void testAddFoodWhenNutritionCantFindFood() throws UnknownFoodException {
        when(nutritionInfoApi.getNutritionInfo("ppp")).thenThrow(new UnknownFoodException("There in no such food"));
        assertThrows(UnknownFoodException.class, () -> foodDiary.addFood(Meal.LUNCH, "ppp", 5),
                "There in no such a food in nutrition which cause UnknownFoodException");
    }

    @Test
    @Order(7)
    @Disabled
    public void testgetAllFoodEntriesWhenThereIsNothing() throws UnknownFoodException {

        assertIterableEquals(new ArrayList<>(), foodDiary.getAllFoodEntries(),
                "List have to be empty, because there are no elements");
    }

    @Test
    @Order(8)
    public void testGetAllFoodEntriesWhenThereIsTreeItems() throws UnknownFoodException {

        List<FoodEntry> temp = new ArrayList<>();
        temp.add(new FoodEntry(foodTestPizza, 2, forPizza));
        temp.add(new FoodEntry(foodTestRice, 2, forRice));
        temp.add(new FoodEntry(foodTestChicken, 2, forChicken));
        assertIterableEquals(temp, foodDiary.getAllFoodEntries(),
                "List have two elements which are not the same");
    }

    @Test
    @Order(9)
    public void testGetAllFoodEntriesByProteinContent() throws UnknownFoodException {

        List<FoodEntry> temp = new ArrayList<>();
        temp.add(new FoodEntry(foodTestPizza, 2, forPizza));
        temp.add(new FoodEntry(foodTestRice, 2, forRice));
        temp.add(new FoodEntry(foodTestChicken, 2, forChicken));
        assertIterableEquals(temp, foodDiary.getAllFoodEntriesByProteinContent(),
                "List have two elements which are not the same");
    }

    @Test
    @Order(10)
    public void testGetDailyCaloriesIntakePerMealAddedOnlyOneFood() throws UnknownFoodException {
        when(nutritionInfoApi.getNutritionInfo("cake")).thenReturn(new NutritionInfo
                (35, 35, 30));
        foodDiary.addFood(Meal.SNACKS, "cake", 2);
        assertEquals(1150.0, foodDiary.getDailyCaloriesIntakePerMeal(Meal.SNACKS), 0.001,
                "Calories are not equel");
    }

    @Test
    @Order(11)
    public void testGetDailyCaloriesIntakePerMealNoFoodAdded() {
        assertEquals(0.0, foodDiary.getDailyCaloriesIntakePerMeal(Meal.BREAKFAST), 0.001,
                "Calories should be zero.");
    }

    @Test
    @Order(12)
    public void testGetDailyCaloriesIntakePerMealWhenIsNull() {
        assertThrows(IllegalArgumentException.class, () -> foodDiary.getDailyCaloriesIntakePerMeal(null),
                "Calories should be zero.");
    }

    @Test
    @Order(12)
    public void getDailyCaloriesIntakeWhenThereAreThreeItems() {
        assertEquals(3197, foodDiary.getDailyCaloriesIntake(), 0.001,
                "Calories should be zero.");
    }

}
