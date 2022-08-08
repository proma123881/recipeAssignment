package com.abnamro.assignment.recipes.service;

import static com.abnamro.assignment.recipes.constant.ApiConstants.DATABASE_ERROR_CODE_RECIPE_NOT_FOUND;
import static com.abnamro.assignment.recipes.constant.ApiConstants.DATABASE_ERROR_CODE_RECIPE_NOT_FOUND_GET_RECIPES;
import static com.abnamro.assignment.recipes.constant.ApiConstants.DATABASE_ERROR_MESSAGE_DELETE;
import static com.abnamro.assignment.recipes.constant.ApiConstants.DATABASE_ERROR_MESSAGE_RECIPE_NOT_FOUND;
import static com.abnamro.assignment.recipes.constant.ApiConstants.DATABASE_ERROR_MESSAGE_RECIPE_NOT_FOUND_CET_RECIPES;
import static com.abnamro.assignment.recipes.constant.ApiConstants.DATABASE_ERROR_MESSAGE_UPDATE;
import static com.abnamro.assignment.recipes.constant.ApiConstants.DATABASE_ERROR_NAME_RECIPE_NOT_FOUND;

import com.abnamro.assignment.recipes.api.model.Error;
import com.abnamro.assignment.recipes.api.model.Recipe;
import com.abnamro.assignment.recipes.api.model.RecipeId;
import com.abnamro.assignment.recipes.api.model.RecipeResponse;
import com.abnamro.assignment.recipes.api.model.RecipesResponse;
import com.abnamro.assignment.recipes.api.model.UpdateRecipeResponse;
import com.abnamro.assignment.recipes.constant.ApiConstants;
import com.abnamro.assignment.recipes.exception.RecipeApiDatabaseException;
import com.abnamro.assignment.recipes.mapper.DataMapper;
import com.abnamro.assignment.recipes.persistence.RecipeEntitySpecification;
import com.abnamro.assignment.recipes.persistence.RecipeRepository;
import com.abnamro.assignment.recipes.persistence.model.RecipeEntity;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

/** RecipeApiService service class  for the API.
 * @author Proma Chowdhury
 * @version 1.0
 */

@Service
@Slf4j
@RequiredArgsConstructor
public class RecipeApiService {

    private final DataMapper dataMapper;
    private final RecipeRepository recipeRepository;

    /**
     * Create a new Recipe.
     *
     * @param recipe recipe input to be created
     * @return RecipeId
     */

    public RecipeId addRecipe(Recipe recipe) {

        RecipeEntity recipeEntityToSave = dataMapper.toRecipeEntity(recipe);
        RecipeEntity recipeEntitySaved;
        try {
            recipeEntitySaved = recipeRepository.saveAndFlush(recipeEntityToSave);
        } catch (Exception e) {
            throw new RecipeApiDatabaseException(ApiConstants.DATABASE_ERROR_MESSAGE_SAVE, e);
        }

        return dataMapper.toRecipeId(recipeEntitySaved);
    }

    /**
     * Update Recipe.
     *
     * @param newRecipe new recipe for update
     * @param id id of existing recipe to be updated
     * @return UpdateRecipeResponse
     */
    public UpdateRecipeResponse updateRecipe(Recipe newRecipe, Long id) {

        RecipeEntity recipeEntity = recipeRepository.findById(id)
                .map(recipe -> {
                    newRecipe.setRecipeName(
                            Optional.ofNullable(newRecipe.getRecipeName()).orElse(recipe.getRecipeName()));
                    newRecipe.setIngredients(
                            Optional.ofNullable(newRecipe.getIngredients()).orElse(recipe.getIngredients()));
                    newRecipe.setInstruction(
                            Optional.ofNullable(newRecipe.getInstruction()).orElse(recipe.getInstruction()));
                    newRecipe.setIsVegetarian(
                            Optional.ofNullable(newRecipe.getIsVegetarian()).orElse(recipe.getIsVegetarian()));
                    newRecipe.setNoOfServings(
                            Optional.ofNullable(newRecipe.getNoOfServings()).orElse(recipe.getNoOfServings()));
                    RecipeEntity recipeEntity1 = dataMapper.toRecipeEntity(newRecipe);
                    recipeEntity1.setId(id);
                    try {
                        return recipeRepository.saveAndFlush(recipeEntity1);
                    } catch (Exception e) {
                        throw new RecipeApiDatabaseException(DATABASE_ERROR_MESSAGE_UPDATE, e);
                    }

                })
                .orElseThrow(() -> {
                    Error error = new Error(DATABASE_ERROR_CODE_RECIPE_NOT_FOUND, DATABASE_ERROR_NAME_RECIPE_NOT_FOUND,
                            DATABASE_ERROR_MESSAGE_RECIPE_NOT_FOUND, HttpStatus.NOT_FOUND);
                    return new RecipeApiDatabaseException(error, error.getDescription());
                });


        return dataMapper.toUpdateRecipeResponse(recipeEntity);
    }

    /**
     * Delete recipe by id.
     *
     * @param id recipe id to be deleted
     */
    public void deleteRecipe(Long id) {

        recipeRepository.findById(id).orElseThrow(() -> {
            Error error = new Error(DATABASE_ERROR_CODE_RECIPE_NOT_FOUND, DATABASE_ERROR_NAME_RECIPE_NOT_FOUND,
                            DATABASE_ERROR_MESSAGE_RECIPE_NOT_FOUND, HttpStatus.NOT_FOUND);
            return new RecipeApiDatabaseException(error, error.getDescription());
        }
        );

        try {
            recipeRepository.deleteById(id);
        } catch (Exception e) {
            throw new RecipeApiDatabaseException(DATABASE_ERROR_MESSAGE_DELETE, e);
        }

    }

    /**
     * Get all recipes.
     *
     * @param isVegetarian param to filter by is vegetarian
     * @param instructionContains param to filter by instructionContains
     * @param absentIngredients  param to filter by absentIngredients
     * @param presentIngredients param to filter by present ingredients
     * @param noOfServings param to filter by noOfServings
     * @return RecipesResponse
     */

    public RecipesResponse getAllRecipes(Boolean isVegetarian, String instructionContains, Integer noOfServings,
                                         List<String> presentIngredients, List<String> absentIngredients) {

        Specification<RecipeEntity> spec = RecipeEntitySpecification.getRecipes(isVegetarian, instructionContains,
                noOfServings, presentIngredients,
                absentIngredients);

        List<RecipeEntity> recipeEntities = recipeRepository.findAll(spec);

        if (recipeEntities.isEmpty()) {
            Error error = new Error(DATABASE_ERROR_CODE_RECIPE_NOT_FOUND_GET_RECIPES,
                    DATABASE_ERROR_NAME_RECIPE_NOT_FOUND, DATABASE_ERROR_MESSAGE_RECIPE_NOT_FOUND_CET_RECIPES,
                    HttpStatus.NOT_FOUND);
            throw new RecipeApiDatabaseException(error, error.getDescription());

        }

        return getRecipesResponse(recipeEntities);
    }

    private RecipesResponse getRecipesResponse(List<RecipeEntity> recipeEntities) {

        RecipesResponse recipesResponse = new RecipesResponse();

        recipeEntities.forEach(recipeEntity -> {
            RecipeResponse recipeResponse = dataMapper.toRecipeResponse(recipeEntity);
            recipesResponse.getRecipes().add(recipeResponse);
        });

        return recipesResponse;

    }
}
