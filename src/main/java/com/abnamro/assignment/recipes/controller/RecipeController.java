package com.abnamro.assignment.recipes.controller;

import com.abnamro.assignment.recipes.api.model.Recipe;
import com.abnamro.assignment.recipes.api.model.RecipeApiResponse;
import com.abnamro.assignment.recipes.api.model.RecipeId;
import com.abnamro.assignment.recipes.persistence.RecipeRepository;
import com.abnamro.assignment.recipes.persistence.model.RecipeEntity;
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
                                 @RequestParam( value = "instructionContains", required = false) String instructionContains) {
        List<RecipeEntity> recipes = recipeRepository.findAll(where(isVegetarian(isVegetarian))
                 .and(instructionContains(instructionContains)));

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
    EntityManager em;
    private List<RecipeEntity> findRecipesByCriteria( Boolean isVegetarian,
                                                     String instructionContains) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<RecipeEntity> cq = cb.createQuery(RecipeEntity.class);

        Root<RecipeEntity> recipe = cq.from(RecipeEntity.class);
        if (isVegetarian != null) {
            Predicate vegetarianPredicate = cb.equal(recipe.get("isVegetarian"), isVegetarian);
            cq.where(vegetarianPredicate);
        }

        if (instructionContains != null) {
            Predicate instructionPredicate = cb.like(recipe.get("instruction"), "%" + instructionContains + "%");
            cq.where(instructionPredicate);
        }

        TypedQuery<RecipeEntity> query = em.createQuery(cq);
        return query.getResultList();
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



    static Specification<RecipeEntity> noOfServings(Integer noOfServings) {
        if (noOfServings != null) {
            return (recipe, cq, cb) -> cb.equal(recipe.get("noOfServings"),  noOfServings);
        } else {
            return null;
        }

    }

    static Specification<RecipeEntity> containtsIngredients(List<String> containsIngredients) {
        if (containsIngredients != null)

            return (recipe, cq, cb) -> {
                Expression<List<RecipeEntity>> bList = recipe.get(.);
                cb.isMember(recipe.get("ingredients"),
                        containsIngredients.get(0));
            }
        } else {
            return null;
        }



    }
}
