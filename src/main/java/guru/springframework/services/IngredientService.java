package guru.springframework.services;

import guru.springframework.commands.IngredientCommand;
import reactor.core.publisher.Mono;

public interface IngredientService {
    Mono<IngredientCommand> findByRecipeIdAndIngredientId(String recipeId, String ingredientId);

    Mono<IngredientCommand> save(IngredientCommand command);

    Mono<Void> deleteByIngredientIdAndRecipeId(String ingredientId, String recipeId);

}
