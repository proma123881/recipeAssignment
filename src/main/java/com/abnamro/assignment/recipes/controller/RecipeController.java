package com.abnamro.assignment.recipes.controller;

import com.abnamro.assignment.recipes.api.model.Recipe;
import com.abnamro.assignment.recipes.api.model.RecipeApiResponse;
import com.abnamro.assignment.recipes.api.model.RecipeId;
import com.abnamro.assignment.recipes.api.model.RecipesResponse;
import com.abnamro.assignment.recipes.api.model.UpdateRecipeResponse;
import com.abnamro.assignment.recipes.service.RecipeApiService;
import java.util.List;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller class for all API endpoints.
 *
 * @author Proma Chowdhury
 * @version 1.0
 */

@RestController
@Slf4j
@AllArgsConstructor
public class RecipeController {

    private final RecipeApiService recipeApiService;

    /**
     * Create a new Recipe.
     *
     * @param recipe recipe input to be created
     * @return ResponseEntity
     */

    @PostMapping("/recipes")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> addRecipe(@Valid @RequestBody Recipe recipe) {

        RecipeId recipeId = recipeApiService.addRecipe(recipe);

        return apiResponse(recipeId, HttpStatus.CREATED);
    }

    /**
     * Update Recipe.
     *
     * @param newRecipe new recipe for update
     * @param id id of existing recipe to be updated
     * @return ResponseEntity
     */

    @PutMapping("/recipes/{id}")
    public ResponseEntity<?> updateRecipe(@RequestBody Recipe newRecipe, @PathVariable Long id) {

        UpdateRecipeResponse updateRecipeResponse = recipeApiService.updateRecipe(newRecipe, id);

        return apiResponse(updateRecipeResponse, HttpStatus.OK);

    }


    /**
     * Get all recipes.
     *
     * @param isVegetarian param to filter by is vegetarian
     * @param instructionContains param to filter by instructionContains
     * @param absentIngredients  param to filter by absentIngredients
     * @param presentIngredients param to filter by present ingredients
     * @param noOfServings param to filter by noOfServings
     * @return ResponseEntity
     */

    @GetMapping("/recipes")
    public ResponseEntity<?> getRecipes(@RequestParam(value = "isVegetarian", required = false) Boolean isVegetarian,
                                    @RequestParam(value = "instructionContains", required = false)
                                            String instructionContains,
                                    @RequestParam(value = "noOfServings", required = false) Integer noOfServings,
                                    @RequestParam(value = "presentIngredients", required = false)
                                            List<String> presentIngredients,
                                    @RequestParam(value = "absentIngredients", required = false)
                                            List<String> absentIngredients
    ) {
        RecipesResponse recipesResponse = recipeApiService.getAllRecipes(isVegetarian, instructionContains,
                noOfServings, presentIngredients, absentIngredients);

        return apiResponse(recipesResponse, HttpStatus.OK);
    }


    /**
     * Delete recipe by id.
     *
     * @param id recipe id to be deleted
     */
    @DeleteMapping("/recipes/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRecipe(@PathVariable Long id) {

        recipeApiService.deleteEmployee(id);
    }


    private static ResponseEntity<?> apiResponse(RecipeApiResponse response, HttpStatus httpStatus) {

        return ResponseEntity.status(httpStatus).cacheControl(CacheControl.noStore().cachePrivate()).body(response);
    }


}
