package guru.springframework.services;

import guru.springframework.commands.RecipeCommand;
import guru.springframework.converters.RecipeCommandToRecipe;
import guru.springframework.converters.RecipeToRecipeCommand;
import guru.springframework.domain.Recipe;
import guru.springframework.repositories.reactive.RecipeReactiveRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Service
class RecipeServiceImpl implements RecipeService {
    /*private final RecipeRepository recipeRepository;
    private final RecipeCommandToRecipe recipeCommandToRecipe;
    private final RecipeToRecipeCommand recipeToRecipeCommand;

    RecipeServiceImpl(RecipeRepository recipeRepository, RecipeCommandToRecipe recipeCommandToRecipe, RecipeToRecipeCommand recipeToRecipeCommand) {
        this.recipeRepository = recipeRepository;
        this.recipeCommandToRecipe = recipeCommandToRecipe;
        this.recipeToRecipeCommand = recipeToRecipeCommand;
    }*/

    private final RecipeReactiveRepository recipeReactiveRepository;
    private final RecipeCommandToRecipe recipeCommandToRecipe;
    private final RecipeToRecipeCommand recipeToRecipeCommand;

    RecipeServiceImpl(RecipeReactiveRepository recipeReactiveRepository, RecipeCommandToRecipe recipeCommandToRecipe, RecipeToRecipeCommand recipeToRecipeCommand) {
        this.recipeReactiveRepository = recipeReactiveRepository;
        this.recipeCommandToRecipe = recipeCommandToRecipe;
        this.recipeToRecipeCommand = recipeToRecipeCommand;
    }

    @Override
    public Flux<Recipe> getRecipes() {
        return recipeReactiveRepository.findAll();
    }

    @Override
    public Mono<Recipe> getRecipeById(String id) {
        return recipeReactiveRepository.findById(id);
    }

    @Transactional
    @Override
    public Mono<RecipeCommand> saveRecipeFromCommandObject(RecipeCommand recipeCommand) {
        return Mono.justOrEmpty(Optional.ofNullable(recipeCommandToRecipe.convert(recipeCommand)))
                .flatMap(recipeReactiveRepository::save)
                .map(recipeToRecipeCommand::convert);
    }

    @Transactional
    @Override
    public Mono<RecipeCommand> findCommandById(String id) {
        return recipeReactiveRepository.findById(id)
                .map(recipeToRecipeCommand::convert)
                .doOnNext(recipeCommand -> recipeCommand.getIngredients()
                        .forEach(ingredientCommand -> ingredientCommand.setRecipeId(id)));
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return recipeReactiveRepository.deleteById(id)
                .then();
    }

    @Override
    public Mono<Boolean> recipeExists(String id) {
        return recipeReactiveRepository.existsById(id);
    }
}
