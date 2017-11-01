package org.dlaws.recipes.commands;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class IngredientCommand
{
    private Long id;
    private Long recipeId;
    private String description;
    private BigDecimal amount;
    private UnitOfMeasureCommand uom;

    public String getFormattedString()
    {
        StringBuilder sb = new StringBuilder();

        sb.append(amount);
        sb.append(" ");

        if (!"".equals(uom.getDescription()))
        {
            sb.append(uom.getDescription());
            sb.append(" of ");
        }

        sb.append(description);

        return sb.toString();
    }
}
