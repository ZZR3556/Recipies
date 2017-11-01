package org.dlaws.recipes.converters;

import lombok.Synchronized;
import org.dlaws.recipes.commands.CategoryCommand;
import org.dlaws.recipes.domain.Category;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class CategoryCommandToCategory implements Converter< CategoryCommand, Category >
{
    @Synchronized
    @Nullable
    @Override
    public Category convert( CategoryCommand source )
    {
        if ( source == null )
        {
            return null;
        }

        final Category categoryEntity = new Category();

        categoryEntity.setId(source.getId());
        categoryEntity.setDescription(source.getDescription());

        return categoryEntity;
    }
}
