package org.dlaws.recipes.services;

import org.dlaws.recipes.commands.RecipeCommand;
import org.dlaws.recipes.converters.RecipeCommandToRecipe;
import org.dlaws.recipes.converters.RecipeToRecipeCommand;
import org.dlaws.recipes.domain.Recipe;
import org.dlaws.recipes.repositories.RecipeRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest // @DataJpaTest
public class RecipeServiceIT
{
    private static final String NEW_DESCRIPTION = "New Description";

    @Autowired
    RecipeService recipeService;

    @Autowired
    RecipeRepository recipeRepository;

//    @Autowired
//    RecipeCommandToRecipe recipeCommandToRecipe;

    @Autowired
    RecipeToRecipeCommand recipeToRecipeCommand;

    @Test
    @Transactional
    public void testStoreRecipeDescription() throws Exception
    {
        // given
        Iterable<Recipe> recipes = recipeRepository.findAll();
        Recipe testRecipe = recipes.iterator().next();
        RecipeCommand testRecipeCommand = recipeToRecipeCommand.convert(testRecipe);

        // when
        testRecipeCommand.setDescription( NEW_DESCRIPTION );
        RecipeCommand savedRecipeCommand = recipeService.storeRecipe( testRecipeCommand );

        // then
        assertEquals( NEW_DESCRIPTION, savedRecipeCommand.getDescription() );
        assertEquals( testRecipe.getId(), savedRecipeCommand.getId() );
        assertEquals( testRecipe.getCategories().size(), savedRecipeCommand.getCategories().size() );
        assertEquals( testRecipe.getIngredients().size(), savedRecipeCommand.getIngredients().size() );
    }
}
