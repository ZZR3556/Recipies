package org.dlaws.recipes.converters;

import lombok.Synchronized;
import org.dlaws.recipes.commands.CategoryCommand;
import org.dlaws.recipes.commands.IngredientCommand;
import org.dlaws.recipes.commands.RecipeCommand;
import org.dlaws.recipes.domain.Recipe;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class RecipeCommandToRecipe implements Converter< RecipeCommand, Recipe >
{
    private NotesCommandToNotes notesConverter;
    private CategoryCommandToCategory categoryConverter;
    private IngredientCommandToIngredient ingredientConverter;

    public RecipeCommandToRecipe( NotesCommandToNotes notesConverter,
                                  CategoryCommandToCategory categoryConverter,
                                  IngredientCommandToIngredient ingredientConverter )
    {
        this.notesConverter = notesConverter;
        this.categoryConverter = categoryConverter;
        this.ingredientConverter = ingredientConverter;
    }

    @Synchronized
    @Nullable
    @Override
    public Recipe convert( RecipeCommand source )
    {
        if ( source == null )
        {
            return null;
        }

        final Recipe recipeEntity = new Recipe();

        recipeEntity.setId(source.getId());
        recipeEntity.setDescription(source.getDescription());
        recipeEntity.setPrepTime(source.getPrepTime());
        recipeEntity.setCookTime(source.getCookTime());
        recipeEntity.setServings(source.getServings());
        recipeEntity.setUrl(source.getUrl());
        recipeEntity.setDirections(source.getDirections());
        recipeEntity.setDifficulty(source.getDifficulty());

        recipeEntity.setNotes(notesConverter.convert(source.getNotes()));

        Set<CategoryCommand> sourceCategories = source.getCategories();
        if ( sourceCategories != null && sourceCategories.size() > 0 )
        {
            sourceCategories.forEach( category -> recipeEntity.addCategory(categoryConverter.convert(category)));
        }

        Set<IngredientCommand> sourceIngredients = source.getIngredients();
        if ( sourceIngredients != null && sourceIngredients.size() > 0 )
        {
            sourceIngredients.forEach( ingredient -> recipeEntity.addIngredient(ingredientConverter.convert(ingredient)));
        }

//        recipeEntity.setImage(source.getImage());

        return recipeEntity;
    }
}
