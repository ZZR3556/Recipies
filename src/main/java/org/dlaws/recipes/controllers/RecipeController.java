package org.dlaws.recipes.controllers;

import lombok.extern.slf4j.Slf4j;

import org.dlaws.recipes.commands.RecipeCommand;
import org.dlaws.recipes.exceptions.NotFoundException;
import org.dlaws.recipes.services.RecipeService;
import org.dlaws.utils.Utils;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

import static org.dlaws.recipes.Statics.*;

@Slf4j
@Controller
public class RecipeController
{
    private static final String VIEW_RECIPE_RECIPEFORM = "/recipe/recipeform";

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

        // Long recipeId = Long.valueOf( recipeIdString );
        Long recipeId = Utils.parseLongInputValue( LABEL_RECIPE_ID, recipeIdString );

        RecipeCommand recipeCommand = recipeService.getRecipeCommandById( recipeId );

        model.addAttribute( MODEL_ATTRIBUTE_RECIPE, recipeCommand ) ;

        return "/recipe/show";
    }

    @GetMapping("/recipe/new")
    public String newRecipe( Model model )
    {
        log.debug("Preparing form to create a new Recipe details.");

        model.addAttribute( MODEL_ATTRIBUTE_RECIPE, new RecipeCommand());

        return VIEW_RECIPE_RECIPEFORM;
    }

    @GetMapping("/recipe/{recipeIdString}/update")
    public String updateRecipe( Model model, @PathVariable String recipeIdString )
    {
        log.debug("Preparing form to update Recipe details. ( recipeId: " + recipeIdString + " )" );

        log.debug(">> Using RecipeService to get RecipeCommand object.");

        // Long recipeId = Long.valueOf( recipeIdString );
        Long recipeId = Utils.parseLongInputValue( LABEL_RECIPE_ID, recipeIdString );

        RecipeCommand recipeCommand = recipeService.getRecipeCommandById( recipeId );

        model.addAttribute( MODEL_ATTRIBUTE_RECIPE, recipeCommand );

        return VIEW_RECIPE_RECIPEFORM;
    }

    @PostMapping("/recipe")
    public String saveOrUpdate( @Valid @ModelAttribute("recipe") RecipeCommand command,
                                BindingResult bindingResult )
    {
        log.debug("Storing New of Updated recipe." +
                        " ( recipeId: " + command.getId() +
                        ", description: " + command.getDescription() + " )");

        if (bindingResult.hasErrors())
        {
            bindingResult.getAllErrors().forEach( objectError -> {
                log.debug(objectError.toString());
            });

            return VIEW_RECIPE_RECIPEFORM;
        }

        log.debug(">> Using RecipeService to store Recipe.");

        RecipeCommand savedCommand = recipeService.storeRecipe( command );

        return "redirect:/recipe/" + savedCommand.getId() + "/show";
    }

    @GetMapping("/recipe/{recipeIdString}/delete")
    public String deleteById( Model model, @PathVariable String recipeIdString )
    {
        log.debug("Deleting Recipe. ( recipeId: " + recipeIdString + " )");

        log.debug(">> Using RecipeService to delete Recipe.");

        // Long recipeId = Long.valueOf( recipeIdString );
        Long recipeId = Utils.parseLongInputValue( LABEL_RECIPE_ID, recipeIdString );

        recipeService.deleteRecipeById( recipeId );

        return "redirect:/"; // display Index page
    }

}
