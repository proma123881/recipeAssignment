package com.abnamro.assignment.recipes.util;

import com.abnamro.assignment.recipes.api.model.Recipe;
import com.abnamro.assignment.recipes.api.model.RecipeId;
import com.abnamro.assignment.recipes.persistence.model.RecipeEntity;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class TestUtils {

    public static Recipe createRecipe() {
        Recipe recipe = new Recipe();
        recipe.setRecipeName("muffin");
        recipe.setIsVegetarian(false);
        recipe.setNoOfServings(4);
        recipe.setInstruction("put chocolate");
        Set<String> ingredients = new HashSet<>(Arrays.asList("chocolate", "sugar"));
        recipe.setIngredients(ingredients);
        return recipe;
    }


    public static RecipeEntity createRecipeEntity() {
        RecipeEntity recipe = new RecipeEntity();
        recipe.setId(1L);
        recipe.setRecipeName("muffin");
        recipe.setIsVegetarian(false);
        recipe.setNoOfServings(4);
        recipe.setInstruction("put chocolate");
        Set<String> ingredients = new HashSet<>(Arrays.asList("chocolate", "sugar"));
        recipe.setIngredients(ingredients);
        return recipe;
    }

    public static RecipeId createRecipeId(){
        RecipeId recipeId = new RecipeId();
        recipeId.setId(1L);
        return recipeId;
    }
}
