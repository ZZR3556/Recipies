package org.dlaws.recipes.services;

import lombok.extern.slf4j.Slf4j;
import org.dlaws.recipes.domain.Recipe;
import org.dlaws.recipes.repositories.RecipeRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class RecipeServiceImpl implements RecipeService
{
    private final RecipeRepository recipeRepository;

    public RecipeServiceImpl( RecipeRepository recipeRepository )
    {
        this.recipeRepository = recipeRepository;
    }

    @Override
    public Set<Recipe> getRecipies()
    {
        log.debug("In Recipe Service getRecipies().");

        Set<Recipe> recipeSet = new HashSet<>();
        recipeRepository.findAll().iterator().forEachRemaining(recipeSet::add);
        return (recipeSet);
    }
}
