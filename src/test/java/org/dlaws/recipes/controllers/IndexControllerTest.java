package org.dlaws.recipes.controllers;

import org.dlaws.recipes.domain.Recipe;
import org.dlaws.recipes.services.RecipeService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

public class IndexControllerTest
{
    IndexController indexController;

    @Mock
    RecipeService recipeService;

    @Mock
    Model model;

    @Before
    public void setUp() throws Exception
    {
        MockitoAnnotations.initMocks(this );

        indexController = new IndexController( recipeService );
    }

    @Test
    public void testShowIndexPageMockMVC() throws Exception
    {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(indexController).build();

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().size(1))
                .andExpect(model().attributeExists("recipes"));
    }

    @Test
    public void testShowIndexPageBasic() throws Exception
    {
        // ***** Given *****

        Recipe recipe = new Recipe();
        HashSet<Recipe> recipesTestSet = new HashSet<>();
        recipesTestSet.add( recipe );

        when(recipeService.getRecipes()).thenReturn(recipesTestSet);

        ArgumentCaptor<Set<Recipe>> recipeSetCaptor = ArgumentCaptor.forClass(Set.class);

        // ***** When *****

        String indexPage = indexController.getIndexPage( model );

        // ***** Then *****

        assertEquals("index", indexPage );

        verify( recipeService, times(1)).getRecipes();

        verify( model, times(1) ).addAttribute( "recipes", recipesTestSet );

        verify( model, times(1) ).addAttribute( eq("recipes"), anySet() );

        verify( model, times(1) ).addAttribute( eq("recipes"), recipeSetCaptor.capture() );
        Set<Recipe> recipeSetCaptured = recipeSetCaptor.getValue();
        assertEquals(1, recipeSetCaptured.size() );
    }

}