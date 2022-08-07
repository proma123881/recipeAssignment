package com.abnamro.assignment.recipes.controller;

import com.abnamro.assignment.recipes.BaseIntegrationTest;
import com.abnamro.assignment.recipes.api.model.Recipe;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RecipeControllerIT extends BaseIntegrationTest {

    @Test
    void addRecipes_happyFlow_returnIsCreated() throws Exception {

        Recipe recipe = new Recipe();
        recipe.setRecipeName("muffin");
        recipe.setIsVegetarian(false);
        recipe.setNoOfServings(4);
        recipe.setInstruction("put chocolate");
        Set<String> ingredients = new HashSet<>(Arrays.asList("chocolate", "sugar"));
        recipe.setIngredients(ingredients);

        mockMvc.perform(post("/recipes")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(recipe))).andExpect(status().isCreated())
                .andDo(print())
                .andExpect(jsonPath("$.id", is(1)));

    }

    @Test
    void addRecipes_recipeNameNotPresent_returnBadRequest() throws Exception {

        Recipe recipe = new Recipe();
        recipe.setIsVegetarian(false);
        recipe.setNoOfServings(4);
        recipe.setInstruction("put chocolate");
        Set<String> ingredients = new HashSet<>(Arrays.asList("chocolate", "sugar"));
        recipe.setIngredients(ingredients);

        ResultActions resultActions = mockMvc.perform(post("/recipes")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(recipe))).andExpect(status().isBadRequest())
                .andDo(print());

        assertTrue(resultActions.andReturn().getResponse().getContentAsString()
                .contains("recipeName: must not be blank"));


    }

    @Test
    void addRecipes_recipeNameBlank_returnBadRequest() throws Exception {

        Recipe recipe = new Recipe();
        recipe.setRecipeName("");
        recipe.setIsVegetarian(false);
        recipe.setNoOfServings(4);
        recipe.setInstruction("put chocolate");
        Set<String> ingredients = new HashSet<>(Arrays.asList("chocla", "sugara"));
        recipe.setIngredients(ingredients);

        ResultActions resultActions = mockMvc.perform(post("/recipes")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(recipe))).andExpect(status().isBadRequest())
                .andDo(print());

        assertTrue(resultActions.andReturn().getResponse().getContentAsString()
                .contains("recipeName: must not be blank"));


    }

    @Test
    void addRecipes_isVegetarianNotPresent_returnBadRequest() throws Exception {

        Recipe recipe = new Recipe();
        recipe.setRecipeName("muffin");
        recipe.setNoOfServings(4);
        recipe.setInstruction("put chocolate");
        Set<String> ingredients = new HashSet<>(Arrays.asList("chocla", "sugara"));
        recipe.setIngredients(ingredients);

        ResultActions resultActions = mockMvc.perform(post("/recipes")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(recipe))).andExpect(status().isBadRequest())
                .andDo(print());

        assertTrue(resultActions.andReturn().getResponse().getContentAsString()
                .contains("isVegetarian: must not be null"));


    }

    @Test
    void addRecipes_noOfServingsNotPresent_returnBadRequest() throws Exception {

        Recipe recipe = new Recipe();
        recipe.setRecipeName("muffin");
        recipe.setIsVegetarian(false);
        recipe.setInstruction("put chocolate");
        Set<String> ingredients = new HashSet<>(Arrays.asList("chocla", "sugara"));
        recipe.setIngredients(ingredients);

        ResultActions resultActions = mockMvc.perform(post("/recipes")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(recipe))).andExpect(status().isBadRequest())
                .andDo(print());

        assertTrue(resultActions.andReturn().getResponse().getContentAsString()
                .contains("noOfServings: must not be null"));


    }

    @Test
    void addRecipes_noOfServingsLessThanOne_returnBadRequest() throws Exception {

        Recipe recipe = new Recipe();
        recipe.setRecipeName("muffin");
        recipe.setIsVegetarian(false);
        recipe.setNoOfServings(0);
        recipe.setInstruction("put chocolate");
        Set<String> ingredients = new HashSet<>(Arrays.asList("chocla", "sugara"));
        recipe.setIngredients(ingredients);

        ResultActions resultActions = mockMvc.perform(post("/recipes")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(recipe))).andExpect(status().isBadRequest())
                .andDo(print());

        assertTrue(resultActions.andReturn().getResponse().getContentAsString()
                .contains("noOfServings: must be greater than 0"));


    }

    @Test
    void addRecipes_instructionNotPresent_returnCreated() throws Exception {

        Recipe recipe = new Recipe();
        recipe.setRecipeName("muffin");
        recipe.setIsVegetarian(false);
        recipe.setNoOfServings(4);
        recipe.setInstruction("put chocolate");
        Set<String> ingredients = new HashSet<>(Arrays.asList("chocla", "sugara"));
        recipe.setIngredients(ingredients);

        mockMvc.perform(post("/recipes")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(recipe))).andExpect(status().isCreated())
                .andDo(print());

    }

    @Test
    void addRecipes_ingredientNotPresent_returnBadRequest() throws Exception {

        Recipe recipe = new Recipe();
        recipe.setRecipeName("muffin");
        recipe.setIsVegetarian(false);
        recipe.setNoOfServings(4);
        recipe.setInstruction("put chocolate");

        ResultActions resultActions = mockMvc.perform(post("/recipes")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(recipe))).andExpect(status().isBadRequest())
                .andDo(print());

        assertTrue(resultActions.andReturn().getResponse().getContentAsString()
                .contains("ingredients: must not be empty"));

    }

    @Test
    void addRecipes_ingredientListEmpty_returnBadRequest() throws Exception {

        Recipe recipe = new Recipe();
        recipe.setRecipeName("muffin");
        recipe.setIsVegetarian(false);
        recipe.setNoOfServings(4);
        recipe.setInstruction("put chocolate");
        Set<String> ingredients = new HashSet<>();
        recipe.setIngredients(ingredients);

        ResultActions resultActions = mockMvc.perform(post("/recipes")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(recipe))).andExpect(status().isBadRequest())
                .andDo(print());

        assertTrue(resultActions.andReturn().getResponse().getContentAsString().contains("ingredients: must not be empty"));

    }


    @Test
    void deleteRecipes_happyFlow_returnSuccess() throws Exception {

        this.mockMvc.perform(MockMvcRequestBuilders
                .delete("/recipes/80")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent()).andDo(print());

    }

    @Test
    void deleteRecipes_idNotPresent_returnNotFound() throws Exception {

        this.mockMvc.perform(MockMvcRequestBuilders
                .delete("/recipes/123")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()).andDo(print());
    }

    @Test
    public void getAllRecipes_searchByIsVegetartianTrue_returnSuccess() throws Exception {

        this.mockMvc.perform(MockMvcRequestBuilders
                .get("/recipes").param("isVegetarian", String.valueOf(true))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].isVegetarian").value(true))
                .andExpect(jsonPath("$[1].isVegetarian").value(true));

    }

    @Test
    public void getAllRecipes_searchByIsVegetartianFalse_returnSuccess() throws Exception {

        this.mockMvc.perform(MockMvcRequestBuilders
                .get("/recipes").param("isVegetarian", String.valueOf(false))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].isVegetarian").value(false));

    }

    @Test
    public void getAllRecipes_searchByNoOfServings_returnSuccess() throws Exception {

        this.mockMvc.perform(MockMvcRequestBuilders
                .get("/recipes").param("noOfServings", String.valueOf(1))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].noOfServings").value(1));

    }


    @Test
    public void getAllRecipes_searchByInstruction_returnSuccess() throws Exception {

        this.mockMvc.perform(MockMvcRequestBuilders
                .get("/recipes").param("instructionContains", "oven")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].instruction").value("put chocolate and baking soda together in oven"));

    }

    @Test
    public void getAllRecipes_searchByIngredientContains_returnSuccess() throws Exception {

        this.mockMvc.perform(MockMvcRequestBuilders
                .get("/recipes").param("presentIngredients", "sugar")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].ingredients.[0]").value("sugar"));


    }

    @Test
    public void getAllRecipes_searchByIngredientContainsMultipleValues_returnSuccess() throws Exception {

        this.mockMvc.perform(MockMvcRequestBuilders
                .get("/recipes").param("presentIngredients", "sugar", "chocolate")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].ingredients.[0]").value("sugar"))
                .andExpect(jsonPath("$[0].ingredients.[1]").value("chocolate"));


    }

    @Test
    public void getAllRecipes_searchByIngredientNotContains_returnSuccess() throws Exception {

        this.mockMvc.perform(MockMvcRequestBuilders
                .get("/recipes").param("absentIngredients", "chicken")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].ingredients.[0]").value("sugar"))
                .andExpect(jsonPath("$[0].ingredients.[1]").value("chocolate"));

    }


    @Test
    public void getAllRecipes_searchCriteriaNotMatch_returnNotFound() throws Exception {

        this.mockMvc.perform(MockMvcRequestBuilders
                .get("/recipes").param("presentIngredients", "fish")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());

    }

    @Test
    public void getAllRecipes_allSearchCriteriaMatched_returnSuccess() throws Exception {

        this.mockMvc.perform(MockMvcRequestBuilders
                .get("/recipes").param("presentIngredients", "fish")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());

    }

    @Test
    public void updateRecipe_recipeNotFound_returnNotFound() throws Exception {

        Recipe recipe = new Recipe();
        recipe.setRecipeName("muffin");
        recipe.setIsVegetarian(false);
        recipe.setNoOfServings(4);
        recipe.setInstruction("put chocolate");
        Set<String> ingredients = new HashSet<>(Arrays.asList("chocla", "sugara"));
        recipe.setIngredients(ingredients);

        this.mockMvc.perform(MockMvcRequestBuilders
                .put("/recipes/{id}", 123)
                .content(objectMapper.writeValueAsString(recipe))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()).andDo(print());


    }

    @Test
    public void updateRecipe_updateAllFields_returnOk() throws Exception {

        Recipe recipe = new Recipe();
        recipe.setRecipeName("muffin");
        recipe.setIsVegetarian(true);
        recipe.setNoOfServings(4);
        recipe.setInstruction("put chocolate");
        Set<String> ingredients = new HashSet<>(Arrays.asList("chocla", "sugara"));
        recipe.setIngredients(ingredients);

        this.mockMvc.perform(MockMvcRequestBuilders
                .put("/recipes/{id}", 81)
                .content(objectMapper.writeValueAsString(recipe))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("81"))
                .andDo(print());
    }

    @Test
    public void updateRecipe_updateRecipeName_returnOk() throws Exception {

        Recipe recipe = new Recipe();
        recipe.setRecipeName("muffin");

        this.mockMvc.perform(MockMvcRequestBuilders
                .put("/recipes/{id}", 81)
                .content(objectMapper.writeValueAsString(recipe))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("81"))
                .andDo(print());


    }

    @Test
    public void updateRecipe_updateIsVegetarian_returnOk() throws Exception {

        Recipe recipe = new Recipe();
        recipe.setIsVegetarian(false);

        this.mockMvc.perform(MockMvcRequestBuilders
                .put("/recipes/{id}", 82)
                .content(objectMapper.writeValueAsString(recipe))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("82"))
                .andDo(print());
    }


}
