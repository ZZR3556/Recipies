package org.dlaws.recipes.services;

import org.dlaws.recipes.commands.UnitOfMeasureCommand;
import org.dlaws.recipes.domain.UnitOfMeasure;

import java.util.Set;

public interface UnitOfMeasureService
{
    Set<UnitOfMeasureCommand> listAllUoms();
}
