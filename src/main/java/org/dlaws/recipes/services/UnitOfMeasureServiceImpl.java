package org.dlaws.recipes.services;

import lombok.extern.slf4j.Slf4j;
import org.dlaws.recipes.commands.UnitOfMeasureCommand;
import org.dlaws.recipes.converters.UnitOfMeasureToUnitOfMeasureCommand;
import org.dlaws.recipes.domain.UnitOfMeasure;
import org.dlaws.recipes.repositories.UnitOfMeasureRepository;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@Service
public class UnitOfMeasureServiceImpl implements UnitOfMeasureService
{
    private final UnitOfMeasureRepository unitOfMeasureRepository;
    private final UnitOfMeasureToUnitOfMeasureCommand unitOfMeasureToUnitOfMeasureCommand;

    public UnitOfMeasureServiceImpl( UnitOfMeasureRepository unitOfMeasureRepository,
                                     UnitOfMeasureToUnitOfMeasureCommand unitOfMeasureToUnitOfMeasureCommand )
    {
        this.unitOfMeasureRepository = unitOfMeasureRepository;
        this.unitOfMeasureToUnitOfMeasureCommand = unitOfMeasureToUnitOfMeasureCommand;
    }

    @Override
    public Set<UnitOfMeasureCommand> listAllUoms()
    {
        return StreamSupport.stream(
            unitOfMeasureRepository.findAll().spliterator(), false )
                .map( unitOfMeasureToUnitOfMeasureCommand::convert )
                .collect( Collectors.toSet() );
    }
}
