package org.dlaws.recipes.services;

import org.dlaws.recipes.commands.IngredientCommand;
import org.dlaws.recipes.converters.IngredientCommandToIngredient;
import org.dlaws.recipes.converters.IngredientToIngredientCommand;
import org.dlaws.recipes.converters.UnitOfMeasureCommandToUnitOfMeasure;
import org.dlaws.recipes.converters.UnitOfMeasureToUnitOfMeasureCommand;
import org.dlaws.recipes.domain.Ingredient;
import org.dlaws.recipes.domain.Recipe;
import org.dlaws.recipes.repositories.RecipeRepository;
import org.dlaws.recipes.repositories.UnitOfMeasureRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class IngredientServiceImplTest
{
    private final IngredientToIngredientCommand ingredientToIngredientCommand;
    private final IngredientCommandToIngredient ingredientCommandToIngredient;

    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private UnitOfMeasureRepository unitOfMeasureRepository;

    private IngredientService ingredientService;

    public IngredientServiceImplTest()
    {
        this.ingredientToIngredientCommand = new IngredientToIngredientCommand( new UnitOfMeasureToUnitOfMeasureCommand());
        this.ingredientCommandToIngredient = new IngredientCommandToIngredient( new UnitOfMeasureCommandToUnitOfMeasure());
    }

    @Before
    public void setup() throws Exception
    {
        MockitoAnnotations.initMocks(this );

        ingredientService = new IngredientServiceImpl( recipeRepository, unitOfMeasureRepository,
            ingredientToIngredientCommand, ingredientCommandToIngredient );
    }

    private static Ingredient newTestIngredient( Long ingredientId )
    {
        Ingredient ingredient = new Ingredient();
        ingredient.setId(ingredientId);
        return ingredient;
    }

    @Test
    public void testFindByRecipeIdAndIngredientId() throws Exception
    {
        Long testRecipeId = Long.valueOf( 2 );
        Long testIngredientId = Long.valueOf( 3 );

        // given
        Recipe testRecipe = new Recipe();
        testRecipe.setId( testRecipeId );

        testRecipe.addIngredient( newTestIngredient( Long.valueOf(1) ));
        testRecipe.addIngredient( newTestIngredient( Long.valueOf(2) ));
        testRecipe.addIngredient( newTestIngredient( Long.valueOf(3) ));
        testRecipe.addIngredient( newTestIngredient( Long.valueOf(4) ));
        testRecipe.addIngredient( newTestIngredient( Long.valueOf(5) ));

        when(recipeRepository.findById( testRecipeId )).thenReturn( Optional.of( testRecipe ));

        // when
        IngredientCommand ingredientCommand = ingredientService.findByRecipeIdAndIngredientId( testRecipeId, testIngredientId );

        // then
        assertEquals( testIngredientId, ingredientCommand.getId() );
        assertEquals( testRecipeId, ingredientCommand.getRecipeId() );
        verify( recipeRepository, times(1)).findById(anyLong());
        verify( recipeRepository, times(1)).findById( testRecipeId );
    }

    @Test
    public void testSaveIngredientCommand() throws Exception
    {
        Long testRecipeId = Long.valueOf( 2 );
        Long testIngredientId = Long.valueOf( 3 );

        // given
        Recipe testRecipe = new Recipe();

        when(recipeRepository.findById( testRecipeId )).thenReturn( Optional.of( testRecipe ));

        Recipe savedRecipe = new Recipe();

        savedRecipe.addIngredient( newTestIngredient( testIngredientId ));

        when(recipeRepository.save(any())).thenReturn( savedRecipe );

        // when
        IngredientCommand ingredientCommand = new IngredientCommand();
        ingredientCommand.setId( testIngredientId );
        ingredientCommand.setRecipeId( testRecipeId );

        IngredientCommand savedIngredientCommand = ingredientService.saveIngredientCommand( ingredientCommand );

        // then
        assertEquals( testIngredientId, savedIngredientCommand.getId() );
        verify( recipeRepository, times( 1 )).findById(anyLong());
        verify( recipeRepository, times( 1 )).findById( testRecipeId );
        verify( recipeRepository, times( 1 )).save( any( Recipe.class ));
    }

    @Test
    public void testDeleteIngredient() throws Exception
    {
        Long testRecipeId = Long.valueOf(2);
        Long testIngredientId = Long.valueOf(3);

        Ingredient testIngredient1 = newTestIngredient( Long.valueOf(1) );
        Ingredient testIngredient2 = newTestIngredient( Long.valueOf(2) );
        Ingredient testIngredient3 = newTestIngredient( Long.valueOf(3) );
        Ingredient testIngredient4 = newTestIngredient( Long.valueOf(4) );
        Ingredient testIngredient5 = newTestIngredient( Long.valueOf(5) );

        // Prepare Recipe entity to be returned by recipeRepository.findById().

        Recipe testRecipeBefore = new Recipe();

        testRecipeBefore.addIngredient( testIngredient1 );
        testRecipeBefore.addIngredient( testIngredient2 );
        testRecipeBefore.addIngredient( testIngredient3 );
        testRecipeBefore.addIngredient( testIngredient4 );
        testRecipeBefore.addIngredient( testIngredient5 );

        when( recipeRepository.findById( testRecipeId )).thenReturn( Optional.of( testRecipeBefore ));

        // Prepare Recipe entity to be returned by recipeRepository.save().

        Recipe testRecipeAfter = new Recipe();

        testRecipeAfter.addIngredient( testIngredient1 );
        testRecipeAfter.addIngredient( testIngredient2 );
        testRecipeAfter.addIngredient( testIngredient4 );
        testRecipeAfter.addIngredient( testIngredient5 );

        when( recipeRepository.save(any(Recipe.class))).thenReturn( testRecipeAfter );

        // when
        int numRemainingIngredients =
                ingredientService.deleteByRecipeIdAndIngredientId( testRecipeId, testIngredientId );

        // then
        verify( recipeRepository, times( 1 ) ).findById(anyLong());
        verify( recipeRepository, times( 1 ) ).findById( testRecipeId );
        verify( recipeRepository, times( 1 ) ).save(any( Recipe.class ));

        assertEquals( 4, numRemainingIngredients );
    }
}
