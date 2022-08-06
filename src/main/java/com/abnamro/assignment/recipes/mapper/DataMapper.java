package com.abnamro.assignment.recipes.mapper;

import com.abnamro.assignment.recipes.api.model.Recipe;
import com.abnamro.assignment.recipes.api.model.RecipeId;
import com.abnamro.assignment.recipes.api.model.RecipeResponse;
import com.abnamro.assignment.recipes.api.model.UpdateRecipeResponse;
import com.abnamro.assignment.recipes.persistence.model.RecipeEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface DataMapper {


    RecipeId toRecipeId(RecipeEntity recipeEntity);

    RecipeEntity toRecipeEntity(Recipe recipe);

    RecipeResponse toRecipeResponse(RecipeEntity recipeEntity);

    UpdateRecipeResponse toUpdateRecipeResponse(RecipeEntity recipeEntity);

}
