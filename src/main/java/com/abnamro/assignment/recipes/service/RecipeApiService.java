package com.abnamro.assignment.recipes.service;

import com.abnamro.assignment.recipes.api.model.*;
import com.abnamro.assignment.recipes.api.model.Error;
import com.abnamro.assignment.recipes.constant.ApiConstants;
import com.abnamro.assignment.recipes.exception.RecipeApiDatabaseException;
import com.abnamro.assignment.recipes.mapper.DataMapper;
import com.abnamro.assignment.recipes.persistence.RecipeEntitySpecification;
import com.abnamro.assignment.recipes.persistence.RecipeRepository;
import com.abnamro.assignment.recipes.persistence.model.RecipeEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import java.util.List;
import org.springframework.stereotype.Service;

import static com.abnamro.assignment.recipes.constant.ApiConstants.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class RecipeApiService {

    private final DataMapper dataMapper;
    private final RecipeRepository recipeRepository;


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

    public RecipeId updateRecipe(Recipe newRecipe, Long id) {



        RecipeEntity recipeEntity = recipeRepository.findById(id)
                .map(recipe -> {
                    if (newRecipe.getRecipeName() == null) {
                        newRecipe.setRecipeName(recipe.getRecipeName());
                    }
                    if (newRecipe.getIngredients() == null || newRecipe.getIngredients().isEmpty()) {
                        newRecipe.setIngredients(recipe.getIngredients());
                    }
                    if (newRecipe.getInstruction() == null) {
                        newRecipe.setInstruction(recipe.getInstruction());
                    }
                    if (newRecipe.getIsVegetarian() == null) {
                        newRecipe.setIsVegetarian(recipe.getIsVegetarian());
                    }
                    if (newRecipe.getNoOfServings() == null) {
                        newRecipe.setNoOfServings(recipe.getNoOfServings());
                    }
                    RecipeEntity recipeEntity1 = dataMapper.toRecipeEntity(newRecipe);
                    recipeEntity1.setId(id);
                    try {
                        return recipeRepository.saveAndFlush(recipeEntity1);
                    } catch (Exception e) {
                        throw new RecipeApiDatabaseException(DATABASE_ERROR_MESSAGE_UPDATE, e);
                    }

                } )
                .orElseThrow(() -> {
                    Error error = new Error(DATABASE_ERROR_CODE_RECIPE_NOT_FOUND, DATABASE_ERROR_NAME_RECIPE_NOT_FOUND,
                            DATABASE_ERROR_MESSAGE_RECIPE_NOT_FOUND, HttpStatus.NOT_FOUND);
                    return new RecipeApiDatabaseException(error, error.getDescription());
                });


       return dataMapper.toRecipeId(recipeEntity);
    }

    public void deleteEmployee(Long id) {

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



    public RecipesResponse getAllRecipes(Boolean isVegetarian, String instructionContains, Integer noOfServings,
                                         List<String> presentIngredients, List<String> absentIngredients) {

        Specification<RecipeEntity> spec = RecipeEntitySpecification.getRecipes(isVegetarian,  instructionContains,  noOfServings,  presentIngredients,
                 absentIngredients);

        List<RecipeEntity> recipeEntities = recipeRepository.findAll(spec);

        if (recipeEntities == null || recipeEntities.isEmpty()) {
            Error error = new Error(DATABASE_ERROR_CODE_RECIPE_NOT_FOUND_GET_RECIPES,
                    DATABASE_ERROR_NAME_RECIPE_NOT_FOUND, DATABASE_ERROR_MESSAGE_RECIPE_NOT_FOUND_CET_RECIPES,
                    HttpStatus.NOT_FOUND);
            throw new RecipeApiDatabaseException(error, error.getDescription());

        }

        return getRecipesResponse(recipeEntities);
    }

    private RecipesResponse getRecipesResponse(List<RecipeEntity> recipeEntities){

        RecipesResponse recipesResponse = new RecipesResponse();

//        recipeEntities.stream().map(recipeEntity -> {
//            RecipeResponse recipeResponse = dataMapper.toRecipeResponse(recipeEntity);
//            recipesResponse.getRecipesResponse().add(recipeResponse);
//            return  recipesResponse;
//        });
        recipeEntities.forEach(recipeEntity -> {
            RecipeResponse recipeResponse = dataMapper.toRecipeResponse(recipeEntity);
            recipesResponse.getRecipesResponse().add(recipeResponse);
        });

       return recipesResponse;

    }
}
