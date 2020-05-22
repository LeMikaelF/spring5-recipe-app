package guru.springframework.controllers;

import guru.springframework.domain.Recipe;
import guru.springframework.services.RecipeService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.matches;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

public class IndexControllerTest {

    @Mock
    RecipeService recipeService;
    @Mock
    Model model;
    IndexController controller;
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        controller = new IndexController(recipeService);
    }

    @Test
    public void testMockMvc() throws Exception {
        //given
        when(recipeService.getRecipes()).thenReturn(Flux.just(new Recipe()));

        //when
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        mockMvc.perform(get("/")).andExpect(status().isOk())
                .andExpect(view().name("index"));

        //then
        verify(recipeService).getRecipes();
    }

    @Test
    public void getRecipes() {
        final Recipe recipe1 = new Recipe();
        recipe1.setDescription("first");
        final Recipe recipe2 = new Recipe();
        recipe2.setDescription("second");
        when(recipeService.getRecipes()).thenReturn(Flux.fromStream(Stream.of(recipe1, recipe2)));

        final ArgumentCaptor<List<Recipe>> argumentCaptor = ArgumentCaptor.forClass(ArrayList.class);

        assertEquals("index", controller.getRecipes(model));
        verify(model, times(1)).addAttribute(matches("recipes"), argumentCaptor.capture());
        assertEquals(2, argumentCaptor.getValue().size());
        verify(recipeService, times(1)).getRecipes();
    }
}
