package org.dlaws.recipes.controllers;

import lombok.extern.slf4j.Slf4j;

import org.dlaws.recipes.commands.IngredientCommand;
import org.dlaws.recipes.commands.RecipeCommand;
import org.dlaws.recipes.commands.UnitOfMeasureCommand;
import org.dlaws.recipes.exceptions.NotFoundException;
import org.dlaws.recipes.services.IngredientService;
import org.dlaws.recipes.services.RecipeService;
import org.dlaws.recipes.services.UnitOfMeasureService;
import org.dlaws.utils.Utils;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

import static org.dlaws.recipes.Statics.*;

@Slf4j
@Controller
public class IngredientController
{
    private final RecipeService recipeService;
    private final IngredientService ingredientService;
    private final UnitOfMeasureService unitOfMeasureService;

    public IngredientController( RecipeService recipeService,
                                 IngredientService ingredientService,
                                 UnitOfMeasureService unitOfMeasureService )
    {
        log.debug("In Constructor.");

        this.recipeService = recipeService;
        this.ingredientService = ingredientService;
        this.unitOfMeasureService = unitOfMeasureService;
    }

    @GetMapping("/recipe/{recipeIdString}/ingredients")
    public String listRecipeIngredients( Model model, @PathVariable String recipeIdString )
    {
        log.debug("Preparing form to show Recipe Ingredient List. ( recipe id: " + recipeIdString + " )" );

        // Long recipeId = Long.valueOf( recipeIdString );
        Long recipeId = Utils.parseLongInputValue( LABEL_RECIPE_ID, recipeIdString );

        log.debug(">> Using RecipeService to get RecipeCommand object.");

        RecipeCommand recipeCommand = recipeService.getRecipeCommandById( recipeId );

        // Use command object to avoid lazy load errors in Thymeleaf rendering.
        model.addAttribute( MODEL_ATTRIBUTE_RECIPE, recipeCommand );

        return "/recipe/ingredient/list";
    }

    @GetMapping("/recipe/{recipeIdString}/ingredient/{ingredientIdString}/show")
    public String showRecipeIngredient( Model model, @PathVariable String recipeIdString, @PathVariable String ingredientIdString )
    {
        log.debug("Preparing form to show Recipe Ingredient details." +
                " ( recipe id: " + recipeIdString + ", ingredientId: " + ingredientIdString + " )" );

        // Long recipeId = Long.valueOf( recipeIdString );
        // Long ingredientId = Long.valueOf( ingredientIdString );
        Long recipeId = Utils.parseLongInputValue( LABEL_RECIPE_ID, recipeIdString );
        Long ingredientId = Utils.parseLongInputValue( LABEL_INGREDIENT_ID, ingredientIdString );

        log.debug(">> Using IngredientService to get IngredientCommand object.");

        IngredientCommand ingredientCommand = ingredientService.findByRecipeIdAndIngredientId( recipeId, ingredientId );

        model.addAttribute( MODEL_ATTRIBUTE_INGREDIENT, ingredientCommand );

        return "/recipe/ingredient/show";
    }

    @GetMapping("/recipe/{recipeIdString}/ingredient/{ingredientIdString}/update")
    public String updateRecipeIngredient( Model model,
                                          @PathVariable String recipeIdString,
                                          @PathVariable String ingredientIdString )
    {
        log.debug("Preparing form to update Recipe Ingredient." +
                " ( recipeId: " + recipeIdString + ", ingredientId: " + ingredientIdString + " )" );

        // Long recipeId = Long.valueOf(recipeIdString);
        // Long ingredientId = Long.valueOf(ingredientIdString);
        Long recipeId = Utils.parseLongInputValue( LABEL_RECIPE_ID, recipeIdString );
        Long ingredientId = Utils.parseLongInputValue( LABEL_INGREDIENT_ID, ingredientIdString );

        log.debug(">> Using IngredientService to get IngredientCommand object.");

        IngredientCommand ingredientCommand = ingredientService.findByRecipeIdAndIngredientId( recipeId, ingredientId );

        log.debug(">> Using UnitOfMeasureService to get Set of UnitOfMeasureCommand objects.");

        Set<UnitOfMeasureCommand> unitOfMeasureCommandSet = unitOfMeasureService.listAllUoms();

        model.addAttribute( MODEL_ATTRIBUTE_INGREDIENT, ingredientCommand );
        model.addAttribute( MODEL_ATTRIBUTE_UOM_LIST, unitOfMeasureCommandSet );

        return "/recipe/ingredient/ingredientform";
    }

    @PostMapping("/recipe/{recipeId}/ingredient")
    public String storeOrUpdateIngredient( @ModelAttribute IngredientCommand ingredientCommand )
    {
        log.debug("Using IngredientService to store IngredientCommand object details. " + ingredientCommand );

        IngredientCommand storedIngredient = ingredientService.saveIngredientCommand( ingredientCommand );

        Long recipeId = storedIngredient.getRecipeId();
        Long ingredientId = storedIngredient.getId();

        log.debug(">> Stored Ingredient. ( recipeId: " + recipeId + ", ingredientId: " + ingredientId + " )" );

        return "redirect:/recipe/" + recipeId + "/ingredient/" + ingredientId + "/show";
    }

    @GetMapping("/recipe/{recipeIdString}/ingredient/new")
    public String newIngredient( Model model,
                                 @PathVariable String recipeIdString )
    {
        log.debug("Preparing form to create new Recipe Ingredient. ( recipeId: " + recipeIdString + " )" );

        // Long recipeId = Long.valueOf( recipeIdString );
        Long recipeId = Utils.parseLongInputValue( LABEL_RECIPE_ID, recipeIdString );

        if ( !recipeService.validateRecipeId( recipeId ) )
        {
            // throw new RuntimeException("Invalid Recipe ID: " + recipeId ); // TODO throw NotFoudException
            throw new NotFoundException("Recipe ID Not Found ( " + recipeId + " )");
        }

        log.debug(">> Constructing new IngredientCommand object.");

        IngredientCommand ingredientCommand = new IngredientCommand();

        ingredientCommand.setRecipeId( recipeId );
        ingredientCommand.setUom( new UnitOfMeasureCommand() );

        log.debug(">> Using UnitOfMeasureService to load Set of Units of Measure.");

        Set<UnitOfMeasureCommand> unitOfMeasureCommandSet = unitOfMeasureService.listAllUoms();

        model.addAttribute( MODEL_ATTRIBUTE_INGREDIENT, ingredientCommand );
        model.addAttribute( MODEL_ATTRIBUTE_UOM_LIST, unitOfMeasureCommandSet );

        return "/recipe/ingredient/ingredientform";
    }

    @GetMapping("/recipe/{recipeIdString}/ingredient/{ingredientIdString}/delete")
    public String deleteIngredient( Model model,
                                    @PathVariable String recipeIdString,
                                    @PathVariable String ingredientIdString )
    {
        log.debug("Deleting Recipe Ingredient. ( recipeId: " + recipeIdString + ", ingredientId: " + ingredientIdString + " )" );

        // Long recipeId = Long.valueOf( recipeIdString );
        // Long ingredientId = Long.valueOf( ingredientIdString );
        Long recipeId = Utils.parseLongInputValue( LABEL_RECIPE_ID, recipeIdString );
        Long ingredientId = Utils.parseLongInputValue( LABEL_INGREDIENT_ID, ingredientIdString );

        log.debug(">> Using IngredientService to Delete Recipe Ingredient.");

        int numRemainIngredients =
                ingredientService.deleteByRecipeIdAndIngredientId( recipeId, ingredientId );

        log.debug(">> Number of remaining ingredients: " + numRemainIngredients );

        log.debug(">> Using RecipeService to get updated RecipeCommand object.");

        RecipeCommand recipeCommand = recipeService.getRecipeCommandById( recipeId );

        model.addAttribute( MODEL_ATTRIBUTE_RECIPE, recipeCommand );

        return "redirect:/recipe/" + recipeIdString + "/ingredients";
    }

}
