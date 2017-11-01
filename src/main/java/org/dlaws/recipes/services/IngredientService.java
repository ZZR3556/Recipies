package org.dlaws.recipes.services;

import org.dlaws.recipes.commands.IngredientCommand;
import org.dlaws.recipes.domain.Ingredient;

public interface IngredientService
{
    IngredientCommand findByRecipeIdAndIngredientId( Long recipeId, Long ingredientId );

    IngredientCommand saveIngredientCommand( IngredientCommand ingredientCommand );

    // Returns remaining number of ingredients.
    int deleteByRecipeIdAndIngredientId( Long recipeId, Long ingredientId );
}
