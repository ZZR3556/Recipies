package org.dlaws.recipes.bootstrap;

import lombok.extern.slf4j.Slf4j;
import org.dlaws.recipes.domain.*;
import org.dlaws.recipes.repositories.CategoryRepository;
import org.dlaws.recipes.repositories.RecipeRepository;
import org.dlaws.recipes.repositories.UnitOfMeasureRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Slf4j
@Component
@Profile("default")
public class RecipeBootstrap implements ApplicationListener<ContextRefreshedEvent>
{
    private final RecipeRepository recipeRepository;
    private final CategoryRepository categoryRepository;
    private final UnitOfMeasureRepository unitOfMeasureRepository;

    public RecipeBootstrap( RecipeRepository recipeRepository,
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
        initCategories();
        initUnitsOfMeasure();

        initRecipies();
    }

    private Category catAmerican;
    private Category catItalian;
    private Category catMexican;
    private Category catFastFood;

    private void initCategories()
    {
        catAmerican = getCategory("American");
        catItalian = getCategory("Italian");
        catMexican = getCategory("Mexican");
        catFastFood = getCategory("Fast Food");
    }

    private Category getCategory( String name )
    {
        Optional<Category> categoryOptional = categoryRepository.findByDescription(name);

        if (!categoryOptional.isPresent())
        {
            throw new RuntimeException("Undefined Category: " + name );
        }

        return  categoryOptional.get();
    }

    private UnitOfMeasure uomEach;
    private UnitOfMeasure uomTeaspoon;
    private UnitOfMeasure uomTablespoon;
    private UnitOfMeasure uomCup;
    private UnitOfMeasure uomPinch;
    private UnitOfMeasure uomDash;
    private UnitOfMeasure uomOunce;
    private UnitOfMeasure uomPint;

    private void initUnitsOfMeasure()
    {
        uomEach = getUnitOfMeasure("");
        uomTeaspoon = getUnitOfMeasure("Teaspoon");
        uomTablespoon = getUnitOfMeasure("Tablespoon");
        uomCup = getUnitOfMeasure("Cup");
        uomPinch = getUnitOfMeasure("Pinch");
        uomDash = getUnitOfMeasure("Dash");
        uomOunce = getUnitOfMeasure("Ounce");
        uomPint = getUnitOfMeasure("Pint");
    }

    private UnitOfMeasure getUnitOfMeasure( String name )
    {
        Optional<UnitOfMeasure> unitOfMeasureOptional = unitOfMeasureRepository.findByDescription(name);

        if (!unitOfMeasureOptional.isPresent())
        {
            throw new RuntimeException("Undefined UnitOfMeasure: " + name );
        }

        return unitOfMeasureOptional.get();
    }

    private void initRecipies()
    {
        initPerfectGuacamole();
        initSpicyGrilledChickenTacos();
    }

    private Ingredient newIngredient( String description, String amount, UnitOfMeasure uom )
    {
        return new Ingredient( description, new BigDecimal(amount), uom );
    }

    // Perfect Guacamole
    private void initPerfectGuacamole()
    {
        log.debug("In initPerfectGuacamole()");

        Recipe recipe = new Recipe();

        recipe.setDescription("Perfect Guacamole");

         recipe.addCategory(catMexican);
         recipe.addCategory(catAmerican);

        recipe.setDifficulty(Difficulty.HARD);

        recipe.setPrepTime(10);
        recipe.setCookTime(0);

        recipe.setServings(4);
        recipe.setSource("");
        recipe.setUrl("http://www.simplyrecipes.com/recipes/perfect_guacamole/");

        // 2 ripe avocados
        // 1/2 teaspoon Kosher salt
        // 1 Tbsp of fresh lime juice or lemon juice
        // 2 Tbsp to 1/4 cup of minced red onion or thinly sliced green onion
        // 1-2 serrano chiles, stems and seeds removed, minced
        // 2 tablespoons cilantro (leaves and tender stems), finely chopped
        // A dash of freshly grated black pepper
        // 1/2 ripe tomato, seeds and pulp removed, chopped

        recipe.addIngredient(newIngredient("ripe avocados","2",uomEach));
        recipe.addIngredient(newIngredient("Kosher salt","0.5",uomTablespoon));
        recipe.addIngredient(newIngredient("fresh lime juice or lemon juice","1",uomTablespoon));
        recipe.addIngredient(newIngredient("minced red onion or thinly sliced green onion","0.25",uomCup));
        recipe.addIngredient(newIngredient("serrano chiles, stems and seeds removed, minced","2",uomEach));
        recipe.addIngredient(newIngredient("cilantro (leaves and tender stems), finely chopped","2",uomTablespoon));
        recipe.addIngredient(newIngredient("freshly grated black pepper","1",uomDash));
        recipe.addIngredient(newIngredient("ripe tomato, seeds and pulp removed, chopped","0.5",uomEach));

        recipe.setDirections("1 Cut avocado, remove flesh: Cut the avocados in half. Remove seed." +
            " Score the inside of the avocado with a blunt knife and scoop out the flesh with a spoon\n" +
            "2 Mash with a fork: Using a fork, roughly mash the avocado. (Don't overdo it!" +
            " The guacamole should be a little chunky.)\n" +
            "3 Add salt, lime juice, and the rest: Sprinkle with salt and lime (or lemon) juice." +
            " The acid in the lime juice will provide some balance to the richness of the avocado" +
            " and will help delay the avocados from turning brown.\n" +
            "Add the chopped onion, cilantro, black pepper, and chiles. Chili peppers vary individually" +
            " in their hotness. So, start with a half of one chili pepper and add to the guacamole" +
            " to your desired degree of hotness.\n" +
            "Remember that much of this is done to taste because of the variability in the fresh ingredients." +
            " Start with this recipe and adjust to your taste.\n" +
            "4 Cover with plastic and chill to store: Place plastic wrap on the surface of the guacamole" +
            " cover it and to prevent air reaching it. (The oxygen in the air causes oxidation which will" +
            " turn the guacamole brown.) Refrigerate until ready to serve.\n" +
            "Chilling tomatoes hurts their flavor, so if you want to add chopped tomato to your guacamole," +
            " add it just before serving.\n\n\n" +
            "Read more: http://www.simplyrecipes.com/recipes/perfect_guacamole/#ixzz4jvpiV9Sd");

        Notes notes = new Notes();
        notes.setRecipeNotes("For a very quick guacamole just take a 1/4 cup of salsa and mix it in with" +
            " your mashed avocados.\n" +
            "Feel free to experiment! One classic Mexican guacamole has pomegranate seeds and chunks of" +
            " peaches in it (a Diana Kennedy favorite). Try guacamole with added pineapple, mango, or strawberries.\n" +
            "The simplest version of guacamole is just mashed avocados with salt. Don't let the lack of availability" +
            " of other ingredients stop you from making guacamole.\n" +
            "To extend a limited supply of avocados, add either sour cream or cottage cheese to your guacamole dip." +
            " Purists may be horrified, but so what? It tastes great.\n\n\n" +
            "Read more: http://www.simplyrecipes.com/recipes/perfect_guacamole/#ixzz4jvoun5ws");

        recipe.setNotes(notes);

        recipeRepository.save(recipe);
    }

    // Spicy Grilled Chicken Tacos
    private void initSpicyGrilledChickenTacos()
    {
        log.debug("In initSpicyGrilledChickenTacos()");

        Recipe recipe = new Recipe();

        recipe.setDescription("Spicy Grilled Chicken Tacos");

        recipe.addCategory(catMexican);
        recipe.addCategory(catAmerican);

        recipe.setDifficulty(Difficulty.EASY);

        recipe.setPrepTime(20);
        recipe.setCookTime(15);

        recipe.setServings(6);
        recipe.setSource("");
        recipe.setUrl("http://www.simplyrecipes.com/recipes/spicy_grilled_chicken_tacos/");

        // 2 tablespoons ancho chili powder
        // 1 teaspoon dried oregano
        // 1 teaspoon dried cumin
        // 1 teaspoon sugar
        // 1/2 teaspoon salt
        // 1 clove garlic, finely chopped
        // 1 tablespoon finely grated orange zest
        // 3 tablespoons fresh-squeezed orange juice
        // 2 tablespoons olive oil
        // 4 to 6 skinless, boneless chicken thighs (1 1/4 pounds)

        recipe.addIngredient(newIngredient("ancho chili powder","2",uomTablespoon));
        recipe.addIngredient(newIngredient("dried oregano","1",uomTeaspoon));
        recipe.addIngredient(newIngredient("dried cumin","1",uomTeaspoon));
        recipe.addIngredient(newIngredient("sugar","1",uomTeaspoon));
        recipe.addIngredient(newIngredient("salt","0.5",uomTeaspoon));
        recipe.addIngredient(newIngredient("clove garlic, finely chopped","1",uomEach));
        recipe.addIngredient(newIngredient("finely grated orange zest","1",uomTablespoon));
        recipe.addIngredient(newIngredient("fresh-squeezed orange juice","3",uomTablespoon));
        recipe.addIngredient(newIngredient("olive oil","2",uomTablespoon));
        recipe.addIngredient(newIngredient("skinless, boneless chicken thighs (1 1/4 pounds)","6",uomEach));

        // 8 small corn tortillas
        // 3 cups packed baby arugula (3 ounces)
        // 2 medium ripe avocados, sliced
        // 4 radishes, thinly sliced
        // 1/2 pint cherry tomatoes, halved
        // 1/4 red onion, thinly sliced
        // Roughly chopped cilantro
        // 1/2 cup sour cream thinned with 1/4 cup milk
        // 1 lime, cut into wedges

        recipe.addIngredient(newIngredient("small corn tortillas","8",uomEach));
        recipe.addIngredient(newIngredient("packed baby arugula (3 ounces)","3",uomCup));
        recipe.addIngredient(newIngredient("medium ripe avocados, sliced","2",uomEach));
        recipe.addIngredient(newIngredient("radishes, thinly sliced","4",uomEach));
        recipe.addIngredient(newIngredient("cherry tomatoes, halved","0.5",uomPint));
        recipe.addIngredient(newIngredient("red onion, thinly sliced","0.25",uomEach));
        recipe.addIngredient(newIngredient("Roughly chopped cilantro","0",uomEach));
        recipe.addIngredient(newIngredient("sour cream","0.56",uomCup));
        recipe.addIngredient(newIngredient("milk","0.25",uomCup));
        recipe.addIngredient(newIngredient("lime, cut into wedges","1",uomEach));

        recipe.setDirections("1 Prepare a gas or charcoal grill for medium-high, direct heat.\n" +
            "2 Make the marinade and coat the chicken: In a large bowl, stir together the chili powder, oregano," +
            " cumin, sugar, salt, garlic and orange zest. Stir in the orange juice and olive oil to make a loose paste." +
            " Add the chicken to the bowl and toss to coat all over.\n" +
            "Set aside to marinate while the grill heats and you prepare the rest of the toppings.\n" +
            "\n" +
            "\n" +
            "3 Grill the chicken: Grill the chicken for 3 to 4 minutes per side, or until a thermometer inserted into" +
            " the thickest part of the meat registers 165F. Transfer to a plate and rest for 5 minutes.\n" +
            "4 Warm the tortillas: Place each tortilla on the grill or on a hot, dry skillet over medium-high heat." +
            " As soon as you see pockets of the air start to puff up in the tortilla, turn it with tongs and heat for" +
            " a few seconds on the other side.\n" +
            "Wrap warmed tortillas in a tea towel to keep them warm until serving.\n" +
            "5 Assemble the tacos: Slice the chicken into strips. On each tortilla, place a small handful of arugula." +
            " Top with chicken slices, sliced avocado, radishes, tomatoes, and onion slices. Drizzle with the thinned" +
            " sour cream. Serve with lime wedges.\n\n\n" +
            "Read more: http://www.simplyrecipes.com/recipes/spicy_grilled_chicken_tacos/#ixzz4jvtrAnNm");

        Notes notes = new Notes();
        notes.setRecipeNotes("We have a family motto and it is this: Everything goes better in a tortilla.\n" +
            "Any and every kind of leftover can go inside a warm tortilla, usually with a healthy dose of" +
            " pickled jalapenos. I can always sniff out a late-night snacker when the aroma of tortillas heating" +
            " in a hot pan on the stove comes wafting through the house.\n" +
            "Todayâ€™s tacos are more purposeful â€“ a deliberate meal instead of a secretive midnight snack!\n" +
            "First, I marinate the chicken briefly in a spicy paste of ancho chile powder, oregano, cumin," +
            " and sweet orange juice while the grill is heating. You can also use this time to prepare the taco toppings.\n" +
            "Grill the chicken, then let it rest while you warm the tortillas. Now you are ready to assemble the" +
            " tacos and dig in. The whole meal comes together in about 30 minutes!\n\n\n" +
            "Read more: http://www.simplyrecipes.com/recipes/spicy_grilled_chicken_tacos/#ixzz4jvu7Q0MJ");

        recipe.setNotes(notes);

        recipeRepository.save(recipe);
    }
}
