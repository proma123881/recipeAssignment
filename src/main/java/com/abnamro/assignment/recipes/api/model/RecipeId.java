package com.abnamro.assignment.recipes.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecipeId extends RecipeApiResponse {

    @JsonProperty("id")
    private  Long id;
}
