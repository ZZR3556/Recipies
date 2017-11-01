package org.dlaws.recipes.domain;

import lombok.*;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Data
@EqualsAndHashCode( exclude = {"recipes"} )
@ToString( exclude = {"recipe"} )
@Entity
public class Category
{
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long id;

    private String description;

    @ManyToMany( mappedBy = "categories" )
    private Set<Recipe> recipes  = new HashSet<>();

    public void removeRecipe( Recipe recipe )
    {
        if ( recipes.remove( recipe ) )
        {
            recipe.removeCategory( this );
        }
    }
}
