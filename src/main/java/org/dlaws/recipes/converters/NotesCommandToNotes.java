package org.dlaws.recipes.converters;

import lombok.Synchronized;
import org.dlaws.recipes.commands.NotesCommand;
import org.dlaws.recipes.domain.Notes;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class NotesCommandToNotes implements Converter< NotesCommand, Notes >
{
    @Synchronized
    @Nullable
    @Override
    public Notes convert( NotesCommand source )
    {
        if ( source == null )
        {
            return null;
        }

        final Notes notesEntity = new Notes();

        notesEntity.setId(source.getId());
        notesEntity.setRecipeNotes(source.getRecipeNotes());

        return notesEntity;
    }
}
