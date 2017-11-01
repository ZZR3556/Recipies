package org.dlaws.recipes.commands;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.dlaws.recipes.domain.Difficulty;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class RecipeCommand
{
    private Long id;
    private String description;
    private Integer prepTime;
    private Integer cookTime;
    private Integer servings;
    private String source;
    private String url;
    private Difficulty difficulty;
    private String directions;

    private NotesCommand notes;
    private Set<CategoryCommand> categories = new HashSet<>();
    private Set<IngredientCommand> ingredients = new HashSet<>();

    private Byte[] image;

    public void addIngredient( IngredientCommand ingredient )
    {
        this.ingredients.add(ingredient);
    }

    public void addCategory( CategoryCommand category )
    {
        this.categories.add(category);
    }

}
