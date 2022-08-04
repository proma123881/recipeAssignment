package com.abnamro.assignment.recipes.service;

import com.abnamro.assignment.recipes.api.model.Error;
import com.abnamro.assignment.recipes.api.model.Recipe;
import com.abnamro.assignment.recipes.api.model.RecipeId;
import com.abnamro.assignment.recipes.constant.ApiConstants;
import com.abnamro.assignment.recipes.exception.RecipeApiDatabaseException;
import com.abnamro.assignment.recipes.mapper.DataMapper;
import com.abnamro.assignment.recipes.persistence.RecipeRepository;
import com.abnamro.assignment.recipes.persistence.model.RecipeEntity;
import com.abnamro.assignment.recipes.persistence.model.RecipeEntity_;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.persistence.metamodel.SetAttribute;
import java.util.List;
import java.util.Locale;

import static org.springframework.data.jpa.domain.Specification.where;

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
                    if(newRecipe.getRecipeName() == null) {
                        newRecipe.setRecipeName(recipe.getRecipeName());
                    }
                    if(newRecipe.getIngredients() == null || newRecipe.getIngredients().isEmpty()) {
                        newRecipe.setIngredients(recipe.getIngredients());
                    }
                    if(newRecipe.getInstruction() == null) {
                        newRecipe.setInstruction(recipe.getInstruction());
                    }
                    if(newRecipe.getIsVegetarian() == null) {
                        newRecipe.setIsVegetarian(recipe.isVegetarian());
                    }
                    if(newRecipe.getNoOfServings() == null) {
                        newRecipe.setNoOfServings(recipe.getNoOfServings());
                    }
                    RecipeEntity recipeEntity1 = dataMapper.toRecipeEntity(newRecipe);
                    recipeEntity1.setId(id);
                    try {
                        return recipeRepository.save(recipeEntity1);
                    }catch (Exception e) {
                        throw new RecipeApiDatabaseException("Error updating recipe in database", e);
                    }

                })
                .orElseThrow(() -> {
                    Error error = new Error("RA-101", "NOT FOUND", "RecipeId not found in db",
                            HttpStatus.NOT_FOUND);
                    return new RecipeApiDatabaseException(error, error.getDescription());
                });
//                .orElseGet(() -> {
//                     RecipeEntity recipeEntity1 = dataMapper.toRecipeEntity(newRecipe);
//                    recipeEntity1.setId(id);
//                    return recipeRepository.save(recipeEntity1);
//                });

       return dataMapper.toRecipeId(recipeEntity);
    }

    public void deleteEmployee(Long id) {

        recipeRepository.findById(id).orElseThrow(() -> {
                    Error error = new Error("RA-101", "NOT FOUND", "RecipeId not found in db",
                            HttpStatus.NOT_FOUND);
                    return new RecipeApiDatabaseException(error, error.getDescription());
                }
        );

        try {
            recipeRepository.deleteById(id);
        } catch (Exception e) {
            throw new RecipeApiDatabaseException("Error deleting recipe from database", e);
        }

    }


    static Specification<RecipeEntity> isVegetarian(Boolean isVegetarian) {
        if (isVegetarian != null) {
            return (recipe, cq, cb) -> cb.equal(recipe.get("isVegetarian"), isVegetarian);
        } else {
            return null;
        }


    }

    private Specification<RecipeEntity> instructionContains(String instructionContains) {
        if (instructionContains != null) {
            return (recipe, cq, cb) -> cb.like(cb.lower(recipe.get("instruction")), "%" + instructionContains.toLowerCase() + "%");
        } else {
            return null;
        }

    }


    private Specification<RecipeEntity> equalsNoOfServings(Integer noOfServings) {
        if (noOfServings != null) {
            return (recipe, cq, cb) -> cb.equal(recipe.get("noOfServings"), noOfServings);
        } else {
            return null;
        }

    }

    private Specification<RecipeEntity> containtsIngredients(String ingredient) {
        if (ingredient != null) {
            return (recipe, cq, cb) -> cb.isMember(ingredient, recipe.get(RecipeEntity_.ingredients));
        } else {
            return null;
        }
    }

    private Specification<RecipeEntity> notcontaintsIngredients(String ingredient) {
        if (ingredient != null) {
            return (recipe, cq, cb) -> cb.isNotMember(ingredient, recipe.get(RecipeEntity_.ingredients));
        } else {
            return null;
        }
    }


    public List<RecipeEntity> getAllRecipes(Boolean isVegetarian, String instructionContains, Integer noOfServings, List<String> presentIngredients,
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

        List<RecipeEntity> recipes = recipeRepository.findAll(spec);

        if(recipes == null || recipes.isEmpty()){
            Error error = new Error("RA-102", "NOT FOUND",
                    "No recipes found for the given search criteria",
                    HttpStatus.NOT_FOUND);
            throw new RecipeApiDatabaseException(error, error.getDescription());

        }

        return recipes;


    }
}
