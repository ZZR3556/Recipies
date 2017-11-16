package org.dlaws.recipes.commands;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.dlaws.recipes.domain.Difficulty;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class RecipeCommand
{
    private Long id;

    @NotBlank
    @Size( min=3, max=255 )
    private String description;

    @Min(1)
    @Max(999)
    private Integer prepTime;

    @Min(1)
    @Max(999)
    private Integer cookTime;

    @Min(1)
    @Max(100)
    private Integer servings;

    @Size( max=255 )
    private String source;

    @URL // May be blank
    private String url;

    private Difficulty difficulty;

    @NotBlank
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
