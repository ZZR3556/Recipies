package org.dlaws.recipes.controllers;

import org.assertj.core.internal.Bytes;
import org.dlaws.recipes.commands.RecipeCommand;
import org.dlaws.recipes.domain.Recipe;
import org.dlaws.recipes.services.ImageService;
import org.dlaws.recipes.services.RecipeService;
import org.dlaws.utils.Utils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ImageControllerTest
{
    private static final String TEST_RECIPE_ID = "1";

    @Mock
    ImageService imageService;

    @Mock
    RecipeService recipeService;

    ImageController controller;

    MockMvc mockMvc;

    @Before
    public void setUp() throws Exception
    {
        MockitoAnnotations.initMocks(this);

        controller = new ImageController( imageService, recipeService );

        mockMvc = MockMvcBuilders.standaloneSetup( controller ).build();
    }

    @Test
    public void showUploadForm() throws Exception
    {
        Long testRecipeId = Long.valueOf( TEST_RECIPE_ID );

        // Prepare RecipeCommand object to be returned by RecipeService getRecipeCommandById().

        RecipeCommand testRecipeCommand = new RecipeCommand();
        testRecipeCommand.setId( testRecipeId );

        when(recipeService.getRecipeCommandById(anyLong())).thenReturn( testRecipeCommand );

        // when / then

        mockMvc.perform(get( "/recipe/" + TEST_RECIPE_ID + "/image" ))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("recipe"))
                .andExpect(model().attribute("recipe", instanceOf(RecipeCommand.class)))
                .andExpect(model().attribute("recipe", testRecipeCommand ));

        verify(recipeService, times(1)).getRecipeCommandById(anyLong());
        verify(recipeService, times(1)).getRecipeCommandById( testRecipeId );
    }

    @Test
    public void handleImagePost() throws Exception
    {
        Long testRecipeId = Long.valueOf( TEST_RECIPE_ID );

        MockMultipartFile mockMultipartFile = new MockMultipartFile(
                "imagefile", "testing.txt",
                "test/plain", "Test Contents".getBytes());

        mockMvc.perform(multipart( "/recipe/" + TEST_RECIPE_ID + "/image" ).file( mockMultipartFile ))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/recipe/" + TEST_RECIPE_ID + "/show"));

        verify(imageService, times( 1 )).saveImageFile(anyLong(),any());
        verify(imageService, times( 1 )).saveImageFile( testRecipeId, mockMultipartFile );
    }

    @Test
    public  void renderImageFromDB() throws Exception
    {
        Long testRecipeId = Long.valueOf( TEST_RECIPE_ID );

        // Prepare Recipe entity to be returned by RecipeService getRecipeCommandById().

        Recipe testRecipe = new Recipe();
        testRecipe.setId( testRecipeId );

        String testFileContents = "Text File Contents.";
        byte[] testBytes = testFileContents.getBytes();
        Byte[] boxedBytes = Utils.boxBytes( testBytes, 0 , testBytes.length );

        testRecipe.setImage(boxedBytes);

        when(recipeService.getRecipeById(anyLong())).thenReturn( testRecipe );

        // when

        MockHttpServletResponse mockResponse = mockMvc.perform(get("/recipe/" + TEST_RECIPE_ID + "/recipeimage"))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        byte[] responseBytes = mockResponse.getContentAsByteArray();

        assertEquals( testBytes.length, responseBytes.length );
    }
}