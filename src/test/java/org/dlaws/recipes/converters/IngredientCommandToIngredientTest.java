package org.dlaws.recipes.converters;

import org.dlaws.recipes.commands.IngredientCommand;
import org.dlaws.recipes.commands.UnitOfMeasureCommand;
import org.dlaws.recipes.domain.Ingredient;
import org.dlaws.recipes.domain.Recipe;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class IngredientCommandToIngredientTest
{
    public static final Recipe TEST_RECIPE = new Recipe();
    public static final BigDecimal TEST_AMOUNT = new BigDecimal("1" );
    public static final String TEST_DESCRIPTION = "Cheesburger";
    public static final Long TEST_ID = new Long( 1 );
    public static final Long TEST_UOM_ID = new Long( 2 );

    IngredientCommandToIngredient converter;

    @Before
    public void setUp() throws Exception
    {
        converter = new IngredientCommandToIngredient( new UnitOfMeasureCommandToUnitOfMeasure());
    }

    @Test
    public void testNullSource() throws Exception
    {
        assertNull( converter.convert( null ));
    }

    @Test
    public void testEmptySource() throws Exception
    {
        assertNotNull( converter.convert( new IngredientCommand()));
    }

    @Test
    public void convert() throws Exception
    {
        // given
        IngredientCommand ingredientCommand = new IngredientCommand();

        ingredientCommand.setId( TEST_ID );
        ingredientCommand.setAmount( TEST_AMOUNT );
        ingredientCommand.setDescription( TEST_DESCRIPTION );

        UnitOfMeasureCommand unitOfMeasureCommand = new UnitOfMeasureCommand();
        unitOfMeasureCommand.setId( TEST_UOM_ID );

        ingredientCommand.setUom( unitOfMeasureCommand );

        // when
        Ingredient ingredientEntity = converter.convert( ingredientCommand );

        // then
        assertNotNull( ingredientEntity );
        assertNotNull( ingredientEntity.getUom() );
        assertEquals( TEST_ID, ingredientEntity.getId() );
        assertEquals( TEST_AMOUNT, ingredientEntity.getAmount() );
        assertEquals( TEST_DESCRIPTION, ingredientEntity.getDescription() );
        assertEquals( TEST_UOM_ID, ingredientEntity.getUom().getId() );
    }

    @Test
    public void convertWithNulUOM() throws Exception
    {
        // given
        IngredientCommand ingredientCommand = new IngredientCommand();

        ingredientCommand.setId( TEST_ID );
        ingredientCommand.setAmount( TEST_AMOUNT );
        ingredientCommand.setDescription( TEST_DESCRIPTION );

        // when
        Ingredient ingredientEntity = converter.convert( ingredientCommand );

        // then
        assertNotNull( ingredientEntity );
        assertNull( ingredientEntity.getUom() );
        assertEquals( TEST_ID, ingredientEntity.getId() );
        assertEquals( TEST_AMOUNT, ingredientEntity.getAmount() );
        assertEquals( TEST_DESCRIPTION, ingredientEntity.getDescription() );
    }

}