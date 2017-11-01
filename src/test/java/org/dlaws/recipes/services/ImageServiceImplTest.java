package org.dlaws.recipes.services;

import org.dlaws.recipes.domain.Recipe;
import org.dlaws.recipes.repositories.RecipeRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ImageServiceImplTest
{
    @Mock
    RecipeRepository recipeRepository;

    ImageService imageService;

    @Before
    public void setUp() throws Exception
    {
        MockitoAnnotations.initMocks( this );

        imageService = new ImageServiceImpl( recipeRepository );
    }

    @Test
    public void saveImageFile() throws Exception
    {
        Long testRecipeId = Long.valueOf(1);

        // Prepare Recipe entity to be returned by RecipeRepository findById().

        Recipe testRecipe = new Recipe();
        testRecipe.setId( testRecipeId );

        when(recipeRepository.findById(anyLong())).thenReturn( Optional.of( testRecipe ) );

        // Prepare Mock File to be handled by InageService saveFile().

        MultipartFile mockMultipartFile = new MockMultipartFile(
                "imageFile", "testing.txt",
                "text/plain", "Test Contents".getBytes() );

        // when

        ArgumentCaptor<Recipe> argumentCaptor = ArgumentCaptor.forClass( Recipe.class );

        imageService.saveImageFile( testRecipeId, mockMultipartFile );

        // then

        verify(recipeRepository,times( 1 )).save(argumentCaptor.capture());

        Recipe savedRecipe = argumentCaptor.getValue();

        assertEquals( mockMultipartFile.getBytes().length, savedRecipe.getImage().length );
    }

}
