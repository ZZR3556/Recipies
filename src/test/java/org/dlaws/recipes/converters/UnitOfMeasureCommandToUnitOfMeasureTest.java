package org.dlaws.recipes.converters;

import org.dlaws.recipes.commands.UnitOfMeasureCommand;
import org.dlaws.recipes.domain.UnitOfMeasure;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class UnitOfMeasureCommandToUnitOfMeasureTest
{
    public static final String TEST_DESCRIPTION = "description";
    public static final Long TEST_ID = new Long(1 );

    UnitOfMeasureCommandToUnitOfMeasure converter;

    @Before
    public void setUp() throws Exception
    {
        converter = new UnitOfMeasureCommandToUnitOfMeasure();
    }

    @Test
    public void testNullSource() throws Exception
    {
        assertNull( converter.convert( null ));
    }

    @Test
    public void testEmptySource() throws Exception
    {
        assertNotNull( converter.convert( new UnitOfMeasureCommand()));
    }

    @Test
    public void convert() throws Exception
    {
        // Given
        UnitOfMeasureCommand uomCommand = new UnitOfMeasureCommand();

        uomCommand.setId( TEST_ID );
        uomCommand.setDescription( TEST_DESCRIPTION );

        // when
        UnitOfMeasure uomEntity = converter.convert( uomCommand );

        // then
        assertNotNull( uomEntity );
        assertEquals( TEST_ID, uomEntity.getId() );
        assertEquals( TEST_DESCRIPTION, uomEntity.getDescription() );
    }
}