package com.abnamro.assignment.recipes.api.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/** Recipe model class
 * Represent model class for API request
 * @author Proma Chowdhury
 * @version 1.0
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class Recipe {

    @NotBlank
    private String recipeName;
    private boolean isVegetarian;
    @NotNull
    private Integer noOfServings;
    @NotNull
    private Set<String> ingredients = new HashSet<>();
    private String instruction;
}
