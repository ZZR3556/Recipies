package org.dlaws.recipes.services;

import com.sun.org.apache.regexp.internal.RE;
import org.dlaws.recipes.converters.*;
import org.dlaws.recipes.domain.Recipe;
import org.dlaws.recipes.repositories.RecipeRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

public class RecipeServiceImplTest
{
    RecipeServiceImpl recipeService;

    @Mock
    RecipeRepository recipeRepository;

    @Before
    public void setUp() throws Exception
    {
        MockitoAnnotations.initMocks(this );

        recipeService = new RecipeServiceImpl( recipeRepository,
                new RecipeCommandToRecipe( new NotesCommandToNotes(),
                        new CategoryCommandToCategory(),
                        new IngredientCommandToIngredient( new UnitOfMeasureCommandToUnitOfMeasure() ) ),
                new RecipeToRecipeCommand( new NotesToNotesCommand(),
                        new CategoryToCategoryCommand(),
                        new IngredientToIngredientCommand( new UnitOfMeasureToUnitOfMeasureCommand() ) ) );
    }

    @Test
    public void getRecipeByIdTest() throws Exception
    {
        Long testRecipeId = Long.valueOf("1");

        Recipe recipe = new Recipe();
        recipe.setId(testRecipeId);
        Optional<Recipe> recipeOptional = Optional.of(recipe);

        when( recipeRepository.findById(anyLong())).thenReturn(recipeOptional);

        Recipe recipeReturned = recipeService.getRecipeById(testRecipeId);

        assertNotNull( "recipeService.findById(targetId) returned null", recipeReturned );
        verify( recipeRepository, times(1) ).findById(anyLong());
        verify( recipeRepository, times(1) ).findById(testRecipeId);
        verify( recipeRepository, never() ).findAll();
    }

    @Test
    public void getRecipesTest() throws Exception
    {
        Recipe recipe = new Recipe();
        HashSet<Recipe> recipesTestSet = new HashSet<>();
        recipesTestSet.add( recipe );

        when(recipeService.getRecipes()).thenReturn(recipesTestSet);

        Set<Recipe> recipesReturned = recipeService.getRecipes();

        assertEquals( 1, recipesReturned.size() );
        verify( recipeRepository, times(1)).findAll();
        verify( recipeRepository, never() ).findById(anyLong());
    }

    @Test
    public void testDeleteById() throws  Exception
    {
        Long testRecipeId = Long.valueOf(2);

        recipeService.deleteRecipeById(testRecipeId);

        verify( recipeRepository, times(1)).deleteById(anyLong());
        verify( recipeRepository, times(1)).deleteById(testRecipeId);
    }
}
