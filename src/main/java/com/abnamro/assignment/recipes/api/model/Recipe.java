package com.abnamro.assignment.recipes.api.model;

import java.util.HashSet;
import java.util.Set;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.springframework.validation.annotation.Validated;

/**
 * Recipe model class.
 * Represent model class for API request
 * @author Proma Chowdhury
 * @version 1.0
 */
@Getter
@Setter
@NoArgsConstructor
@Validated
public class Recipe {

    @NotBlank
    private String recipeName;
    @NotNull
    private Boolean isVegetarian;
    @NotNull
    @Positive
    private Integer noOfServings;
    @NotNull
    @NotEmpty
    private Set<String> ingredients = new HashSet<>();
    private String instruction;
}
