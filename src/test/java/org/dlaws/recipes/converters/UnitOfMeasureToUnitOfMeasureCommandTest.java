package org.dlaws.recipes.converters;

import org.dlaws.recipes.commands.UnitOfMeasureCommand;
import org.dlaws.recipes.domain.UnitOfMeasure;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class UnitOfMeasureToUnitOfMeasureCommandTest
{
    public static final String TEST_DESCRIPTION = "description";
    public static final Long TEST_ID = new Long(1 );

    UnitOfMeasureToUnitOfMeasureCommand converter;

    @Before
    public void setUp() throws Exception
    {
        converter = new UnitOfMeasureToUnitOfMeasureCommand();
    }

    @Test
    public void testNullSource() throws Exception
    {
        assertNull( converter.convert( null ));
    }

    @Test
    public void testEmptySource() throws Exception
    {
        assertNotNull( converter.convert( new UnitOfMeasure()));
    }

    @Test
    public void convert() throws Exception
    {
        // Given
        UnitOfMeasure uomEntity = new UnitOfMeasure();

        uomEntity.setId( TEST_ID );
        uomEntity.setDescription( TEST_DESCRIPTION );

        // when
        UnitOfMeasureCommand uomCommand = converter.convert( uomEntity );

        // then
        assertNotNull( uomCommand );
        assertEquals( TEST_ID, uomCommand.getId() );
        assertEquals( TEST_DESCRIPTION, uomCommand.getDescription() );
    }
}