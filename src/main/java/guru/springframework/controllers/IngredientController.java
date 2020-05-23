package guru.springframework.controllers;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.services.IngredientService;
import guru.springframework.services.RecipeService;
import guru.springframework.services.UnitOfMeasureService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RequestMapping("/recipe/{recipeId}")
@Controller
public class IngredientController {
    private final RecipeService recipeService;
    private final IngredientService ingredientService;
    private final UnitOfMeasureService unitOfMeasureService;

    public IngredientController(RecipeService recipeService, IngredientService ingredientService, UnitOfMeasureService unitOfMeasureService) {
        this.recipeService = recipeService;
        this.ingredientService = ingredientService;
        this.unitOfMeasureService = unitOfMeasureService;
    }

    @GetMapping("/ingredients")
    public Mono<String> showIngredients(@PathVariable String recipeId, Model model) {
        return recipeService.findCommandById(recipeId)
                .doOnNext(recipeCommand -> model.addAttribute("recipe", recipeCommand))
                .then()
                .doOnNext(aVoid -> model.addAttribute("recipeId", recipeId))
                .thenReturn("recipe/ingredient/list");
    }

    @GetMapping("/ingredient/{ingredientId}/show")
    public Mono<String> showIngredient(@PathVariable String recipeId, @PathVariable String ingredientId, Model model) {
        return ingredientService.findByRecipeIdAndIngredientId(recipeId, ingredientId)
                .map(ingredientCommand -> model.addAttribute("ingredient", ingredientCommand))
                .thenReturn("recipe/ingredient/show");
    }

    @PostMapping("/ingredient")
    public Mono<String> postIngredient(@PathVariable String recipeId, @ModelAttribute IngredientCommand command) {
     return ingredientService.save(command)
             .map(ingredientCommand ->
                     String.format("redirect:/recipe/%s/ingredient/%s/show", recipeId, ingredientCommand.getId()));

    }

    @GetMapping("/ingredient/{ingredientId}/update")
    public Mono<String> updateIngredient(@PathVariable String recipeId, @PathVariable String ingredientId, Model model) {
        return ingredientService.findByRecipeIdAndIngredientId(recipeId, ingredientId)
                .doOnNext(ingredientCommand -> model.addAttribute("ingredient", ingredientCommand))
                .then(unitOfMeasureService.findAllCommands().collectList())
                .doOnNext(unitOfMeasureCommands -> model.addAttribute("uomList", unitOfMeasureCommands))
                .then()
                .map(aVoid -> model.addAttribute("recipeId", recipeId))
                .thenReturn("recipe/ingredient/ingredientform");
    }

    @GetMapping("/ingredient/new")
    public Mono<String> newIngredient(@PathVariable String recipeId, Model model) {
        final IngredientCommand ingredientCommand = new IngredientCommand();
        ingredientCommand.setRecipeId(recipeId);
        return unitOfMeasureService.findAllCommands().collectList()
                .map(unitOfMeasureCommands -> model.addAttribute("uomList", unitOfMeasureCommands))
                .then()
                .doOnNext(aVoid -> model.addAttribute("ingredient", ingredientCommand))
                .doOnNext(aVoid -> model.addAttribute("recipeId", recipeId))
                .thenReturn("recipe/ingredient/ingredientform");
    }

    @GetMapping("/ingredient/{ingredientId}/delete")
    public Mono<String> deleteIngredient(@PathVariable String ingredientId, @PathVariable String recipeId) {
        return ingredientService.deleteByIngredientIdAndRecipeId(ingredientId, recipeId)
                .thenReturn(String.format("redirect:/recipe/%s/ingredients", recipeId));
    }
}
