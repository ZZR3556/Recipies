package org.dlaws.recipes.services;

import lombok.extern.slf4j.Slf4j;
import org.dlaws.recipes.commands.RecipeCommand;
import org.dlaws.recipes.converters.RecipeCommandToRecipe;
import org.dlaws.recipes.converters.RecipeToRecipeCommand;
import org.dlaws.recipes.domain.Recipe;
import org.dlaws.recipes.exceptions.NotFoundException;
import org.dlaws.recipes.repositories.RecipeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
public class RecipeServiceImpl implements RecipeService
{
    private final RecipeRepository recipeRepository;
    private final RecipeCommandToRecipe recipeCommandToRecipe;
    private final RecipeToRecipeCommand recipeToRecipeCommand;

    public RecipeServiceImpl( RecipeRepository recipeRepository,
                              RecipeCommandToRecipe recipeCommandToRecipe,
                              RecipeToRecipeCommand recipeToRecipeCommand )
    {
        this.recipeRepository = recipeRepository;
        this.recipeCommandToRecipe = recipeCommandToRecipe;
        this.recipeToRecipeCommand = recipeToRecipeCommand;
    }

    @Override
    public boolean validateRecipeId( Long id )
    {
        return recipeRepository.existsById( id );
    }

    @Override
    public Set<Recipe> getRecipes()
    {
        log.debug("In Recipe Service getRecipes().");

        Set<Recipe> recipeSet = new HashSet<>();
        recipeRepository.findAll().iterator().forEachRemaining(recipeSet::add);
        return (recipeSet);
    }

    @Override
    public Recipe getRecipeById( Long recipeId )
    {
        Optional<Recipe> recipeOptional = recipeRepository.findById( recipeId );

        if ( !recipeOptional.isPresent() )
        {
            throw new NotFoundException("Recipe ID Not Found ( " + recipeId + " )");
        }

        return recipeOptional.get();
    }

    @Override
    @Transactional
    public RecipeCommand getRecipeCommandById( Long id )
    {
        return recipeToRecipeCommand.convert(getRecipeById(id));
    }

    @Override
    @Transactional // needed for converters // <-- TODO test this
    public RecipeCommand storeRecipe( RecipeCommand recipeCommand )
    {
        Recipe detachedRecipeEntity = recipeCommandToRecipe.convert( recipeCommand );

        Recipe recipeEntity = recipeRepository.save( detachedRecipeEntity );

        log.debug("Saved Recipe: " + recipeEntity.getId());

        return recipeToRecipeCommand.convert( recipeEntity );
    }

    public void deleteRecipeById( Long idToDelete )
    {
        recipeRepository.deleteById( idToDelete );
    }
}
