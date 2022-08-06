package com.abnamro.assignment.recipes.api.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.HashSet;
import java.util.Set;
import lombok.Setter;

/** RecipeResponse model class
 * Represent model class for API response
 * @author Proma Chowdhury
 * @version 1.0
 */
@Getter
@Setter
//@AllArgsConstructor
@NoArgsConstructor
public class RecipeResponse {
    private Long id;
    private String recipeName;
    private Boolean isVegetarian;
    private Integer noOfServings;
    private Set<String> ingredients = new HashSet<>();
    private String instruction;
}
