package org.dlaws.recipes.converters;

import lombok.Synchronized;
import org.dlaws.recipes.commands.CategoryCommand;
import org.dlaws.recipes.commands.IngredientCommand;
import org.dlaws.recipes.commands.RecipeCommand;
import org.dlaws.recipes.domain.Category;
import org.dlaws.recipes.domain.Ingredient;
import org.dlaws.recipes.domain.Recipe;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class RecipeToRecipeCommand implements Converter< Recipe, RecipeCommand >
{
    private NotesToNotesCommand notesConverter;
    private CategoryToCategoryCommand categoryConverter;
    private IngredientToIngredientCommand ingredientConverter;

    public RecipeToRecipeCommand( NotesToNotesCommand notesConverter,
                                  CategoryToCategoryCommand categoryConverter,
                                  IngredientToIngredientCommand ingredientConverter )
    {
        this.notesConverter = notesConverter;
        this.categoryConverter = categoryConverter;
        this.ingredientConverter = ingredientConverter;
    }

    @Synchronized
    @Nullable
    @Override
    public RecipeCommand convert( Recipe source )
    {
        if ( source == null )
        {
            return null;
        }

        final RecipeCommand recipeCommand = new RecipeCommand();

        recipeCommand.setId(source.getId());
        recipeCommand.setDescription(source.getDescription());
        recipeCommand.setPrepTime(source.getPrepTime());
        recipeCommand.setCookTime(source.getCookTime());
        recipeCommand.setServings(source.getServings());
        recipeCommand.setUrl(source.getUrl());
        recipeCommand.setDirections(source.getDirections());
        recipeCommand.setDifficulty(source.getDifficulty());

        recipeCommand.setNotes(notesConverter.convert(source.getNotes()));

        Set<Category> sourceCategories = source.getCategories();
        if ( sourceCategories != null && sourceCategories.size() > 0 )
        {
            sourceCategories.forEach( category -> recipeCommand.addCategory(categoryConverter.convert(category)));
        }

        Set<Ingredient> sourceIngredients = source.getIngredients();
        if ( sourceIngredients != null && sourceIngredients.size() > 0 )
        {
            sourceIngredients.forEach( ingredient -> recipeCommand.addIngredient(ingredientConverter.convert(ingredient)));
        }

//        recipeCommand.setImage(source.getImage());

        return recipeCommand;
    }
}
