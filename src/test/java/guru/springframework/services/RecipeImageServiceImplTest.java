package guru.springframework.services;

import guru.springframework.domain.Recipe;
import guru.springframework.repositories.reactive.RecipeReactiveRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import reactor.core.publisher.Mono;

import java.io.IOException;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RecipeImageServiceImplTest {

    final String recipeId = String.valueOf(111L);
    @Mock
    RecipeReactiveRepository recipeRepository;
    RecipeImageServiceImpl recipeImageService;

    //TODO setup mocks
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        recipeImageService = new RecipeImageServiceImpl(recipeRepository);
    }

    @Test
    public void save() throws IOException {
        //given
        Recipe recipe = new Recipe();
        recipe.setId(recipeId);
        when(recipeRepository.findById(eq(recipeId))).thenReturn(Mono.just(recipe));
        when(recipeRepository.save(any())).thenReturn(Mono.just(recipe));
        final MockMultipartFile multipartFile = new MockMultipartFile("imagefile", "Mon contenu fou".getBytes());

        //when
        recipeImageService.save(recipeId, multipartFile).block();

        final ArgumentCaptor<Recipe> captor = ArgumentCaptor.forClass(Recipe.class);

        //then
        verify(recipeRepository).findById(recipeId);
        verify(recipeRepository).save(captor.capture());
        assertEquals(multipartFile.getBytes().length, captor.getValue().getImage().length);
    }

    @Test
    public void findById() {
        //given
        final byte[] primitiveBytes = "primitive bytes".getBytes();
        Byte[] wrappedBytes = new Byte[primitiveBytes.length];
        for (int i = 0; i < primitiveBytes.length; i++) {
            wrappedBytes[i] = primitiveBytes[i];
        }

        final Recipe recipe = new Recipe();
        recipe.setId(recipeId);
        recipe.setImage(wrappedBytes);
        when(recipeRepository.findById(eq(recipeId))).thenReturn(Mono.just(recipe));

        //when
        final Byte[] foundBytes = recipeImageService.findById(recipeId).block();

        //then
        assertArrayEquals(wrappedBytes, foundBytes);
        verify(recipeRepository).findById(eq(recipeId));
    }


}
