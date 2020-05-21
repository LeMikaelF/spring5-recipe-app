package guru.springframework.services;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.commands.UnitOfMeasureCommand;
import guru.springframework.converters.IngredientCommandToIngredient;
import guru.springframework.converters.IngredientToIngredientCommand;
import guru.springframework.domain.Ingredient;
import guru.springframework.domain.Recipe;
import guru.springframework.domain.UnitOfMeasure;
import guru.springframework.repositories.reactive.RecipeReactiveRepository;
import guru.springframework.repositories.reactive.UnitOfMeasureReactiveRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class IngredientServiceImplTest {

    final String ingredientId = String.valueOf(345L);
    final String recipeId = String.valueOf(123L);
    private final String unitOfMeasureId = String.valueOf(77777L);
    @Mock
    RecipeReactiveRepository recipeReactiveRepository;
    @Mock
    IngredientToIngredientCommand ingredientToIngredientCommand;
    @Mock
    IngredientCommandToIngredient ingredientCommandToIngredient;
    @Mock
    UnitOfMeasureReactiveRepository unitOfMeasureReactiveRepository;
    IngredientServiceImpl service;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        service = new IngredientServiceImpl(recipeReactiveRepository,
                unitOfMeasureReactiveRepository, ingredientToIngredientCommand, ingredientCommandToIngredient);
    }

    @Test
    public void findByRecipeIdAndIngredientIdHappyPath() {
        //given
        final Recipe recipe = new Recipe();
        recipe.setId(recipeId);

        final IngredientCommand ingredientCommand = new IngredientCommand();
        ingredientCommand.setId(ingredientId);
        final Ingredient ingredient = new Ingredient();
        ingredient.setId(ingredientId);
        recipe.addIngredient(ingredient);

        when(recipeReactiveRepository.findById(eq(recipeId))).thenReturn(Mono.just(recipe));
        when(ingredientToIngredientCommand.convert(eq(ingredient))).thenReturn(ingredientCommand);

        //when
        final IngredientCommand foundIngredientCommand = service.findByRecipeIdAndIngredientId(recipeId, ingredientId).block();

        //then
        assertNotNull(foundIngredientCommand);
        assertEquals(ingredient.getDescription(), foundIngredientCommand.getDescription());
    }

    @Test
    public void save() {
        //given
        final UnitOfMeasureCommand unitOfMeasureCommand = new UnitOfMeasureCommand();
        unitOfMeasureCommand.setId(unitOfMeasureId);

        final IngredientCommand ingredientCommand = new IngredientCommand();
        ingredientCommand.setId(ingredientId);
        ingredientCommand.setAmount(BigDecimal.valueOf(3));
        ingredientCommand.setRecipeId(recipeId);

        final UnitOfMeasure unitOfMeasure = new UnitOfMeasure();
        unitOfMeasure.setId(unitOfMeasureId);
        ingredientCommand.setUom(unitOfMeasureCommand);

        final Ingredient ingredient = new Ingredient();
        ingredient.setId(ingredientId);
        ingredient.setUom(unitOfMeasure);

        final Recipe recipe = new Recipe();
        recipe.addIngredient(ingredient);
        recipe.setId(recipeId);


        when(recipeReactiveRepository.findById(anyString())).thenReturn(Mono.just(recipe));
        when(recipeReactiveRepository.save(any(Recipe.class))).thenReturn(Mono.just(recipe));
        when(ingredientToIngredientCommand.convert(any())).thenReturn(ingredientCommand);
        when(ingredientCommandToIngredient.convert(any())).thenReturn(ingredient);
        when(unitOfMeasureReactiveRepository.findById(eq(unitOfMeasureId))).thenReturn(Mono.just(unitOfMeasure));

        //when
        final IngredientCommand savedIngredientCommand = service.save(ingredientCommand).block();

        //then
        assertNotNull(savedIngredientCommand);
        assertEquals(recipeId, savedIngredientCommand.getRecipeId());
    }

    @Test
    public void deleteByIngredientIdAndRecipeId() {
        //given
        final Recipe recipe = new Recipe();
        recipe.setId(recipeId);
        final Ingredient ingredient = new Ingredient();
        ingredient.setId(ingredientId);
        recipe.addIngredient(ingredient);


        when(recipeReactiveRepository.findById(recipeId)).thenReturn(Mono.just(recipe));
        when(recipeReactiveRepository.save(any())).thenReturn(Mono.just(recipe));

        //when
        service.deleteByIngredientIdAndRecipeId(ingredientId, recipeId);

        //then
        assertTrue(recipe.getIngredients().isEmpty());
        verify(recipeReactiveRepository).save(eq(recipe));
    }

}
