package com.abnamro.assignment.recipes.persistence;

import com.abnamro.assignment.recipes.persistence.model.RecipeEntity;
import com.abnamro.assignment.recipes.persistence.model.RecipeEntity_;
import org.springframework.data.jpa.domain.Specification;
import java.util.List;
import org.springframework.stereotype.Component;

import static com.abnamro.assignment.recipes.constant.ApiConstants.*;
import static org.springframework.data.jpa.domain.Specification.where;

//@Component
public class RecipeEntitySpecification {

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
        if (ingredient != null) {
            return (recipe, cq, cb) -> cb.isMember(ingredient, recipe.get(RecipeEntity_.ingredients));
        } else {
            return null;
        }
    }

    static Specification<RecipeEntity> notcontaintsIngredients(String ingredient) {
        if (ingredient != null) {
            return (recipe, cq, cb) -> cb.isNotMember(ingredient, recipe.get(RecipeEntity_.ingredients));
        } else {
            return null;
        }
    }

}
