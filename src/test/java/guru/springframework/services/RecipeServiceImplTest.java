package guru.springframework.services;

import guru.springframework.commands.RecipeCommand;
import guru.springframework.converters.RecipeCommandToRecipe;
import guru.springframework.converters.RecipeToRecipeCommand;
import guru.springframework.domain.Recipe;
import guru.springframework.repositories.reactive.RecipeReactiveRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RecipeServiceImplTest {

    final String id = String.valueOf(123L);
    @Mock
    RecipeReactiveRepository repository;
    @Mock
    RecipeToRecipeCommand recipeToRecipeCommand;
    @Mock
    RecipeCommandToRecipe recipeCommandToRecipe;
    RecipeServiceImpl service;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        service = new RecipeServiceImpl(repository,
                recipeCommandToRecipe, recipeToRecipeCommand);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getRecipes() {
        final Recipe recipe = new Recipe();
        Set<Recipe> set = new HashSet<>();
        set.add(recipe);
        when(repository.findAll()).thenReturn(Flux.fromIterable(set));

        final List<Recipe> recipes = service.getRecipes().buffer().blockFirst();
        assertEquals(1, recipes.size());
        verify(repository, times(1)).findAll();
    }

    @Test
    public void getRecipeById() {
        String id = String.valueOf(33L);
        final Recipe recipe = new Recipe();
        recipe.setId(id);
        when(repository.findById(eq(id))).thenReturn(Mono.just(recipe));
        assertEquals("33", service.getRecipeById("33").block().getId());
    }

    @Test
    public void findCommandById() {
        final RecipeCommand recipeCommand = new RecipeCommand();
        final Recipe recipe = new Recipe();
        recipe.setId(id);
        recipeCommand.setId(id);
        when(repository.findById(eq(id))).thenReturn(Mono.just(recipe));
        when(recipeToRecipeCommand.convert(any())).thenReturn(recipeCommand);
        final RecipeCommand commandById = service.findCommandById(id).block();
        assertNotNull(commandById);
        assertEquals(id, commandById.getId());
    }

    @Test
    public void deleteById() {
        //given
        when(repository.deleteById(id)).thenReturn(Mono.empty());

        //when
        service.deleteById(id);

        //then
        verify(repository).deleteById(eq(id));
    }

    @Test
    public void findRecipeByIdNotFound() {
        //given
        when(repository.findById(id)).thenReturn(Mono.empty());

        //when
        final Mono<RecipeCommand> result = service.findCommandById(id);

        //then
        StepVerifier.create(result).verifyComplete();
    }
}

