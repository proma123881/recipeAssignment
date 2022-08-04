package com.abnamro.assignment.recipes.controller;

import com.abnamro.assignment.recipes.api.model.Recipe;
import com.abnamro.assignment.recipes.api.model.RecipeApiResponse;
import com.abnamro.assignment.recipes.api.model.RecipeId;
import com.abnamro.assignment.recipes.mapper.DataMapper;
import com.abnamro.assignment.recipes.persistence.RecipeRepository;
import com.abnamro.assignment.recipes.persistence.model.RecipeEntity;
import com.abnamro.assignment.recipes.persistence.model.RecipeEntity_;
import com.abnamro.assignment.recipes.service.RecipeApiService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import javax.validation.Valid;
import java.util.List;

import static org.springframework.data.jpa.domain.Specification.where;

@RestController
@Slf4j
@AllArgsConstructor
public class RecipeController {

 private final RecipeApiService recipeApiService;

    /**
     * Create Recipe
     * @return ResponseEntity
     */

    @PostMapping("/recipes")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> addRecipe(@Valid @RequestBody Recipe recipe) {

       RecipeId recipeId =  recipeApiService.addRecipe(recipe);

        return apiResponse(recipeId, HttpStatus.CREATED);
    }
    /**
     * Update Recipe
     * @param
     * @param
     * @return ResponseEntity
     */

    @PutMapping("/recipes/{id}")
    public ResponseEntity<?> updateRecipe(@RequestBody Recipe newRecipe, @PathVariable Long id) {

       RecipeId recipeId = recipeApiService.updateRecipe(newRecipe, id);

        return apiResponse(recipeId, HttpStatus.OK);

        }




    /**
     * Get ALL RECIPES
     * @param
     * @param
     * @return ResponseEntity
     */

    @GetMapping("/recipes")
    ResponseEntity getAllRecipes(@RequestParam( value = "isVegetarian", required = false) Boolean isVegetarian,
                                 @RequestParam( value = "instructionContains", required = false) String instructionContains,
                                 @RequestParam( value = "noOfServings", required = false) Integer noOfServings,
                                 @RequestParam( value = "presentIngredients", required = false) List<String> presentIngredients,
                                 @RequestParam( value = "absentIngredients", required = false) List<String> absentIngredients
                                 ) {
        List<RecipeEntity> recipes = recipeApiService.getAllRecipes(isVegetarian, instructionContains,
                noOfServings, presentIngredients, absentIngredients);

        return  ResponseEntity.ok(recipes);
    }


    /**
     * Delete recipe by id
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
        if (response != null && response.getErrors() != null && !response.getErrors().isEmpty()) {
            response.getErrors().sort((e1, e2) -> {
                if (e1.getHttpStatus().value() == e2.getHttpStatus().value()) {
                    return 0;
                }
                return e1.getHttpStatus().value() > e2.getHttpStatus().value() ? -1 : 1;
            });

            status = response.getErrors().get(0).getHttpStatus();
        }

        return  ResponseEntity.status(status).cacheControl(CacheControl.noStore().cachePrivate()).body(response);
    }



}
