package org.dlaws.recipes.converters;

import org.dlaws.recipes.commands.CategoryCommand;
import org.dlaws.recipes.domain.Category;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CategoryToCategoryCommandTest
{
    private static final Long TEST_ID = new Long(1 );
    private static final String TEST_DESCRIPTION = "testCategory";

    private CategoryToCategoryCommand converter;

    @Before
    public void setUp() throws Exception
    {
        converter = new CategoryToCategoryCommand();
    }

    @Test
    public void testNullSource() throws Exception
    {
        assertNull( converter.convert(null ));
    }

    @Test
    public void testEmptySource() throws Exception
    {
        assertNotNull( converter.convert( new Category()));
    }

    @Test
    public void convert() throws Exception
    {
        // given
        Category categoryEntity = new Category();

        categoryEntity.setId( TEST_ID );
        categoryEntity.setDescription( TEST_DESCRIPTION );

        // when
        CategoryCommand categoryCommand = converter.convert( categoryEntity );

        // then
        assertNotNull( categoryCommand );
        assertEquals( TEST_ID, categoryCommand.getId() );
        assertEquals( TEST_DESCRIPTION, categoryCommand.getDescription() );
    }

}