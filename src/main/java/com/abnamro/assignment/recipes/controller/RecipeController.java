package com.abnamro.assignment.recipes.controller;

import com.abnamro.assignment.recipes.api.model.Recipe;
import com.abnamro.assignment.recipes.api.model.RecipeApiResponse;
import com.abnamro.assignment.recipes.api.model.RecipeId;
import com.abnamro.assignment.recipes.persistence.RecipeRepository;
import com.abnamro.assignment.recipes.persistence.model.RecipeEntity;
import com.abnamro.assignment.recipes.persistence.model.RecipeEntity_;
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

 private final RecipeRepository recipeRepository;

    /**
     * Create Recipe
     * @return ResponseEntity
     */

    @PostMapping("/recipes")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> addRecipe(@Valid @RequestBody Recipe recipe) {
       RecipeEntity recipeEntity = recipeRepository.save(RecipeEntity.builder().isVegetarian(recipe.isVegetarian())
                .recipeName(recipe.getRecipeName())
                .noOfServings(recipe.getNoOfServings())
               .ingredients(recipe.getIngredients())
               .instruction(recipe.getInstruction())
               .build());
       RecipeId recipeId = new RecipeId();
        recipeId.setId(recipeEntity.getId());
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


            RecipeEntity recipeEntity = recipeRepository.findById(id)
                    .map(recipe -> {
                        recipe.setRecipeName(newRecipe.getRecipeName());
                        recipe.setInstruction(newRecipe.getInstruction());
                        recipe.setNoOfServings(newRecipe.getNoOfServings());
                        recipe.setVegetarian(newRecipe.isVegetarian());
                        recipe.setIngredients(newRecipe.getIngredients());
                        return recipeRepository.save(recipe);
                    })
                    .orElseGet(() -> {
                        return  recipeRepository.save(RecipeEntity.builder().isVegetarian(newRecipe.isVegetarian())
                                .recipeName(newRecipe.getRecipeName())
                                .noOfServings(newRecipe.getNoOfServings())
                                .ingredients(newRecipe.getIngredients())
                                .instruction(newRecipe.getInstruction())
                                .build());
                    });
        RecipeId recipeId = new RecipeId();
        recipeId.setId(recipeEntity.getId());
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
                                 @RequestParam( value = "ingredients", required = false) List<String> ingredients
                                 ) {
        List<RecipeEntity> recipes = recipeRepository.findAll(where(isVegetarian(isVegetarian))
                 .and(instructionContains(instructionContains))
                .and(equalsNoOfServings(noOfServings))
                .and(containtsIngredients(ingredients))
        );

        return  ResponseEntity.ok(recipes);
    }

    /**
     * Get recipe by id
     * @param
     * @param
     * @return ResponseEntity
     */

    @GetMapping("/recipes/{id}")
    ResponseEntity getRecipeById(@PathVariable Long id) {

        return null;
    }

    /**
     * Delete recipe by id
     * @param
     * @param
     * @return ResponseEntity
     */
    @DeleteMapping("/recipes/{id}")
    @ResponseStatus(HttpStatus.OK)
    void deleteEmployee(@PathVariable Long id) {

        recipeRepository.deleteById(id);
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

    static Specification<RecipeEntity> isVegetarian(Boolean isVegetarian) {
        if (isVegetarian != null) {
            return (recipe, cq, cb) -> cb.equal(recipe.get("isVegetarian"), isVegetarian);
        } else {
            return null;
        }


    }

    static Specification<RecipeEntity> instructionContains(String instructionContains) {
        if (instructionContains != null) {
            return (recipe, cq, cb) -> cb.like(recipe.get("instruction"), "%" + instructionContains + "%");
        } else {
            return null;
        }

    }



    static Specification<RecipeEntity> equalsNoOfServings(Integer noOfServings) {
        if (noOfServings != null) {
            return (recipe, cq, cb) -> cb.equal(recipe.get("noOfServings"),  noOfServings);
        } else {
            return null;
        }

    }

    static Specification<RecipeEntity> containtsIngredients(List<String> ingredients) {
        if (ingredients != null)
        {
            return (recipe, cq, cb) -> cb.isMember(ingredients.get(0),
                    recipe.get(RecipeEntity_.ingredients));
        }
        else {
            return null;
        }
    }

}
