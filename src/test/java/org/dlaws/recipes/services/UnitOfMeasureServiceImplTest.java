package org.dlaws.recipes.services;

import org.dlaws.recipes.commands.UnitOfMeasureCommand;
import org.dlaws.recipes.converters.UnitOfMeasureToUnitOfMeasureCommand;
import org.dlaws.recipes.domain.UnitOfMeasure;
import org.dlaws.recipes.repositories.UnitOfMeasureRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UnitOfMeasureServiceImplTest
{
    @Mock
    private UnitOfMeasureRepository unitOfMeasureRepository;

    private UnitOfMeasureToUnitOfMeasureCommand unitOfMeasureToUnitOfMeasureCommand = new UnitOfMeasureToUnitOfMeasureCommand();

    private UnitOfMeasureService unitOfMeasureService;

    @Before
    public void setup() throws Exception
    {
        MockitoAnnotations.initMocks(this);

        unitOfMeasureService = new UnitOfMeasureServiceImpl( unitOfMeasureRepository, unitOfMeasureToUnitOfMeasureCommand );
    }

    @Test
    public void listAllUoms() throws Exception
    {
        // given
        Set<UnitOfMeasure> unitOfMeasureSet = new HashSet<>();

        UnitOfMeasure uom1 = new UnitOfMeasure();
        uom1.setId( 1L );
        unitOfMeasureSet.add( uom1 );

        UnitOfMeasure uom2 = new UnitOfMeasure();
        uom1.setId( 2L );
        unitOfMeasureSet.add( uom2 );

        when(unitOfMeasureRepository.findAll()).thenReturn(unitOfMeasureSet);

        // when
        Set<UnitOfMeasureCommand> unitOfMeasureCommands = unitOfMeasureService.listAllUoms();

        // then
        assertEquals(2, unitOfMeasureCommands.size());
        verify( unitOfMeasureRepository, times(1)).findAll();
    }

}
