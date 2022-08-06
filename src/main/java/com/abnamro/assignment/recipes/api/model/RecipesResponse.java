package com.abnamro.assignment.recipes.api.model;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/** RecipesResponse model class
 * Represent model class for API response
 * @author Proma Chowdhury
 * @version 1.0
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RecipesResponse extends  RecipeApiResponse{
   @JsonValue
   private List<RecipeResponse> recipesResponse = new ArrayList<>();
}
