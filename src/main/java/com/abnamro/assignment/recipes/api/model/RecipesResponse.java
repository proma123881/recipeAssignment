package com.abnamro.assignment.recipes.api.model;

import com.fasterxml.jackson.annotation.JsonValue;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * RecipesResponse model class.
 * Represent model class for API response
 *
 * @author Proma Chowdhury
 * @version 1.0
 */
@Getter
@NoArgsConstructor
public class RecipesResponse extends RecipeApiResponse {
    @JsonValue
    private List<RecipeResponse> recipes = new ArrayList<>();
}
