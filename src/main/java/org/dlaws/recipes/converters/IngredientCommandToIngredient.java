package org.dlaws.recipes.converters;
import lombok.Synchronized;
import org.dlaws.recipes.commands.IngredientCommand;
import org.dlaws.recipes.domain.Ingredient;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class IngredientCommandToIngredient implements Converter< IngredientCommand, Ingredient >
{
    private final UnitOfMeasureCommandToUnitOfMeasure uomConverter;

    public IngredientCommandToIngredient( UnitOfMeasureCommandToUnitOfMeasure uomConverter )
    {
        this.uomConverter = uomConverter;
    }

    @Synchronized
    @Nullable
    @Override
    public Ingredient convert( IngredientCommand source )
    {
        if ( source == null )
        {
            return null;
        }

        final Ingredient ingredientEntity = new Ingredient();

        ingredientEntity.setId(source.getId());
        ingredientEntity.setAmount(source.getAmount());
        ingredientEntity.setUom(uomConverter.convert(source.getUom()));
        ingredientEntity.setDescription(source.getDescription());

        return ingredientEntity;
    }
}
