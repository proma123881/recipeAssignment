package com.abnamro.assignment.recipes.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * RecipeId model class.
 * Represent model class for API Response
 * @author Proma Chowdhury
 * @version 1.0
 */

@Getter
@Setter
@NoArgsConstructor
public class RecipeId extends RecipeApiResponse {

    @JsonProperty("id")
    private  Long id;
}
