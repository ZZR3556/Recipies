package org.dlaws.recipes.services;

import org.dlaws.recipes.commands.RecipeCommand;
import org.dlaws.recipes.domain.Recipe;

import java.util.Set;

public interface RecipeService
{
    boolean validateRecipeId( Long id );

    Set<Recipe> getRecipes();

    Recipe getRecipeById( Long id );

    RecipeCommand getRecipeCommandById( Long id );

    RecipeCommand storeRecipe( RecipeCommand recipeCommand );

    void deleteRecipeById( Long idToDelete );
}
