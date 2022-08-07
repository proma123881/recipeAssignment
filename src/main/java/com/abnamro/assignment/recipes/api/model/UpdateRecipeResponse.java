package com.abnamro.assignment.recipes.api.model;

import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * UpdateRecipeResponse model class.
 * Represent model class for API response
 *
 * @author Proma Chowdhury
 * @version 1.0
 */
@Getter
@Setter
@NoArgsConstructor
public class UpdateRecipeResponse extends RecipeApiResponse {
    private Long id;
    private String recipeName;
    private Boolean isVegetarian;
    private Integer noOfServings;
    private Set<String> ingredients = new HashSet<>();
    private String instruction;
}