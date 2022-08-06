package com.abnamro.assignment.recipes.api.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
//@AllArgsConstructor
@NoArgsConstructor
public class UpdateRecipeResponse extends RecipeApiResponse {
    private Long id;
    private String recipeName;
    private Boolean isVegetarian;
    private Integer noOfServings;
    private Set<String> ingredients = new HashSet<>();
    private String instruction;
}