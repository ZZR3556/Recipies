package org.dlaws.recipes.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity
public class Category
{
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long id;

    private String description;

    @ManyToMany( mappedBy = "categories" )
    private Set<Recipe> recipies  = new HashSet<>();

    public Long getId()
    {
        return id;
    }

    public void setId( Long id )
    {
        this.id = id;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription( String description )
    {
        this.description = description;
    }

    public Set<Recipe> getRecipies()
    {
        return recipies;
    }

    public void setRecipies( Set<Recipe> recipies )
    {
        this.recipies = recipies;
    }

}