package org.dlaws.recipes.services;

import lombok.extern.slf4j.Slf4j;
import org.dlaws.recipes.domain.Recipe;
import org.dlaws.recipes.repositories.RecipeRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

import static org.dlaws.utils.Utils.boxBytes;

@Slf4j
@Service
public class ImageServiceImpl implements ImageService
{
    RecipeRepository recipeRepository;

    public ImageServiceImpl( RecipeRepository recipeRepository )
    {
        this.recipeRepository = recipeRepository;
    }

    @Override
    public void saveImageFile( Long recipeId, MultipartFile file )
    {
        log.debug("Saving a file...");

        try
        {
            Optional<Recipe> recipeOptional = recipeRepository.findById( recipeId );

            if ( !recipeOptional.isPresent() )
            {
                log.error("Recipe Entity not found. ( recipeId: " + recipeId + " )");

                throw new RuntimeException("Recipe Entity not found. ( recipeId: " + recipeId + " )");
            }

            Recipe recipe = recipeOptional.get();

            byte[] fileBytes = file.getBytes();
            int fileLength = fileBytes.length;

            Byte[] boxedBytes = boxBytes( fileBytes, 0, fileLength );

            recipe.setImage( boxedBytes );

            recipeRepository.save( recipe );

            log.debug("Saved " + fileLength + " bytes.");
        }
        catch ( IOException e )
        {
            log.error("Error occurred while saving file.", e );

            throw new RuntimeException("Error occurred while saving file.", e );
        }
    }

}
