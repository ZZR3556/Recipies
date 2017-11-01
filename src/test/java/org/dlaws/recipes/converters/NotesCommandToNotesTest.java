package org.dlaws.recipes.converters;

import org.dlaws.recipes.commands.NotesCommand;
import org.dlaws.recipes.domain.Notes;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class NotesCommandToNotesTest
{
    private static Long TEST_ID = new Long(1 );
    private static String TEST_NOTES = "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor.";

    private NotesCommandToNotes converter;

    @Before
    public void setUp() throws Exception
    {
        converter = new NotesCommandToNotes();
    }

    @Test
    public void testNullSource() throws Exception
    {
        assertNull( converter.convert(null ));
    }

    @Test
    public void testEmptySource() throws Exception
    {
        assertNotNull( converter.convert( new NotesCommand()));
    }

    @Test
    public void convert() throws Exception
    {
        // given
        NotesCommand notesCommand = new NotesCommand();

        notesCommand.setId( TEST_ID );
        notesCommand.setRecipeNotes( TEST_NOTES );

        // when
        Notes notesEntity = converter.convert( notesCommand );

        // then
        assertNotNull( notesEntity );
        assertEquals( TEST_ID, notesEntity.getId() );
        assertEquals( TEST_NOTES, notesEntity.getRecipeNotes() );
    }

}