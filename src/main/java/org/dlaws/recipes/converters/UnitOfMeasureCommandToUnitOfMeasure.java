package org.dlaws.recipes.converters;

import lombok.Synchronized;
import org.dlaws.recipes.commands.UnitOfMeasureCommand;
import org.dlaws.recipes.domain.UnitOfMeasure;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class UnitOfMeasureCommandToUnitOfMeasure implements Converter<UnitOfMeasureCommand, UnitOfMeasure>
{
    @Synchronized
    @Nullable
    @Override
    public UnitOfMeasure convert( UnitOfMeasureCommand source )
    {
        if ( source == null )
        {
            return null;
        }

        final UnitOfMeasure uomEntity = new UnitOfMeasure();

        uomEntity.setId(source.getId());
        uomEntity.setDescription(source.getDescription());

        return uomEntity;
    }
}
