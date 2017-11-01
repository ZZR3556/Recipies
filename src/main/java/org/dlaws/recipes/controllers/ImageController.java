package org.dlaws.recipes.controllers;

import org.dlaws.recipes.domain.Recipe;
import org.dlaws.recipes.services.ImageService;
import org.dlaws.recipes.services.RecipeService;
import org.dlaws.utils.Utils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class ImageController
{
    private final ImageService imageService;
    private final RecipeService recipeService;

    public ImageController( ImageService imageService, RecipeService recipeService )
    {
        this.imageService = imageService;
        this.recipeService = recipeService;
    }

    @GetMapping("/recipe/{recipeIdString}/image")
    public String showUploadForm( Model model, @PathVariable String recipeIdString )
    {
        Long recipeId = Long.valueOf( recipeIdString );

        model.addAttribute("recipe", recipeService.getRecipeCommandById( recipeId ));

        return "/recipe/imageuploadform";
    }

    @PostMapping("/recipe/{recipeIdString}/image")
    public String handleImagePost( @PathVariable String recipeIdString,
                                   @RequestParam("imagefile") MultipartFile file  )
    {
        Long recipeId = Long.valueOf( recipeIdString );

        imageService.saveImageFile( recipeId, file );

        return "redirect:/recipe/" + recipeIdString + "/show";
    }

    @GetMapping("/recipe/{recipeIdString}/recipeimage")
    public void renderImageFromDB( @PathVariable String recipeIdString, HttpServletResponse response ) throws IOException
    {
        Long recipeId = Long.valueOf( recipeIdString );

        Recipe recipe = recipeService.getRecipeById( recipeId );
        Byte[] imageBoxedBytes = recipe.getImage();

        if ( imageBoxedBytes != null )
        {
            int imageLength = imageBoxedBytes.length;

            response.setContentType("image/jpeg");
            Utils.writeBytes( response.getOutputStream(), imageBoxedBytes, 0, imageLength );
        }
        else
        {
            // TODO redirect to default image
        }
    }
}
