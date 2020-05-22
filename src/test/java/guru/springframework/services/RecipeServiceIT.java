package guru.springframework.services;

import guru.Spring5RecipeAppApplication;
import guru.springframework.commands.RecipeCommand;
import guru.springframework.converters.RecipeCommandToRecipe;
import guru.springframework.converters.RecipeToRecipeCommand;
import guru.springframework.domain.Recipe;
import guru.springframework.repositories.RecipeRepository;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@Ignore //Broken with mongo
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
@ContextConfiguration(classes = {Spring5RecipeAppApplication.class})
public class RecipeServiceIT {
    private static final String DESCRIPTION = "description";
    @Autowired
    RecipeRepository repository;
    @Autowired
    RecipeService service;
    @Autowired
    RecipeToRecipeCommand recipeToRecipeCommand;
    @Autowired
    RecipeCommandToRecipe recipeCommandToRecipe;

    @Test
    public void nullObject() {
        assertNull(service.saveRecipeFromCommandObject(null).block());
    }

    @Test
    @Ignore
    public void saveRecipeFromCommandObject() {
        Recipe recipe = repository.findAll().iterator().next();
        recipe.setDescription(DESCRIPTION);
        final RecipeCommand recipeCommand = recipeToRecipeCommand.convert(recipe);
        final RecipeCommand savedRecipeCommand = service.saveRecipeFromCommandObject(recipeCommand).block();

        assertNotNull(recipeCommand);
        assertEquals(DESCRIPTION, savedRecipeCommand.getDescription());
        assertEquals(recipe.getCategories().size(), recipeCommand.getCategories().size());
        assertEquals(recipe.getIngredients().size(), savedRecipeCommand.getIngredients().size());
    }

    @Test
    public void recipeExistsTrue() {
        final Recipe recipe = repository.findAll().iterator().next();
        assertTrue(service.recipeExists(recipe.getId()).block());
    }

/*  Disabled on id migration to Strings
    @Test
    public void recipeExistsFalse() {
        Set<Long> ids = new TreeSet<>();
        repository.findAll().iterator().forEachRemaining(recipe -> ids.add(recipe.getId()));
        Long tracker = Long.MIN_VALUE;
        for (Long id : ids) {
            tracker = id;
        }
        assertFalse(service.recipeExists(tracker + 1));
    }
*/

}
