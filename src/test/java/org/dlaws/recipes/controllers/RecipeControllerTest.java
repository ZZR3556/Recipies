package org.dlaws.recipes.controllers;

import org.dlaws.recipes.commands.RecipeCommand;
import org.dlaws.recipes.exceptions.NotFoundException;
import org.dlaws.recipes.services.RecipeService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

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
    private RecipeService recipeService;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception
    {
        MockitoAnnotations.initMocks(this );

        RecipeController recipeController = new RecipeController( recipeService );

        mockMvc = MockMvcBuilders.standaloneSetup(recipeController)
                .setControllerAdvice(new ControllerExceptionHandler())
                .build();
    }

    @Test
    public void testGetRecipe() throws Exception
    {
        Long testRecipeId = 1L;

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
    public void testGetRecipeNotFound() throws Exception
    {
        Long testRecipeId = 1L;

        when(recipeService.getRecipeCommandById(anyLong())).thenThrow( NotFoundException.class );

        // when / then
        mockMvc.perform(get( "/recipe/" + testRecipeId + "/show" ))
                .andExpect(status().isNotFound())
                .andExpect(view().name("error"));
    }

    @Test
    public void testGetRecipeNumberFormatException() throws Exception
    {
        String testRecipeId = "adsf";

        // when / then
        mockMvc.perform(get( "/recipe/" + testRecipeId + "/show" ))
                .andExpect(status().isBadRequest())
                .andExpect(view().name("error"));
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
        Long testRecipeId = 2L;

        // Prepare RecipeCommand object to be returned by recipeService.storeRecipe();

        RecipeCommand testRecipeCommand = new RecipeCommand();

        testRecipeCommand.setId( testRecipeId );

        when(recipeService.storeRecipe(any())).thenReturn( testRecipeCommand );

        // when / then

        mockMvc.perform( post("/recipe" )
                         .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                         .param( "id", "" )
                         .param( "description", "Secret Sauce" )
                         .param( "directions", "Directions" ) )
                .andExpect( status().is3xxRedirection() )
                .andExpect( view().name( "redirect:/recipe/" + testRecipeId + "/show" ) );

        verify( recipeService, times( 1 )).storeRecipe(any(RecipeCommand.class));
    }

    @Test
    public void testPostValidationFailure() throws Exception
    {
        Long testRecipeId = 2L;

        // Prepare RecipeCommand object to be returned by recipeService.storeRecipe();

        RecipeCommand recipeCommand = new RecipeCommand();

        recipeCommand.setId( testRecipeId );

        when(recipeService.storeRecipe(any())).thenReturn( recipeCommand );

        mockMvc.perform( post("/recipe")
                         .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                         .param("id","")
                         .param("cookTime", "9000") )
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("recipe"))
                .andExpect(view().name( "/recipe/recipeform" ));
    }

    @Test
    public void testGetUpdateView() throws Exception
    {
        Long testRecipeId = 2L;

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
        Long testRecipeId = 1L;

        // when / then

        mockMvc.perform( get( "/recipe/" + testRecipeId + "/delete"))
                .andExpect( status().is3xxRedirection() )
                .andExpect( view().name( "redirect:/"));

        verify( recipeService, times(1)).deleteRecipeById(anyLong());
        verify( recipeService, times(1)).deleteRecipeById( testRecipeId );
    }
}
