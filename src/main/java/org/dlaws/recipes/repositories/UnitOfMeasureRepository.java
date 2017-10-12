package org.dlaws.recipes.repositories;

import org.dlaws.recipes.domain.UnitOfMeasure;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UnitOfMeasureRepository extends CrudRepository< UnitOfMeasure, Long >
{
    Optional<UnitOfMeasure> findByDescription( String description );
}
