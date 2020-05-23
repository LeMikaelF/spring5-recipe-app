package guru.springframework.controllers;

import guru.springframework.services.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;

@Slf4j
@Controller
public class IndexController {
    private final RecipeService recipeService;

    public IndexController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @RequestMapping({"", "/"})
    public Mono<String> getRecipes(Model model) {
        return recipeService.getRecipes()
                .collectList()
                .map(recipes -> model.addAttribute("recipes", recipes))
                .then(Mono.just("index"));
    }
}
