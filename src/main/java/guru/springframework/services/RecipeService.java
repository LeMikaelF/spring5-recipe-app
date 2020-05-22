package guru.springframework.services;

import guru.springframework.commands.RecipeCommand;
import guru.springframework.domain.Recipe;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RecipeService {
    Flux<Recipe> getRecipes();

    Mono<Recipe> getRecipeById(String id);

    Mono<RecipeCommand> saveRecipeFromCommandObject(RecipeCommand recipeCommand);

    Mono<RecipeCommand> findCommandById(String id);

    Mono<Void> deleteById(String id);

    Mono<Boolean> recipeExists(String id);
}
