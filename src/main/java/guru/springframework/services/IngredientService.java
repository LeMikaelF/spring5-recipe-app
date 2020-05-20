package guru.springframework.services;

import guru.springframework.commands.IngredientCommand;

public interface IngredientService {
    IngredientCommand findByRecipeIdAndIngredientId(String recipeId, String ingredientId);

    IngredientCommand save(IngredientCommand command);

    void deleteByIngredientIdAndRecipeId(String ingredientId, String recipeId);

}
