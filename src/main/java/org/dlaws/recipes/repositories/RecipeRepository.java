package org.dlaws.recipes.repositories;

import org.dlaws.recipes.domain.Recipe;
import org.springframework.data.repository.CrudRepository;

public interface RecipeRepository extends CrudRepository< Recipe, Long >
{
}
