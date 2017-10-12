package org.dlaws.recipes.services;

import org.dlaws.recipes.domain.Recipe;

import java.util.Set;

public interface RecipeService
{
    Set<Recipe> getRecipies();
}
