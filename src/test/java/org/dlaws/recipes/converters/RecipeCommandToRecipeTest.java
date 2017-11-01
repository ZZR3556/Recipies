package org.dlaws.recipes.converters;

import org.dlaws.recipes.commands.CategoryCommand;
import org.dlaws.recipes.commands.IngredientCommand;
import org.dlaws.recipes.commands.NotesCommand;
import org.dlaws.recipes.commands.RecipeCommand;
import org.dlaws.recipes.domain.Difficulty;
import org.dlaws.recipes.domain.Recipe;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class RecipeCommandToRecipeTest
{
    private static Long TEST_ID = new Long( 1 );
    private static String TEST_DESCRIPTION = "Test Recipe";
    private static Integer TEST_PREP_TIME = new Integer(10 );
    private static Integer TEST_COOK_TIME = new Integer(20 );
    private static Integer TEST_SERVINGS = new Integer(6 );
    private static String TEST_SOURCE = "Test Source";
    private static String TEST_URL = "http://test.testsource.test/TestRecipe";
    private static Difficulty TEST_DIFFICULITY = Difficulty.KIND_OF_HARD;
    private static String TEST_DIRECTIONS = "Test Directions";

    private static Long TEST_NOTES_ID = new Long( 1 );

    private static Long TEST_CATEGORY_ID_1 = new Long( 1 );
    private static Long TEST_CATEGORY_ID_2 = new Long( 2 );

    private static Long TEST_INGREDIENT_ID_1 = new Long( 1 );
    private static Long TEST_INGREDIENT_ID_2 = new Long( 2 );
    private static Long TEST_INGREDIENT_ID_3 = new Long( 3 );

    private RecipeCommandToRecipe converter;

    @Before
    public void setUp() throws Exception
    {
        converter = new RecipeCommandToRecipe( new NotesCommandToNotes(),
                new CategoryCommandToCategory(),
                new IngredientCommandToIngredient( new UnitOfMeasureCommandToUnitOfMeasure() ) );
    }

    @Test
    public void testNullSource() throws Exception
    {
        assertNull( converter.convert( null ));
    }

    @Test
    public void testEmptySource() throws Exception
    {
        assertNotNull( converter.convert( new RecipeCommand()));
    }

    @Test
    public void convert() throws Exception
    {
        // given
        RecipeCommand recipeCommand = new RecipeCommand();

        recipeCommand.setId( TEST_ID );
        recipeCommand.setDescription( TEST_DESCRIPTION );
        recipeCommand.setPrepTime( TEST_PREP_TIME );
        recipeCommand.setCookTime( TEST_COOK_TIME );
        recipeCommand.setServings( TEST_SERVINGS );
        recipeCommand.setSource( TEST_SOURCE );
        recipeCommand.setUrl( TEST_URL );
        recipeCommand.setDifficulty( TEST_DIFFICULITY );
        recipeCommand.setDirections( TEST_DIRECTIONS );

        NotesCommand notesCommand = new NotesCommand();
        notesCommand.setId( TEST_NOTES_ID );

        recipeCommand.setNotes( notesCommand );

        CategoryCommand categoryCommand1 = new CategoryCommand();
        categoryCommand1.setId( TEST_CATEGORY_ID_1 );

        recipeCommand.addCategory( categoryCommand1 );

        CategoryCommand categoryCommand2 = new CategoryCommand();
        categoryCommand2.setId( TEST_CATEGORY_ID_2 );

        recipeCommand.addCategory( categoryCommand2 );

        IngredientCommand ingredientCommand1 = new IngredientCommand();
        ingredientCommand1.setId( TEST_INGREDIENT_ID_1 );

        recipeCommand.addIngredient( ingredientCommand1 );

        IngredientCommand ingredientCommand2 = new IngredientCommand();
        ingredientCommand2.setId( TEST_INGREDIENT_ID_2 );

        recipeCommand.addIngredient( ingredientCommand2 );

        IngredientCommand ingredientCommand3 = new IngredientCommand();
        ingredientCommand3.setId( TEST_INGREDIENT_ID_3 );

        recipeCommand.addIngredient( ingredientCommand3 );

        // when
        Recipe recipeEntity = converter.convert( recipeCommand );

        // then
        assertNotNull( recipeEntity );
        assertEquals( TEST_ID, recipeEntity.getId() );
        assertEquals( TEST_DESCRIPTION, recipeEntity.getDescription() );
        assertEquals( TEST_PREP_TIME, recipeEntity.getPrepTime() );
        assertEquals( TEST_COOK_TIME, recipeEntity.getCookTime() );
        assertEquals( TEST_SERVINGS, recipeEntity.getServings() );
        assertEquals( TEST_URL, recipeEntity.getUrl() );
        assertEquals( TEST_DIFFICULITY, recipeEntity.getDifficulty() );
        assertEquals( TEST_DIRECTIONS, recipeEntity.getDirections() );
        assertEquals( TEST_NOTES_ID, recipeEntity.getNotes().getId() );
        assertEquals( 2, recipeEntity.getCategories().size() );
        assertEquals( 3, recipeEntity.getIngredients().size() );
    }

}