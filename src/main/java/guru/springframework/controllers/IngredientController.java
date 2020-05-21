package guru.springframework.controllers;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.services.IngredientService;
import guru.springframework.services.RecipeService;
import guru.springframework.services.UnitOfMeasureService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    public String showIngredients(@PathVariable String recipeId, Model model) {
        model.addAttribute("recipe", recipeService.findCommandById(recipeId));
        model.addAttribute("recipeId", recipeId);
        return "recipe/ingredient/list";
    }

    @GetMapping("/ingredient/{ingredientId}/show")
    public String showIngredient(@PathVariable String recipeId, @PathVariable String ingredientId, Model model) {
        model.addAttribute("ingredient", ingredientService.findByRecipeIdAndIngredientId(recipeId, ingredientId).block());
        return "recipe/ingredient/show";
    }

    @PostMapping("/ingredient")
    public String postIngredient(@PathVariable String recipeId, @ModelAttribute IngredientCommand command) {
        final IngredientCommand savedCommand = ingredientService.save(command).block();
        return String.format("redirect:/recipe/%s/ingredient/%s/show", recipeId, savedCommand.getId());

    }

    @GetMapping("/ingredient/{ingredientId}/update")
    public String updateIngredient(@PathVariable String recipeId, @PathVariable String ingredientId, Model model) {
        model.addAttribute("ingredient", ingredientService.findByRecipeIdAndIngredientId(recipeId, ingredientId).block());
        model.addAttribute("uomList", unitOfMeasureService.findAllCommands().collectList().block());
        model.addAttribute("recipeId", recipeId);
        return "recipe/ingredient/ingredientform";
    }

    @GetMapping("/ingredient/new")
    public String newIngredient(@PathVariable String recipeId, Model model) {
        final IngredientCommand ingredientCommand = new IngredientCommand();
        ingredientCommand.setRecipeId(recipeId);
        model.addAttribute("ingredient", ingredientCommand);
        model.addAttribute("uomList", unitOfMeasureService.findAllCommands().collectList().block());
        model.addAttribute("recipeId", recipeId);
        return "recipe/ingredient/ingredientform";
    }

    @GetMapping("/ingredient/{ingredientId}/delete")
    public String deleteIngredient(@PathVariable String ingredientId, @PathVariable String recipeId) {
        ingredientService.deleteByIngredientIdAndRecipeId(ingredientId, recipeId)
                .block();
        return String.format("redirect:/recipe/%s/ingredients", recipeId);
    }
}
