package guru.springframework.services;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.converters.IngredientCommandToIngredient;
import guru.springframework.converters.IngredientToIngredientCommand;
import guru.springframework.domain.Ingredient;
import guru.springframework.domain.Recipe;
import guru.springframework.domain.UnitOfMeasure;
import guru.springframework.repositories.reactive.RecipeReactiveRepository;
import guru.springframework.repositories.reactive.UnitOfMeasureReactiveRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
public class IngredientServiceImpl implements IngredientService {
    /*
        private final RecipeRepository recipeRepository;
        private final IngredientToIngredientCommand ingredientToIngredientCommand;
        private final IngredientCommandToIngredient ingredientCommandToIngredient;
        private final UnitOfMeasureRepository unitOfMeasureRepository;
    */
    private final RecipeReactiveRepository recipeReactiveRepository;
    private final UnitOfMeasureReactiveRepository unitOfMeasureReactiveRepository;
    private final IngredientToIngredientCommand ingredientToIngredientCommand;
    private final IngredientCommandToIngredient ingredientCommandToIngredient;

    public IngredientServiceImpl(RecipeReactiveRepository recipeReactiveRepository, UnitOfMeasureReactiveRepository unitOfMeasureReactiveRepository, IngredientToIngredientCommand ingredientToIngredientCommand, IngredientCommandToIngredient ingredientCommandToIngredient) {
        this.recipeReactiveRepository = recipeReactiveRepository;
        this.unitOfMeasureReactiveRepository = unitOfMeasureReactiveRepository;
        this.ingredientToIngredientCommand = ingredientToIngredientCommand;
        this.ingredientCommandToIngredient = ingredientCommandToIngredient;
    }


    @Override
    public Mono<IngredientCommand> findByRecipeIdAndIngredientId(String recipeId, String ingredientId) {
        return recipeReactiveRepository.findById(recipeId)
                .map(recipe -> recipe.getIngredients()
                        .stream()
                        .filter(ingredient -> ingredientId.equals(ingredient.getId()))
                        .map(ingredientToIngredientCommand::convert)
                        .findFirst())
                .filter(Optional::isPresent)
                .map(Optional::get);

    }

    @Override
    @Transactional
    public Mono<IngredientCommand> save(IngredientCommand command) {
        //check recipe exists
        final Optional<Recipe> optRecipe = recipeReactiveRepository.findById(command.getRecipeId()).blockOptional();

        if (!optRecipe.isPresent()) {
            log.debug("Recipe attached to ingredient could not be found.");
            return Mono.just(new IngredientCommand());
        }
        final Recipe recipe = optRecipe.get();
        final Optional<Ingredient> foundIngredient = recipe.getIngredients().stream().filter(ingredient -> Objects.equals(ingredient.getId(), command.getId()))
                .findFirst();
        if (foundIngredient.isPresent()) {
            final Ingredient ingredient = foundIngredient.get();
            ingredient.setAmount(command.getAmount());
            ingredient.setDescription(command.getDescription());

            final UnitOfMeasure savedUnitOfMeasure = Optional.ofNullable(command.getUom())
                    .flatMap(uomCommand ->
                            Optional.ofNullable(unitOfMeasureReactiveRepository.findById(uomCommand.getId()).block()))
                    .orElseThrow(() -> new RuntimeException("Uom could not be found"));
            ingredient.setUom(savedUnitOfMeasure);
        } else {
            final Ingredient ingredient = ingredientCommandToIngredient.convert(command);
            recipe.addIngredient(ingredient);
        }
        log.debug("IngredientCommand to save: {}", command.toString());

        return recipeReactiveRepository.save(recipe)
                .map(recipe1 -> recipe1.getIngredients().stream()
                        .filter(ingredient ->
                                Objects.equals(ingredient.getDescription(), command.getDescription())
                                        && Objects.equals(ingredient.getAmount(), command.getAmount())
                                        && Objects.equals(ingredient.getUom().getId(), command.getUom().getId()))
                        .findFirst()
                        .map(ingredientToIngredientCommand::convert)
                        .orElse(null)
                );
    }

    @Override
    public Mono<Void> deleteByIngredientIdAndRecipeId(String ingredientId, String recipeId) {
        recipeReactiveRepository.findById(recipeId)
                .map(recipe -> {
                    recipe.getIngredients().stream()
                            .filter(ingredient -> Objects.equals(ingredientId, ingredient.getId()))
                            .findFirst().ifPresent(ingredient ->
                            recipe.getIngredients().remove(ingredient));
                    return recipeReactiveRepository.save(recipe).block();
                }).block();
        return Mono.empty();
    }
}
