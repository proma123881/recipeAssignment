package com.abnamro.assignment.recipes.constant;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

/**
 * ApiConstants constant class.
 * Represent constant class for RecipeApiApplication
 * @author Proma Chowdhury
 * @version 1.0
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiConstants {

    public static final String DATABASE_ERROR_MESSAGE_SAVE = "Error saving recipe to db";
    public static final String DATABASE_ERROR_MESSAGE_UPDATE = "Error updating recipe in database";
    public static final String DATABASE_ERROR_MESSAGE_DELETE = "Error deleting recipe from database";
    public static final String DATABASE_ERROR_NAME_RECIPE_NOT_FOUND = "NOT FOUND";
    public static final String DATABASE_ERROR_CODE_RECIPE_NOT_FOUND = "RA-101";
    public static final String DATABASE_ERROR_MESSAGE_RECIPE_NOT_FOUND = "RecipeId not found in db";

    public static final String DATABASE_ERROR_CODE_RECIPE_NOT_FOUND_GET_RECIPES = "RA-102";
    public static final String DATABASE_ERROR_MESSAGE_RECIPE_NOT_FOUND_CET_RECIPES = "No recipes found for the given search criteria";

    public static final String IS_VEGETARIAN = "isVegetarian";
    public static final String INSTRUCTION = "instruction";
    public static final String NO_OF_SERVINGS = "noOfServings";


}
