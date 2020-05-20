package guru.springframework.repositories.reactive;

import guru.springframework.domain.Recipe;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@DataMongoTest
public class RecipeReactiveRepositoryIT {

    @Autowired
    RecipeReactiveRepository recipeReactiveRepository;
    Recipe recipe;

    @Before
    public void setup() {
        recipe = new Recipe();
        recipeReactiveRepository.deleteAll().block();
    }

    @Test
    public void saveAndRetrieveRecipe() {
        assertEquals((Long) 0L, recipeReactiveRepository.count().block());
        recipeReactiveRepository.save(recipe).block();
        assertEquals((Long) 1L, recipeReactiveRepository.count().block());
    }
}
