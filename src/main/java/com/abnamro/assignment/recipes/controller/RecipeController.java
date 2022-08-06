package com.abnamro.assignment.recipes.controller;

import com.abnamro.assignment.recipes.api.model.*;
import com.abnamro.assignment.recipes.persistence.model.RecipeEntity;
import com.abnamro.assignment.recipes.service.RecipeApiService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.List;


/** Controller class for all API endpoints.
 * @author Proma Chowdhury
 * @version 1.0
 */

@RestController
@Slf4j
@AllArgsConstructor
public class RecipeController {

    private final RecipeApiService recipeApiService;

    /**
     * Create a new Recipe
     * @param recipe
     * @return ResponseEntity
     */

    @PostMapping("/recipes")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> addRecipe(@Valid @RequestBody Recipe recipe) {

        RecipeId recipeId = recipeApiService.addRecipe(recipe);

        return apiResponse(recipeId, HttpStatus.CREATED);
    }

    /**
     * Update Recipe
     *
     * @param
     * @param
     * @return ResponseEntity
     */

    @PutMapping("/recipes/{id}")
    public ResponseEntity<?> updateRecipe(@RequestBody Recipe newRecipe, @PathVariable Long id) {

        UpdateRecipeResponse updateRecipeResponse = recipeApiService.updateRecipe(newRecipe, id);

        return apiResponse(updateRecipeResponse, HttpStatus.OK);

    }


    /**
     * Get ALL RECIPES
     *
     * @param
     * @param
     * @return ResponseEntity
     */

    @GetMapping("/recipes")
    ResponseEntity<?> getAllRecipes(@RequestParam(value = "isVegetarian", required = false) Boolean isVegetarian,
                                 @RequestParam(value = "instructionContains", required = false) String instructionContains,
                                 @RequestParam(value = "noOfServings", required = false) Integer noOfServings,
                                 @RequestParam(value = "presentIngredients", required = false) List<String> presentIngredients,
                                 @RequestParam(value = "absentIngredients", required = false) List<String> absentIngredients
    ) {
        RecipesResponse recipesResponse = recipeApiService.getAllRecipes(isVegetarian, instructionContains,
                noOfServings, presentIngredients, absentIngredients);

        return apiResponse(recipesResponse, HttpStatus.OK);
    }


    /**
     * Delete recipe by id
     *
     * @param
     * @param
     * @return ResponseEntity
     */
    @DeleteMapping("/recipes/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteEmployee(@PathVariable Long id) {

        recipeApiService.deleteEmployee(id);
    }


    private static ResponseEntity<?> apiResponse(RecipeApiResponse response, HttpStatus httpStatus) {

        HttpStatus status = httpStatus;
//        if (response != null && response.getErrors() != null && !response.getErrors().isEmpty()) {
//            response.getErrors().sort((e1, e2) -> {
//                if (e1.getHttpStatus().value() == e2.getHttpStatus().value()) {
//                    return 0;
//                }
//                return e1.getHttpStatus().value() > e2.getHttpStatus().value() ? -1 : 1;
//            });
//
//            status = response.getErrors().get(0).getHttpStatus();
//        }

        return ResponseEntity.status(status).cacheControl(CacheControl.noStore().cachePrivate()).body(response);
    }


}
