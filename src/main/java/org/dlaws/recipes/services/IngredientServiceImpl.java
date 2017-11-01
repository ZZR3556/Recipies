package org.dlaws.recipes.services;

import com.sun.org.apache.regexp.internal.RE;
import lombok.extern.slf4j.Slf4j;
import org.dlaws.recipes.commands.IngredientCommand;
import org.dlaws.recipes.commands.RecipeCommand;
import org.dlaws.recipes.commands.UnitOfMeasureCommand;
import org.dlaws.recipes.converters.IngredientCommandToIngredient;
import org.dlaws.recipes.converters.IngredientToIngredientCommand;
import org.dlaws.recipes.domain.Ingredient;
import org.dlaws.recipes.domain.Recipe;
import org.dlaws.recipes.domain.UnitOfMeasure;
import org.dlaws.recipes.repositories.RecipeRepository;
import org.dlaws.recipes.repositories.UnitOfMeasureRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
public class IngredientServiceImpl implements IngredientService
{
    private final RecipeRepository recipeRepository;
    private final UnitOfMeasureRepository unitOfMeasureRepository;
    private final IngredientToIngredientCommand ingredientToIngredientCommand;
    private final IngredientCommandToIngredient ingredientCommandToIngredient;

    public IngredientServiceImpl( RecipeRepository recipeRepository,
                                  UnitOfMeasureRepository unitOfMeasureRepository,
                                  IngredientToIngredientCommand ingredientToIngredientCommand,
                                  IngredientCommandToIngredient ingredientCommandToIngredient )
    {
        this.recipeRepository = recipeRepository;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
        this.ingredientToIngredientCommand = ingredientToIngredientCommand;
        this.ingredientCommandToIngredient = ingredientCommandToIngredient;
    }

    public IngredientCommand findByRecipeIdAndIngredientId( Long recipeId, Long ingredientId )
    {
        Optional<Recipe> recipeOptional = recipeRepository.findById( recipeId );

        if ( !recipeOptional.isPresent() )
        {
            // TODO implement error handling ( return HTTP error 404 )
            throw new RuntimeException("Recipe Not Found ( recipeId = " + recipeId + " )");
        }

        Recipe recipe = recipeOptional.get();

        Optional<IngredientCommand> ingredientCommandOptional =
            recipe.getIngredients().stream()
                .filter( ingredient -> ingredient.getId().equals( ingredientId ) )
                .map( ingredient -> ingredientToIngredientCommand.convert( ingredient ) )
                .findFirst();

        if ( ! ingredientCommandOptional.isPresent() )
        {
            // TODO implement error handling ( return HTTP error 404 )
            throw new RuntimeException("Ingredient Not Found ( ingredientId = " + ingredientId + " )");
        }

        return ingredientCommandOptional.get();
    }

    @Override
    public IngredientCommand saveIngredientCommand( IngredientCommand ingredientCommand )
    {
        Long recipeId = ingredientCommand.getRecipeId();
        Long ingredientId = ingredientCommand.getId();

        String newIngredientDescription = ingredientCommand.getDescription();

        Optional<Recipe> recipeOptional = recipeRepository.findById( recipeId );

        if ( !recipeOptional.isPresent() )
        {
            log.error("Recipe not found. ( recipeId: " + recipeId + " )");

            // TODO throw Exception if not found.
            return new IngredientCommand();
        }
        else
        {
            Recipe recipe = recipeOptional.get();
            Set<Ingredient> ingredients = recipe.getIngredients();

            Optional<Ingredient> ingredientOptional =
                ingredients.stream()
                    .filter( ingredient -> ingredient.getId().equals( ingredientId ) )
                    .findFirst();

            boolean isUpdatescenario = ingredientOptional.isPresent();

            Ingredient updatedIngredientEntity = null;

            BigDecimal newIngredientAmount = ingredientCommand.getAmount();

            UnitOfMeasureCommand uomCommand = ingredientCommand.getUom();

            if ( ingredientOptional.isPresent() )
            {
                // UPDATE Ingredient scenario
                updatedIngredientEntity = ingredientOptional.get();

                updatedIngredientEntity.setDescription( newIngredientDescription );

                updatedIngredientEntity.setAmount( newIngredientAmount );

                if ( uomCommand == null )
                {
                    throw new RuntimeException("Unit Of Measure not defined in IngredientCommand object.");
                }

                Long newIngredientUomId = uomCommand.getId();

                Optional<UnitOfMeasure> unitOfMeasureOptional = unitOfMeasureRepository.findById( newIngredientUomId );

                if ( !unitOfMeasureOptional.isPresent() )
                {
                    throw new RuntimeException("Unit Of Measure not found. ( id: " + newIngredientUomId + " )" );
                }

                updatedIngredientEntity.setUom( unitOfMeasureOptional.get() );
            }
            else
            {
                // NEW Ingredient scenario
                Ingredient newIngredientEntity = null;

                newIngredientEntity = ingredientCommandToIngredient.convert( ingredientCommand );
                newIngredientEntity.setRecipe( recipe );

                recipe.addIngredient( newIngredientEntity );
            }

            Recipe savedRecipe = recipeRepository.save( recipe );

            Set<Ingredient> savedRecipeIngredientSet = savedRecipe.getIngredients();

            Optional<Ingredient> savedIngredientOptional = savedRecipeIngredientSet.stream()
                    .filter( ingedient -> ingedient.getId().equals( ingredientId ) )
                    .findFirst();

            if ( !savedIngredientOptional.isPresent() )
            {
                log.debug("Ingredient not found by ID ( " + ingredientId + " ), trying to find by matching attributes.");

                savedIngredientOptional = savedRecipeIngredientSet.stream()
                        .filter( ingedient -> ingedient.getDescription().equals( newIngredientDescription ) )
                        .filter( ingedient -> ingedient.getAmount().equals( newIngredientAmount ) )
                        .filter( ingedient -> ingedient.getUom().getId().equals( uomCommand.getId() ) )
                        .findFirst();
            }

            return ingredientToIngredientCommand.convert( savedIngredientOptional.get() );
        }
    }

    @Override
    public int deleteByRecipeIdAndIngredientId( Long recipeId, Long ingredientId )
    {
        // TODO
        Optional<Recipe> recipeOptional = recipeRepository.findById( recipeId );

        if (!recipeOptional.isPresent())
        {
            throw new RuntimeException("Recipe not found ( recipeId: " + recipeId + " )");
        }

        Recipe recipe = recipeOptional.get();

        Optional<Ingredient> ingredientOptional = recipe.getIngredients().stream()
                .filter(ingredient -> ingredient.getId().equals(ingredientId)).findFirst();

        if (!ingredientOptional.isPresent())
        {
            throw new RuntimeException("Ingredient not found ( recipeId: " + recipeId + ", ingredientId: " + ingredientId + " )");
        }

        log.debug("Number of Ingredients (before): " + recipe.getIngredients().size() );

        recipe.removeIngredient( ingredientOptional.get() );

        Recipe savedRecipe = recipeRepository.save( recipe );

        Set<Ingredient> savedRecipeIngredientSet = savedRecipe.getIngredients();

        int numIngredientsAfter = savedRecipeIngredientSet.size();

        log.debug("Number of Ingredients (before): " + numIngredientsAfter );

        log.debug("Saved Recipe object is new object: " + ( recipe != savedRecipe ));

        return numIngredientsAfter;
    }

}
