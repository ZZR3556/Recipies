package org.dlaws.recipes.converters;

import org.dlaws.recipes.commands.CategoryCommand;
import org.dlaws.recipes.domain.Category;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CategoryCommandToCategoryTest
{
    private static final Long TEST_ID = new Long(1 );
    private static final String TEST_DESCRIPTION = "testCategory";

    private CategoryCommandToCategory converter;

    @Before
    public void setUp() throws Exception
    {
        converter = new CategoryCommandToCategory();
    }

    @Test
    public void testNullSource() throws Exception
    {
        assertNull( converter.convert(null ));
    }

    @Test
    public void testEmptySource() throws Exception
    {
        assertNotNull( converter.convert( new CategoryCommand()));
    }

    @Test
    public void convert() throws Exception
    {
        // given
        CategoryCommand categoryCommand = new CategoryCommand();

        categoryCommand.setId( TEST_ID );
        categoryCommand.setDescription( TEST_DESCRIPTION );

        // when
        Category categoryEntity = converter.convert(categoryCommand);

        // then
        assertNotNull( categoryEntity );
        assertEquals( TEST_ID, categoryEntity.getId() );
        assertEquals( TEST_DESCRIPTION, categoryEntity.getDescription() );
    }

}