package org.dlaws.recipes.services;

import org.dlaws.recipes.domain.Recipe;
import org.dlaws.recipes.repositories.RecipeRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

public class RecipeServiceImplTest
{
    RecipeServiceImpl recipeService;

    @Mock
    RecipeRepository recipeRepository;

    @Before
    public void setUp() throws Exception
    {
        MockitoAnnotations.initMocks(this );

        recipeService = new RecipeServiceImpl( recipeRepository );
    }

    @Test
    public void getRecipes() throws Exception
    {
        Recipe recipe = new Recipe();
        HashSet<Recipe> recipesTestSet = new HashSet<>();
        recipesTestSet.add( recipe );

        when(recipeService.getRecipes()).thenReturn(recipesTestSet);

        Set<Recipe> recipesReturned = recipeService.getRecipes();

        assertEquals( 1, recipesReturned.size() );

        verify( recipeRepository, times(1)).findAll();
    }

}