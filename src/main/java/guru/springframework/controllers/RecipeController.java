package guru.springframework.controllers;

import guru.springframework.commands.RecipeCommand;
import guru.springframework.exceptions.RecipeNotFoundException;
import guru.springframework.services.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@Slf4j
@RequestMapping("/recipe")
@Controller
public class RecipeController {
    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping("/{id}/show")
    public Mono<String> showRecipeById(@PathVariable String id, Model model) {
        return recipeService.getRecipeById(id)
                .doOnNext(recipe -> model.addAttribute("recipe", recipe))
                .thenReturn("recipe/show");
    }

    @GetMapping("/new")
    public Mono<String> newRecipe(Model model) {
        return Mono.fromRunnable(() -> model.addAttribute("recipe", new RecipeCommand()))
                .thenReturn("recipe/recipeform");
    }

    @PostMapping("")
    public Mono<String> postRecipe(@Valid @ModelAttribute("recipe") RecipeCommand command,
                                   BindingResult result) {
        if (result.hasErrors()) {
            result.getAllErrors().stream().map(ObjectError::toString).forEach(log::debug);
            return Mono.just("recipe/recipeform");
        }
        return recipeService.saveRecipeFromCommandObject(command)
                .map(savedCommand -> "redirect:/recipe/" + savedCommand.getId() + "/show");
    }

    @GetMapping("/{id}/update")
    public Mono<String> updateRecipe(@PathVariable String id, Model model) {
        return recipeService.findCommandById(id)
                .map(recipeCommand -> model.addAttribute("recipe", recipeCommand))
                .thenReturn("recipe/recipeform");
    }

    @GetMapping("/{id}/delete")
    public Mono<String> deleteRecipeById(@PathVariable String id) {
        return recipeService.deleteById(id)
                .thenReturn("redirect:/");
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(RecipeNotFoundException.class)
    public Mono<String> fourOfourHandler(Exception exception, Model model) {
        return Mono.fromRunnable(() -> model.addAttribute("exception", exception))
                .thenReturn("errors/404error");
    }
}
