package org.dlaws.recipes.controllers;

import org.dlaws.recipes.commands.RecipeCommand;
import org.dlaws.recipes.domain.Recipe;
import org.dlaws.recipes.services.RecipeService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.regex.Matcher;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

public class RecipeControllerTest
{
    @Mock
    RecipeService recipeService;

    MockMvc mockMvc;

    @Before
    public void setUp() throws Exception
    {
        MockitoAnnotations.initMocks(this );

        RecipeController recipeController = new RecipeController( recipeService );

        mockMvc = MockMvcBuilders.standaloneSetup(recipeController).build();
    }

    @Test
    public void testGetRecipe() throws Exception
    {
        Long testRecipeId = Long.valueOf(1);

        // Prepare RecipeCommand object to be returned by recipeService.getRecipeCommandById().

        RecipeCommand testRecipe = new RecipeCommand();

        testRecipe.setId( testRecipeId );

        when(recipeService.getRecipeCommandById(anyLong())).thenReturn( testRecipe );

        // when / then
        mockMvc.perform(get( "/recipe/" + testRecipeId + "/show" ))
                .andExpect(status().isOk())
                .andExpect(view().name( "/recipe/show" ))
                .andExpect(model().size(1))
                .andExpect(model().attributeExists( "recipe" ))
                .andExpect(model().attribute("recipe", testRecipe ));
    }

    @Test
    public void testGetNewRecipeForm() throws Exception
    {
        mockMvc.perform(get( "/recipe/new" ))
                .andExpect(status().isOk())
                .andExpect(view().name( "/recipe/recipeform" ))
                .andExpect(model().size(1))
                .andExpect(model().attributeExists( "recipe" ))
                .andExpect(model().attribute( "recipe", instanceOf(RecipeCommand.class) ));
    }

    @Test
    public void testPostNewRecipeForm() throws Exception
    {
        Long testRecipeId = Long.valueOf(2);

        // Prepare RecipeCommand object to be returned by recipeService.storeRecipe();

        RecipeCommand testRecipeCommand = new RecipeCommand();

        testRecipeCommand.setId( testRecipeId );

        when(recipeService.storeRecipe(any())).thenReturn( testRecipeCommand );

        // when / then

        mockMvc.perform( post("/recipe" )
                         .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                         .param("id", "" )
                         .param( "description", "Secret Sauce" ) )
                .andExpect( status().is3xxRedirection() )
                .andExpect( view().name("redirect:/recipe/" + testRecipeId + "/show" ) );

        verify( recipeService, times( 1 )).storeRecipe(any(RecipeCommand.class));
    }

    @Test
    public void testGetUpdateView() throws Exception
    {
        Long testRecipeId = Long.valueOf(2);

        // Prepare RecipeCommand object to be returned by recipeService.getRecipeCommandById().

        RecipeCommand testRecipeCommand = new RecipeCommand();

        testRecipeCommand.setId( testRecipeId );

        when(recipeService.getRecipeCommandById(anyLong())).thenReturn( testRecipeCommand );

        // when / then

        mockMvc.perform( get( "/recipe/" + testRecipeId + "/update" ))
                .andExpect(status().isOk())
                .andExpect(view().name( "/recipe/recipeform" ))
                .andExpect(model().attributeExists( "recipe" ))
                .andExpect(model().attribute("recipe", testRecipeCommand ));

        verify( recipeService, times( 1 )).getRecipeCommandById(anyLong());
        verify( recipeService, times( 1 )).getRecipeCommandById( testRecipeId );
    }

    @Test
    public void testDeleteAction() throws Exception
    {
        Long testRecipeId = Long.valueOf(1);

        // when / then

        mockMvc.perform( get( "/recipe/" + testRecipeId + "/delete"))
                .andExpect( status().is3xxRedirection() )
                .andExpect( view().name( "redirect:/"));

        verify( recipeService, times(1)).deleteRecipeById(anyLong());
        verify( recipeService, times(1)).deleteRecipeById( testRecipeId );
    }
}
