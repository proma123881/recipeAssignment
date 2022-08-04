package com.abnamro.assignment.recipes.service;


import com.abnamro.assignment.recipes.BaseIntegrationTest;
import com.abnamro.assignment.recipes.api.model.Recipe;
import com.abnamro.assignment.recipes.api.model.RecipeId;
import com.abnamro.assignment.recipes.constant.ApiConstants;
import com.abnamro.assignment.recipes.exception.RecipeApiDatabaseException;
import com.abnamro.assignment.recipes.mapper.DataMapper;
import com.abnamro.assignment.recipes.persistence.RecipeRepository;
import com.abnamro.assignment.recipes.persistence.model.RecipeEntity;
import com.abnamro.assignment.recipes.util.TestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import javax.validation.constraints.AssertTrue;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

public class RecipeApiServiceTest extends BaseIntegrationTest {

    @Mock
    DataMapper dataMapper;
    @Mock
    RecipeRepository recipeRepository;
    @InjectMocks
    RecipeApiService recipeApiService;

    @Test
    void addRecipe_happyFlow_returnSuccess() {

       RecipeEntity recipeEntity = TestUtils.createRecipeEntity();

        when(dataMapper.toRecipeEntity(any())).thenReturn(recipeEntity);

        when(recipeRepository.saveAndFlush(recipeEntity)).thenReturn(recipeEntity);

        when(dataMapper.toRecipeId(recipeEntity)).thenReturn(TestUtils.createRecipeId());

        RecipeId recipeId = recipeApiService.addRecipe(TestUtils.createRecipe());

        assertEquals(recipeId.getId(), TestUtils.createRecipeEntity().getId());


    }


    @Test
    void addRecipe_unHappyFlow_throwException() {

        RecipeEntity recipeEntity = TestUtils.createRecipeEntity();

        when(dataMapper.toRecipeEntity(any())).thenReturn(recipeEntity);

        when(recipeRepository.saveAndFlush(recipeEntity))
                .thenThrow(new RecipeApiDatabaseException(ApiConstants.DATABASE_ERROR_MESSAGE_SAVE, new Exception()));

        when(dataMapper.toRecipeId(recipeEntity)).thenReturn(TestUtils.createRecipeId());

        Exception exception = Assertions.assertThrows(RecipeApiDatabaseException.class,
                () ->   recipeApiService.addRecipe(TestUtils.createRecipe()));

        String expectedMessage = ApiConstants.DATABASE_ERROR_MESSAGE_SAVE;
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));


    }

    @Test
    void deleteRecipe_happyFlow_returnSuccess() {

        RecipeEntity recipeEntity = TestUtils.createRecipeEntity();

        when(recipeRepository.findById(1L)).thenReturn(Optional.of(recipeEntity));

        recipeApiService.deleteEmployee(1L);

        assertDoesNotThrow(() -> recipeApiService.deleteEmployee(1L));


    }

    @Test
    void deleteRecipe_idNotFound_throwException() {

        when(recipeRepository.findById(1L)).thenReturn(Optional.empty());

        //recipeApiService.deleteEmployee(1L);

        assertThrows(
                RecipeApiDatabaseException.class,
                () -> recipeApiService.deleteEmployee(1L));
    }

    @Test
    void deleteRecipe_errorWhileDbDelete_throwException() {

        RecipeEntity recipeEntity = TestUtils.createRecipeEntity();

        when(recipeRepository.findById(1L)).thenReturn(Optional.of(recipeEntity));

        doThrow(new RecipeApiDatabaseException("Error deleting recipe from database", new Exception()))
                .when(recipeRepository).deleteById(1L);

       // recipeApiService.deleteEmployee(1L);

        assertThrows(
                RecipeApiDatabaseException.class,
                () -> recipeApiService.deleteEmployee(1L));
    }


    @Test
    void updateRecipe_idNotFound_throwException() {

        when(recipeRepository.findById(1L)).thenReturn(Optional.empty());

        //recipeApiService.deleteEmployee(1L);

        assertThrows(
                RecipeApiDatabaseException.class,
                () -> recipeApiService.updateRecipe(TestUtils.createRecipe(),1L));
    }

    @Test
    void updateRecipe_errorWhileDbUpdate_throwException() {

        RecipeEntity recipeEntity = TestUtils.createRecipeEntity();

        when(recipeRepository.findById(1L)).thenReturn(Optional.of(recipeEntity));

        when(dataMapper.toRecipeEntity(any())).thenReturn(recipeEntity);

        doThrow(new RecipeApiDatabaseException("Error updating recipe in database", new Exception()))
                .when(recipeRepository).save(recipeEntity);

        // recipeApiService.deleteEmployee(1L);

        assertThrows(
                RecipeApiDatabaseException.class,
                () -> recipeApiService.updateRecipe(TestUtils.createRecipe(),  1L));
    }

    @Test
    void updateRecipe_updateAllParams_returnSuccess() {

        RecipeEntity recipeEntity = TestUtils.createRecipeEntity();

        when(recipeRepository.findById(1L)).thenReturn(Optional.of(recipeEntity));

    }

}
