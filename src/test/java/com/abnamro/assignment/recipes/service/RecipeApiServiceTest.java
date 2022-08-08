package com.abnamro.assignment.recipes.service;


import com.abnamro.assignment.recipes.BaseIntegrationTest;
import com.abnamro.assignment.recipes.api.model.Recipe;
import com.abnamro.assignment.recipes.api.model.RecipeId;
import com.abnamro.assignment.recipes.api.model.RecipesResponse;
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
import org.mockito.Mockito;
import org.springframework.data.jpa.domain.Specification;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
                () -> recipeApiService.addRecipe(TestUtils.createRecipe()));

        String expectedMessage = ApiConstants.DATABASE_ERROR_MESSAGE_SAVE;
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));


    }

    @Test
    void deleteRecipe_happyFlow_returnSuccess() {

        RecipeEntity recipeEntity = TestUtils.createRecipeEntity();

        when(recipeRepository.findById(1L)).thenReturn(Optional.of(recipeEntity));

        recipeApiService.deleteRecipe(1L);

        assertDoesNotThrow(() -> recipeApiService.deleteRecipe(1L));


    }

    @Test
    void deleteRecipe_idNotFound_throwException() {

        when(recipeRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(
                RecipeApiDatabaseException.class,
                () -> recipeApiService.deleteRecipe(1L));
    }

    @Test
    void deleteRecipe_errorWhileDbDelete_throwException() {

        RecipeEntity recipeEntity = TestUtils.createRecipeEntity();

        when(recipeRepository.findById(1L)).thenReturn(Optional.of(recipeEntity));

        doThrow(new RecipeApiDatabaseException(ApiConstants.DATABASE_ERROR_MESSAGE_DELETE, new Exception()))
                .when(recipeRepository).deleteById(1L);

        assertThrows(
                RecipeApiDatabaseException.class,
                () -> recipeApiService.deleteRecipe(1L));
    }


    @Test
    void updateRecipe_idNotFound_throwException() {

        when(recipeRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(
                RecipeApiDatabaseException.class,
                () -> recipeApiService.updateRecipe(TestUtils.createRecipe(), 1L));
    }

    @Test
    void updateRecipe_errorWhileDbUpdate_throwException() {

        RecipeEntity recipeEntity = TestUtils.createRecipeEntity();

        when(recipeRepository.findById(1L)).thenReturn(Optional.of(recipeEntity));

        when(dataMapper.toRecipeEntity(any())).thenReturn(recipeEntity);

        doThrow(new RecipeApiDatabaseException(ApiConstants.DATABASE_ERROR_MESSAGE_UPDATE, new Exception()))
                .when(recipeRepository).saveAndFlush(recipeEntity);


        assertThrows(
                RecipeApiDatabaseException.class,
                () -> recipeApiService.updateRecipe(TestUtils.createRecipe(), 1L));
    }

    @Test
    void updateRecipe_updateAllParams_returnSuccess() {

        RecipeEntity recipeEntity = TestUtils.createRecipeEntity();

        Recipe newRecipe = new Recipe();
        newRecipe.setRecipeName("cake");
        newRecipe.setIsVegetarian(false);
        newRecipe.setNoOfServings(3);
        newRecipe.setInstruction("whisk everything");
        Set<String> ingredients = new HashSet<>(Arrays.asList("egg", "flour"));
        newRecipe.setIngredients(ingredients);

        RecipeEntity newRecipeEntity = new RecipeEntity();
        newRecipeEntity.setRecipeName("cake");
        newRecipeEntity.setIsVegetarian(false);
        newRecipeEntity.setNoOfServings(3);
        newRecipeEntity.setInstruction("whisk everything");
        Set<String> ingredientSet = new HashSet<>(Arrays.asList("egg", "flour"));
        newRecipeEntity.setIngredients(ingredientSet);

        when(dataMapper.toRecipeEntity(newRecipe)).thenReturn(newRecipeEntity);
        when(recipeRepository.findById(1L)).thenReturn(Optional.of(recipeEntity));
        when(recipeRepository.saveAndFlush(any())).thenReturn(newRecipeEntity);


        assertDoesNotThrow(() -> recipeApiService.updateRecipe(newRecipe, 1L));

        Mockito.verify(recipeRepository, times(1)).saveAndFlush(any());


    }

    @Test
    void updateRecipe_updateIsVegetarian_returnSuccess() {

        RecipeEntity recipeEntity = TestUtils.createRecipeEntity();

        Recipe newRecipe = new Recipe();
        newRecipe.setIsVegetarian(false);

        RecipeEntity newRecipeEntity = new RecipeEntity();
        newRecipeEntity.setIsVegetarian(true);

        when(dataMapper.toRecipeEntity(newRecipe)).thenReturn(newRecipeEntity);
        when(recipeRepository.findById(1L)).thenReturn(Optional.of(recipeEntity));
        when(recipeRepository.saveAndFlush(any())).thenReturn(newRecipeEntity);


        assertDoesNotThrow(() -> recipeApiService.updateRecipe(newRecipe, 1L));

        Mockito.verify(recipeRepository, times(1)).saveAndFlush(any());


    }

    @Test
    void updateRecipe_updateRecipeName_returnSuccess() {

        RecipeEntity recipeEntity = TestUtils.createRecipeEntity();

        Recipe newRecipe = new Recipe();
        newRecipe.setRecipeName("cake");

        RecipeEntity newRecipeEntity = new RecipeEntity();
        newRecipeEntity.setRecipeName("cake");

        when(dataMapper.toRecipeEntity(newRecipe)).thenReturn(newRecipeEntity);
        when(recipeRepository.findById(1L)).thenReturn(Optional.of(recipeEntity));
        when(recipeRepository.saveAndFlush(any())).thenReturn(newRecipeEntity);


        assertDoesNotThrow(() -> recipeApiService.updateRecipe(newRecipe, 1L));

        Mockito.verify(recipeRepository, times(1)).saveAndFlush(any());


    }

    @Test
    void getRecipe_getAllRecipes_returnSuccess() {


        List<RecipeEntity> recipeEntities = new ArrayList<>();
        recipeEntities.add(TestUtils.createRecipeEntity());

        when(recipeRepository.findAll(any(Specification.class))).thenReturn(recipeEntities);

        RecipesResponse recipesResponse = recipeApiService.getAllRecipes(null,
                null, null,
                null, null);

        assertEquals(recipesResponse.getRecipes().size(), 1);


    }

    @Test
    void getRecipe_getOnlyVegitarianRecipes_returnSuccess() {


        List<RecipeEntity> recipeEntities = new ArrayList<>();
        RecipeEntity recipeEntity = TestUtils.createRecipeEntity();
        recipeEntity.setIsVegetarian(true);
        recipeEntities.add(TestUtils.createRecipeEntity());

        when(recipeRepository.findAll(any(Specification.class))).thenReturn(recipeEntities);

        RecipesResponse recipesResponse = recipeApiService.getAllRecipes(true,
                null, null,
                null, null);

        assertEquals(recipesResponse.getRecipes().size(), 1);


    }

    @Test
    void getRecipe_getByNoOfServings_returnSuccess() {


        List<RecipeEntity> recipeEntities = new ArrayList<>();
        RecipeEntity recipeEntity = TestUtils.createRecipeEntity();
        recipeEntity.setNoOfServings(5);
        recipeEntities.add(TestUtils.createRecipeEntity());


        when(recipeRepository.findAll(any(Specification.class))).thenReturn(recipeEntities);

        RecipesResponse recipesResponse = recipeApiService.getAllRecipes(null,
                null, 5,
                null, null);

        assertEquals(recipesResponse.getRecipes().size(), 1);


    }


    @Test
    void getRecipe_getByPresentIngredients_returnSuccess() {


        List<RecipeEntity> recipeEntities = new ArrayList<>();
        RecipeEntity recipeEntity = TestUtils.createRecipeEntity();
        recipeEntities.add(recipeEntity);

        when(recipeRepository.findAll(any(Specification.class))).thenReturn(recipeEntities);

        RecipesResponse recipesResponse = recipeApiService.getAllRecipes(null,
                null, null,
                Arrays.asList("sugar"), null);

        assertEquals(recipesResponse.getRecipes().size(), 1);


    }


    @Test
    void getRecipe_getByAbsentIngredients_returnSuccess() {


        List<RecipeEntity> recipeEntities = new ArrayList<>();
        RecipeEntity recipeEntity = TestUtils.createRecipeEntity();
        recipeEntities.add(recipeEntity);

        when(recipeRepository.findAll(any(Specification.class))).thenReturn(recipeEntities);

        RecipesResponse recipesResponse = recipeApiService.getAllRecipes(null,
                null, null,
                null, Arrays.asList("eggs"));

        assertEquals(recipesResponse.getRecipes().size(), 1);


    }


    @Test
    void getRecipe_getByAllSearchCriterias_returnSuccess() {


        List<RecipeEntity> recipeEntities = new ArrayList<>();
        RecipeEntity recipeEntity = TestUtils.createRecipeEntity();
        recipeEntities.add(recipeEntity);

        when(recipeRepository.findAll(any(Specification.class))).thenReturn(recipeEntities);

        RecipesResponse recipesResponse = recipeApiService.getAllRecipes(false,
                "put", 3,
                Arrays.asList("sugar"), Arrays.asList("eggs"));

        assertEquals(recipesResponse.getRecipes().size(), 1);


    }


    @Test
    void getRecipe_idNotFound_throwException() {

        when(recipeRepository.findById(1L)).thenReturn(Optional.empty());


        assertThrows(
                RecipeApiDatabaseException.class,
                () -> recipeApiService.getAllRecipes(null, null, null, null, null));
    }


}
