package guru.springframework.services;

import guru.springframework.commands.IngredientCommand;

public interface IngredientService {
    IngredientCommand findByRecipeIdAndIngredientId(Long recipeId, Long ingredientId);

    IngredientCommand save(IngredientCommand command);

    void deleteByIngredientIdAndRecipeId(Long ingredientId, Long recipeId);

}
