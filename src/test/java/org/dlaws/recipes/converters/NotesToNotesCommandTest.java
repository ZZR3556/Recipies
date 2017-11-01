package org.dlaws.recipes.converters;

import org.dlaws.recipes.commands.NotesCommand;
import org.dlaws.recipes.domain.Notes;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class NotesToNotesCommandTest
{
    private static Long TEST_ID = new Long(1 );
    private static String TEST_NOTES = "Test Notes.";

    private NotesToNotesCommand converter;

    @Before
    public void setUp() throws Exception
    {
        converter = new NotesToNotesCommand();
    }

    @Test
    public void testNullSource() throws Exception
    {
        assertNull( converter.convert(null ));
    }

    @Test
    public void testEmptySource() throws Exception
    {
        assertNotNull( converter.convert( new Notes()));
    }

    @Test
    public void convert() throws Exception
    {
        // given
        Notes notesEntity = new Notes();

        notesEntity.setId( TEST_ID );
        notesEntity.setRecipeNotes( TEST_NOTES );

        // when
        NotesCommand notesCommand = converter.convert( notesEntity );

        // then
        assertNotNull( notesCommand );
        assertEquals( TEST_ID, notesCommand.getId() );
        assertEquals( TEST_NOTES, notesCommand.getRecipeNotes() );
    }

}