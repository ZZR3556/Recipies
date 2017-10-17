package org.dlaws.recipes.domain;

import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class CategoryTest
{
    Category category;

    @Before
    public void setup()
    {
        category = new Category();
    }

    @Test
    public void getIdInitiallyNull() throws Exception
    {
        assertNull( category.getId() );
    }

    @Test
    public void getId() throws Exception
    {
        Long idTestValue = 4L;

        category.setId( idTestValue );

        assertEquals( idTestValue, category.getId() );
    }

    @Test
    public void getDescriptionInitiallyNull() throws Exception
    {
        assertNull( category.getDescription() );
    }

    @Test
    public void getDescription() throws Exception
    {
        String descriptionTestValue = "This is a test.";

        category.setDescription( descriptionTestValue );

        assertEquals( descriptionTestValue, category.getDescription() );
    }

    @Test
    public void getRecipesInitiallyEmptySet() throws Exception
    {
        Set<Recipe> recipeSet = category.getRecipes();

        assertNotNull( recipeSet );

        assertTrue( recipeSet.isEmpty() );
    }

    @Test
    public void getRecipes() throws Exception
    {
        Set<Recipe> recipeSet = new HashSet<>();

        category.setRecipes( recipeSet );

        assertEquals( recipeSet, category.getRecipes() ) ;
    }

}
