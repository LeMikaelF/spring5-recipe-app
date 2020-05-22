package guru.springframework.controllers;

import guru.springframework.domain.Recipe;
import guru.springframework.services.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Slf4j
@Controller
public class IndexController {
    private final RecipeService recipeService;

    public IndexController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @RequestMapping({"", "/"})
    public String getRecipes(Model model) {
        final List<Recipe> recipes = recipeService.getRecipes().buffer().blockFirst();
        model.addAttribute("recipes", recipes);
        log.info("Added to model: {} recipes", recipes.size());
        return "index";
    }
}
