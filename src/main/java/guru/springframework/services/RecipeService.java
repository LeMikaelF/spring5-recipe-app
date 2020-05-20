package guru.springframework.services;

import guru.springframework.commands.RecipeCommand;
import guru.springframework.domain.Recipe;

import java.util.Set;

public interface RecipeService {
    Set<Recipe> getRecipes();

    Recipe getRecipeById(String id);

    RecipeCommand saveRecipeFromCommandObject(RecipeCommand recipeCommand);

    RecipeCommand findCommandById(String id);

    void deleteById(String id);

    boolean recipeExists(String id);
}
