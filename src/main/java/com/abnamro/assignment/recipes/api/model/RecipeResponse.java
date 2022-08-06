package com.abnamro.assignment.recipes.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.HashSet;
import java.util.Set;

/** RecipeResponse model class
 * Represent model class for API response
 * @author Proma Chowdhury
 * @version 1.0
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RecipeResponse {
    private Long id;
    private String recipeName;
    private Boolean isVegetarian;
    private Integer noOfServings;
    private Set<String> ingredients = new HashSet<>();
    private String instruction;
}
