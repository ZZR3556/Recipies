package org.dlaws.recipes.bootstrap;

import lombok.extern.slf4j.Slf4j;
import org.dlaws.recipes.domain.Category;
import org.dlaws.recipes.domain.UnitOfMeasure;
import org.dlaws.recipes.repositories.CategoryRepository;
import org.dlaws.recipes.repositories.RecipeRepository;
import org.dlaws.recipes.repositories.UnitOfMeasureRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@Profile( { "dev", "prod" } )
public class RecipeBootstrapMySQL implements ApplicationListener<ContextRefreshedEvent>
{
    private final RecipeRepository recipeRepository;
    private final CategoryRepository categoryRepository;
    private final UnitOfMeasureRepository unitOfMeasureRepository;

    public RecipeBootstrapMySQL( RecipeRepository recipeRepository,
                                 CategoryRepository categoryRepository,
                                 UnitOfMeasureRepository unitOfMeasureRepository )
    {
        log.debug("In Constructor.");

        this.recipeRepository = recipeRepository;
        this.categoryRepository = categoryRepository;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
    }

    @Override
    @Transactional
    public void onApplicationEvent( ContextRefreshedEvent contextRefreshedEvent )
    {
        if ( categoryRepository.count() == 0L )
        {
            log.debug("Loading Categories...");
            loadCategories();
        }

        if ( unitOfMeasureRepository.count() == 0L )
        {
            log.debug("Loading Units Of Measure...");
            loadUnitsOfMeasure();
        }
    }

    private void loadCategories()
    {
        newCategory("American");
        newCategory("Italian");
        newCategory("Mexican");
        newCategory("Fast Food");
    }

    private void newCategory( String description )
    {
        Category cat = new Category();
        cat.setDescription( description );
        categoryRepository.save( cat );
    }

    private void loadUnitsOfMeasure()
    {
        newUnitOfMeasure("");
        newUnitOfMeasure("Teaspoon");
        newUnitOfMeasure("Tablespoon");
        newUnitOfMeasure("Cup");
        newUnitOfMeasure("Pinch");
        newUnitOfMeasure("Dash");
        newUnitOfMeasure("Ounce");
        newUnitOfMeasure("Pint");
    }

    private void newUnitOfMeasure( String description )
    {
        UnitOfMeasure uom = new UnitOfMeasure();
        uom.setDescription( description );
        unitOfMeasureRepository.save( uom );
    }

}
