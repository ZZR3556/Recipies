package org.dlaws.recipes.controllers;

import lombok.extern.slf4j.Slf4j;
import org.dlaws.recipes.commands.RecipeCommand;
import org.dlaws.recipes.services.RecipeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
public class RecipeController
{
    private static final String MODEL_ATTRIBUTE = "recipe";

    private final RecipeService recipeService;

    public RecipeController( RecipeService recipeService )
    {
        log.debug("In Constructor.");

        this.recipeService = recipeService;
    }

    @GetMapping("/recipe/{recipeIdString}/show")
    public String showById( Model model, @PathVariable String recipeIdString )
    {
        log.debug("Preparing form to show Recipe details. ( recipeId: " + recipeIdString + " )" );

        log.debug(">> Using RecipeService to get RecipeCommand object.");

        Long recipeId = Long.valueOf( recipeIdString );

        RecipeCommand recipeCommand = recipeService.getRecipeCommandById( recipeId );

        model.addAttribute( "recipe", recipeCommand ) ;

        return "/recipe/show";
    }

    @GetMapping("/recipe/new")
    public String newRecipe( Model model )
    {
        log.debug("Preparing form to create a new Recipe details.");

        model.addAttribute( "recipe", new RecipeCommand());

        return "/recipe/recipeform";
    }

    @GetMapping("/recipe/{recipeIdString}/update")
    public String updateRecipe( Model model, @PathVariable String recipeIdString )
    {
        log.debug("Preparing form to update Recipe details. ( recipeId: " + recipeIdString + " )" );

        log.debug(">> Using RecipeService to get RecipeCommand object.");

        Long recipeId = Long.valueOf( recipeIdString );

        RecipeCommand recipeCommand = recipeService.getRecipeCommandById( recipeId );

        model.addAttribute( "recipe", recipeCommand );

        return "/recipe/recipeform";
    }

    @PostMapping("/recipe")
    public String saveOrUpdate( @ModelAttribute RecipeCommand command )
    {
        log.debug("Storing New of Updated recipe." +
                        " ( recipeId: " + command.getId() +
                        ", description: " + command.getDescription() + " )");

        log.debug(">> Using RecipeService to store Recipe.");

        RecipeCommand savedCommand = recipeService.storeRecipe( command );

        return "redirect:/recipe/" + savedCommand.getId() + "/show";
    }

    @GetMapping("/recipe/{recipeIdString}/delete")
    public String deleteById( Model model, @PathVariable String recipeIdString )
    {
        log.debug("Deleting Recipe. ( recipeId: " + recipeIdString + " )");

        log.debug(">> Using RecipeService to delete Recipe.");

        Long recipeId = Long.valueOf( recipeIdString );

        recipeService.deleteRecipeById( recipeId );

        return "redirect:/"; // display Index page
    }

}
