package bg.sofia.uni.fmi.mjt.myfitnesspal.diary;

import bg.sofia.uni.fmi.mjt.myfitnesspal.exception.UnknownFoodException;
import bg.sofia.uni.fmi.mjt.myfitnesspal.nutrition.NutritionInfoAPI;
import bg.sofia.uni.fmi.mjt.myfitnesspal.nutrition.NutritionInfo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class DailyFoodDiary implements FoodDiary {

    private final NutritionInfoAPI nutritionInfoApi;

    private final Map<Meal, List<FoodEntry>> meals;

    private final FoodEntryProteinContentComparator foodEntryProteinContentComparator;

    public DailyFoodDiary(NutritionInfoAPI nutritionInfoApi) {
        this.nutritionInfoApi = nutritionInfoApi;
        this.meals = new EnumMap<>(Meal.class);
        this.foodEntryProteinContentComparator = new FoodEntryProteinContentComparator();
    }

    @Override
    public FoodEntry addFood(Meal meal, String foodName, double servingSize) throws UnknownFoodException {
        if (meal == null) {
            throw new IllegalArgumentException("Meal cannot be null");
        }
        else if (foodName == null || foodName.isBlank() || foodName.isEmpty()) {
            throw new IllegalArgumentException("Food cannot be null or blank");
        }
        else if (servingSize < 0) {
            throw new IllegalArgumentException("Serving size cannot be negative");
        }
        else if (meal != Meal.LUNCH && meal != Meal.BREAKFAST && meal != Meal.SNACKS && meal != Meal.DINNER) {
            throw new UnknownFoodException("Meal is unknown");
        }

        NutritionInfo nutritionInfo = nutritionInfoApi.getNutritionInfo(foodName);
        if (nutritionInfo == null) {
            throw new UnknownFoodException("There is no information about this food");
        }
        FoodEntry foodEntry = new FoodEntry(foodName, servingSize, nutritionInfo);
        if (!meals.containsKey(meal)) {
            meals.put(meal, new ArrayList<>());

        }
        meals.get(meal).add(foodEntry);
        return foodEntry;
    }

    @Override
    public Collection<FoodEntry> getAllFoodEntries() {
        List<FoodEntry> allFoodEntries = new ArrayList<>();

        for (List<FoodEntry> foodEntries : meals.values()) {
            allFoodEntries.addAll(foodEntries);
        }
        return Collections.unmodifiableCollection(allFoodEntries);
    }

    @Override
    public List<FoodEntry> getAllFoodEntriesByProteinContent() {
        List<FoodEntry> allFoodEntries = new ArrayList<>(getAllFoodEntries());
        allFoodEntries.sort(foodEntryProteinContentComparator);
        return Collections.unmodifiableList(allFoodEntries);
    }

    @Override
    public double getDailyCaloriesIntake() {
        double dailyCaloriesIntake = 0.0;

        for (Meal meal : meals.keySet()) {
            dailyCaloriesIntake += getDailyCaloriesIntakePerMeal(meal);
        }

        return dailyCaloriesIntake;
    }

    @Override
    public double getDailyCaloriesIntakePerMeal(Meal meal) {
        if (meal == null) {
            throw new IllegalArgumentException("meal cannot be null");
        }

        double dailyCaloriesIntakePerMeal = 0.0;
        if (!meals.containsKey(meal)) {
            return dailyCaloriesIntakePerMeal;
        }
        List<FoodEntry> foodEntries = meals.get(meal);
        for (FoodEntry foodEntry : foodEntries) {
            dailyCaloriesIntakePerMeal += foodEntry.nutritionInfo().calories() * foodEntry.servingSize();
        }

        return dailyCaloriesIntakePerMeal;
    }

}