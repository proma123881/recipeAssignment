package com.abnamro.assignment.recipes.persistence;

import static com.abnamro.assignment.recipes.constant.ApiConstants.INSTRUCTION;
import static com.abnamro.assignment.recipes.constant.ApiConstants.IS_VEGETARIAN;
import static com.abnamro.assignment.recipes.constant.ApiConstants.NO_OF_SERVINGS;
import static org.springframework.data.jpa.domain.Specification.where;

import com.abnamro.assignment.recipes.persistence.model.RecipeEntity;
import com.abnamro.assignment.recipes.persistence.model.RecipeEntity_;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;

/** RecipeEntitySpecification  for the API.
 * @author Proma Chowdhury
 * @version 1.0
 */
public class RecipeEntitySpecification {

    /**
     * get Recipe Specification.
     *
     * @param isVegetarian isVegetarian
     * @param instructionContains instructionContains
     * @param noOfServings noOfServings
     * @param presentIngredients presentIngredients
     * @param absentIngredients absentIngredients
     * @return UpdateRecipeResponse
     */

    public static Specification<RecipeEntity> getRecipes(Boolean isVegetarian,
                                                         String instructionContains,
                                                         Integer noOfServings,
                                                         List<String> presentIngredients,
                                                         List<String> absentIngredients) {

        Specification<RecipeEntity> spec = where(isVegetarian(isVegetarian))
                .and(instructionContains(instructionContains))
                .and(equalsNoOfServings(noOfServings));

        if (presentIngredients != null) {
            for (String ingredient : presentIngredients) {
                spec = spec.and(where(containtsIngredients(ingredient)));
            }
        }

        if (absentIngredients != null) {
            for (String ingredient : absentIngredients) {
                spec = spec.and(where(notcontaintsIngredients(ingredient)));
            }
        }
        return spec;
    }

    static Specification<RecipeEntity> isVegetarian(Boolean isVegetarian) {
        if (isVegetarian != null) {
            return (recipe, cq, cb) -> cb.equal(recipe.get(IS_VEGETARIAN), isVegetarian);
        } else {
            return null;
        }


    }

    static Specification<RecipeEntity> instructionContains(String instructionContains) {
        if (instructionContains != null) {
            return (recipe, cq, cb) -> cb.like(cb.lower(recipe.get(INSTRUCTION)),
                    "%" + instructionContains.toLowerCase() + "%");
        } else {
            return null;
        }

    }


    static Specification<RecipeEntity> equalsNoOfServings(Integer noOfServings) {
        if (noOfServings != null) {
            return (recipe, cq, cb) -> cb.equal(recipe.get(NO_OF_SERVINGS), noOfServings);
        } else {
            return null;
        }

    }

    static Specification<RecipeEntity> containtsIngredients(String ingredient) {
        return (recipe, cq, cb) -> cb.isMember(ingredient, recipe.get(RecipeEntity_.ingredients));

    }

    static Specification<RecipeEntity> notcontaintsIngredients(String ingredient) {
        return (recipe, cq, cb) -> cb.isNotMember(ingredient, recipe.get(RecipeEntity_.ingredients));

    }

}
