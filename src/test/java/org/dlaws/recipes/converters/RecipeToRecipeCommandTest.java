package org.dlaws.recipes.converters;

import org.dlaws.recipes.commands.CategoryCommand;
import org.dlaws.recipes.commands.IngredientCommand;
import org.dlaws.recipes.commands.NotesCommand;
import org.dlaws.recipes.commands.RecipeCommand;
import org.dlaws.recipes.domain.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class RecipeToRecipeCommandTest
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

    private RecipeToRecipeCommand converter;

    @Before
    public void setUp() throws Exception
    {
        converter = new RecipeToRecipeCommand( new NotesToNotesCommand(), new CategoryToCategoryCommand(),
                new IngredientToIngredientCommand( new UnitOfMeasureToUnitOfMeasureCommand() ) );
    }

    @Test
    public void testNullSource() throws Exception
    {
        assertNull( converter.convert( null ));
    }

    @Test
    public void testEmptySource() throws Exception
    {
        assertNotNull( converter.convert( new Recipe()));
    }

    @Test
    public void convert() throws Exception
    {
        // given
        Recipe recipeEntity = new Recipe();

        recipeEntity.setId( TEST_ID );
        recipeEntity.setDescription( TEST_DESCRIPTION );
        recipeEntity.setPrepTime( TEST_PREP_TIME );
        recipeEntity.setCookTime( TEST_COOK_TIME );
        recipeEntity.setServings( TEST_SERVINGS );
        recipeEntity.setSource( TEST_SOURCE );
        recipeEntity.setUrl( TEST_URL );
        recipeEntity.setDifficulty( TEST_DIFFICULITY );
        recipeEntity.setDirections( TEST_DIRECTIONS );

        Notes notesEntity = new Notes();
        notesEntity.setId( TEST_NOTES_ID );

        recipeEntity.setNotes( notesEntity );

        Category categoryEntity1 = new Category();
        categoryEntity1.setId( TEST_CATEGORY_ID_1 );

        recipeEntity.addCategory( categoryEntity1 );

        Category categoryEntity2 = new Category();
        categoryEntity2.setId( TEST_CATEGORY_ID_2 );

        recipeEntity.addCategory( categoryEntity2 );

        Ingredient ingredientEntity1 = new Ingredient();
        ingredientEntity1.setId( TEST_INGREDIENT_ID_1 );

        recipeEntity.addIngredient( ingredientEntity1 );

        Ingredient ingredientEntity2 = new Ingredient();
        ingredientEntity2.setId( TEST_INGREDIENT_ID_2 );

        recipeEntity.addIngredient( ingredientEntity2 );

        Ingredient ingredientEntity3 = new Ingredient();
        ingredientEntity3.setId( TEST_INGREDIENT_ID_3 );

        recipeEntity.addIngredient( ingredientEntity3 );

        // when
        RecipeCommand recipeCommand = converter.convert( recipeEntity );

        // then
        assertNotNull( recipeCommand );
        assertEquals( TEST_ID, recipeCommand.getId() );
        assertEquals( TEST_DESCRIPTION, recipeCommand.getDescription() );
        assertEquals( TEST_PREP_TIME, recipeCommand.getPrepTime() );
        assertEquals( TEST_COOK_TIME, recipeCommand.getCookTime() );
        assertEquals( TEST_SERVINGS, recipeCommand.getServings() );
        assertEquals( TEST_URL, recipeCommand.getUrl() );
        assertEquals( TEST_DIFFICULITY, recipeCommand.getDifficulty() );
        assertEquals( TEST_DIRECTIONS, recipeCommand.getDirections() );
        assertEquals( TEST_NOTES_ID, recipeCommand.getNotes().getId() );
        assertEquals( 2, recipeCommand.getCategories().size() );
        assertEquals( 3, recipeCommand.getIngredients().size() );
    }

}