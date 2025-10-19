package org.example.java.springbatch;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecipeService {

    private final RecipeRepo recipeRepo;

    public RecipeService(RecipeRepo recipeRepo) {
        this.recipeRepo = recipeRepo;
    }

    public Page<RecipeEntity> getRecipes(Pageable pageable){
        return recipeRepo.findAll(pageable);
    }

    public List<RecipeEntity> searchRecipe(String title, String cuisine,
                                           Double rating, Integer calories, Integer totalTime) {
        List<RecipeEntity> recipes = recipeRepo.findAll();
        return recipes.stream()
                .filter(recipe -> title==null || (recipe.getTitle() != null && recipe.getTitle().toLowerCase().contains(title.toLowerCase())))
                .filter(recipe -> cuisine==null || (recipe.getCuisine() != null && recipe.getCuisine().toLowerCase().contains(cuisine.toLowerCase())))
                .filter(recipe -> rating==null || (recipe.getRating() != null &&  recipe.getRating() >= rating))
                .filter(recipe ->calories==null ||
                        recipe.getNutrients() != null &&
                        recipe.getNutrients().get("calories") != null &&
                        extractCalories(recipe.getNutrients().get("calories")) <= calories)
                .filter(recipe -> totalTime==null || (recipe.getTotalTime() != null && recipe.getTotalTime() <= totalTime))
                .toList();
    }

    public int extractCalories(String calorie){
        try {
            return Integer.parseInt(calorie.replaceAll("[^0-9]",""));
        }catch (Exception e){
            return Integer.MAX_VALUE;
        }
    }
}
