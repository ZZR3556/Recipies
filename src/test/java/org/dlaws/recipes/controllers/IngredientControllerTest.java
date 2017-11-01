package org.dlaws.recipes.controllers;

import org.dlaws.recipes.commands.IngredientCommand;
import org.dlaws.recipes.commands.RecipeCommand;
import org.dlaws.recipes.services.IngredientService;
import org.dlaws.recipes.services.RecipeService;
import org.dlaws.recipes.services.UnitOfMeasureService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashSet;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

public class IngredientControllerTest
{
    @Mock
    RecipeService recipeService;

    @Mock
    IngredientService ingredientService;

    @Mock
    UnitOfMeasureService unitOfMeasureService;

    MockMvc mockMvc;

    @Before
    public void setUp() throws Exception
    {
        MockitoAnnotations.initMocks( this );

        IngredientController ingredientController =
            new IngredientController( recipeService, ingredientService, unitOfMeasureService );

        mockMvc = MockMvcBuilders.standaloneSetup( ingredientController ).build();
    }

    @Test
    public void testListIngredients() throws Exception
    {
        Long testRecipeId = Long.valueOf("1");

        // Prepare RecipeCommand object to be returned by recipeService.getRecipeCommandById().

        RecipeCommand recipeCommand = new RecipeCommand();

        when(recipeService.getRecipeCommandById( testRecipeId )).thenReturn( recipeCommand );

        // when / then

        mockMvc.perform( get("/recipe/" + testRecipeId + "/ingredients" ))
                .andExpect(status().isOk())
                .andExpect(view().name( "/recipe/ingredient/list" ))
                .andExpect(model().attributeExists( "recipe" ));

        verify( recipeService,times(1)).getRecipeCommandById(anyLong());
        verify( recipeService,times(1)).getRecipeCommandById( testRecipeId );
    }

    @Test
    public void testShowIngredient() throws Exception
    {
        // Prepare IngredientCommand object to be retunred by ingredientService.findByRecipeIdAndIngredientId().

        IngredientCommand ingredientCommand = new IngredientCommand();

        when(ingredientService.findByRecipeIdAndIngredientId(anyLong(),anyLong())).thenReturn( ingredientCommand );

        // when / then

        mockMvc.perform( get( "/recipe/1/ingredient/2/show" ))
                .andExpect(status().isOk())
                .andExpect(view().name( "/recipe/ingredient/show" ))
                .andExpect(model().attributeExists( "ingredient" ));
    }

    @Test
    public void testNewRecipeIngredientForm() throws Exception
    {
        Long testRecipeId = Long.valueOf(1);

        // Prepare RecipeService validateRecipeId() response.

        when(recipeService.validateRecipeId(anyLong())).thenReturn(true);

        // Prepare UnitOfMeasure object Set to be returned by UnitOfMeasureService.

        when(unitOfMeasureService.listAllUoms()).thenReturn( new HashSet<>());

        // when / then

        mockMvc.perform( get( "/recipe/" + testRecipeId + "/ingredient/new" ))
                .andExpect(status().isOk())
                .andExpect(view().name( "/recipe/ingredient/ingredientform" ))
                .andExpect(model().attributeExists( "ingredient" ))
                .andExpect(model().attributeExists( "uomList" ));

        verify( recipeService, times( 1 )).validateRecipeId(anyLong());
        verify( recipeService, times( 1 )).validateRecipeId( testRecipeId );
    }

    @Test
    public void testUpdateRecipeIngredientForm() throws Exception
    {
        Long testRecipeId = Long.valueOf( 1 );
        Long testIngredientId = Long.valueOf( 2 );

        // Prepare IngredientCommand object to be returned by ingredientService.findByRecipeIdAndIngredientId().

        IngredientCommand ingredientCommand = new IngredientCommand();

        when( ingredientService.findByRecipeIdAndIngredientId( testRecipeId, testIngredientId )).thenReturn( ingredientCommand );

        // Prepare UnitOfMeasure object Set to be returned by unitOfMeasureService.

        when( unitOfMeasureService.listAllUoms()).thenReturn( new HashSet<>());

        // when / then

        mockMvc.perform( get( "/recipe/" + testRecipeId + "/ingredient/" + testIngredientId + "/update" ))
                .andExpect( status().isOk() )
                .andExpect( view().name( "/recipe/ingredient/ingredientform" ))
                .andExpect( model().attributeExists( "ingredient" ) )
                .andExpect( model().attributeExists( "uomList" ));
    }

    @Test
    public void testSaveOrUpdateRecipeIngredient() throws Exception
    {
        Long testRecipeId = Long.valueOf( 2 );
        Long testIngredientId = Long.valueOf( 3 );

        // Prepare IngredientCommand object to be returned by ingredientService.saveIngredientCommand().

        IngredientCommand ingredientCommand = new IngredientCommand();

        ingredientCommand.setId( testIngredientId );
        ingredientCommand.setRecipeId( testRecipeId );

        when( ingredientService.saveIngredientCommand(any())).thenReturn( ingredientCommand );

        // when / then

        mockMvc.perform( post( "/recipe/" + testRecipeId + "/ingredient" )
                         .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                         .param( "id", "" )
                         .param( "description", "Test Description of Ingredient" ) )
                .andExpect( status().is3xxRedirection() )
                .andExpect( view().name( "redirect:/recipe/" + testRecipeId + "/ingredient/" + testIngredientId + "/show" ));
    }

    @Test
    public void testDeleteRecipeIngredient() throws  Exception
    {
        Long testRecipeId = Long.valueOf( 2 );
        Long testIngredientId = Long.valueOf( 3 );

        // Prepare RecipeCommand object to be returned by recipeService.getRecipeCommandById()

        RecipeCommand recipeCommand = new RecipeCommand();

        when(recipeService.getRecipeCommandById( testRecipeId )).thenReturn( recipeCommand );

        // when / then

        mockMvc.perform( get("/recipe/" + testRecipeId + "/ingredient/" + testIngredientId + "/delete" ))
                .andExpect( status().is3xxRedirection() )
                .andExpect( model().attributeExists("recipe" ))
                .andExpect( model().attribute( "recipe", recipeCommand ))
                .andExpect( view().name( "redirect:/recipe/" + testRecipeId +"/ingredients" ));

        verify( ingredientService, times(1)).deleteByRecipeIdAndIngredientId(anyLong(),anyLong());
        verify( ingredientService, times(1)).deleteByRecipeIdAndIngredientId( testRecipeId, testIngredientId );

        verify( recipeService, times(1)).getRecipeCommandById(anyLong());
        verify( recipeService, times(1)).getRecipeCommandById( testRecipeId );
    }

}
