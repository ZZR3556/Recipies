package org.dlaws.recipes.converters;

import org.dlaws.recipes.commands.IngredientCommand;
import org.dlaws.recipes.commands.UnitOfMeasureCommand;
import org.dlaws.recipes.domain.Ingredient;
import org.dlaws.recipes.domain.Recipe;
import org.dlaws.recipes.domain.UnitOfMeasure;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class IngredientToIngredientCommandTest
{
    private static final Recipe TEST_RECIPE = new Recipe();
    private static final BigDecimal TEST_AMOUNT = new BigDecimal("1" );
    private static final String TEST_DESCRIPTION = "Cheesburger";
    private static final Long TEST_ID = new Long( 1 );
    private static final Long TEST_UOM_ID = new Long( 2 );

    private IngredientToIngredientCommand converter;

    @Before
    public void setUp() throws Exception
    {
        converter = new IngredientToIngredientCommand( new UnitOfMeasureToUnitOfMeasureCommand());
    }

    @Test
    public void testNullSource() throws Exception
    {
        assertNull( converter.convert( null ));
    }

    @Test
    public void testEmptySource() throws Exception
    {
        Ingredient ingredientEntity = new Ingredient();

        assertNotNull( converter.convert( ingredientEntity ));
    }

    @Test
    public void convert() throws Exception
    {
        // given
        Ingredient ingredientEntity = new Ingredient();

        ingredientEntity.setId( TEST_ID );
        ingredientEntity.setAmount( TEST_AMOUNT );
        ingredientEntity.setDescription( TEST_DESCRIPTION );

        UnitOfMeasure unitOfMeasureEntity = new UnitOfMeasure();

        unitOfMeasureEntity.setId( TEST_UOM_ID );

        ingredientEntity.setUom( unitOfMeasureEntity );

        // when
        IngredientCommand ingredientCommand = converter.convert( ingredientEntity );

        // then
        assertNotNull( ingredientCommand );
        assertNotNull( ingredientCommand.getUom() );
        assertEquals( TEST_ID, ingredientCommand.getId() );
        assertEquals( TEST_AMOUNT, ingredientCommand.getAmount() );
        assertEquals( TEST_DESCRIPTION, ingredientCommand.getDescription() );
        assertEquals( TEST_UOM_ID, ingredientCommand.getUom().getId() );
    }

    @Test
    public void convertWithNulUOM() throws Exception
    {
        // given
        Ingredient ingredientEntity = new Ingredient();

        ingredientEntity.setId( TEST_ID );
        ingredientEntity.setAmount( TEST_AMOUNT );
        ingredientEntity.setDescription( TEST_DESCRIPTION );

        // when
        IngredientCommand ingredientCommand = converter.convert( ingredientEntity );

        // then
        assertNotNull( ingredientCommand );
        assertNull( ingredientCommand.getUom() );
        assertEquals( TEST_ID, ingredientCommand.getId() );
        assertEquals( TEST_AMOUNT, ingredientCommand.getAmount() );
        assertEquals( TEST_DESCRIPTION, ingredientCommand.getDescription() );
    }

}