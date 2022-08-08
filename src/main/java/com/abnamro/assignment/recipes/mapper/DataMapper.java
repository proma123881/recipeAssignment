package com.abnamro.assignment.recipes.mapper;

import com.abnamro.assignment.recipes.api.model.Recipe;
import com.abnamro.assignment.recipes.api.model.RecipeId;
import com.abnamro.assignment.recipes.api.model.RecipeResponse;
import com.abnamro.assignment.recipes.api.model.UpdateRecipeResponse;
import com.abnamro.assignment.recipes.persistence.model.RecipeEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface DataMapper {


    RecipeId toRecipeId(RecipeEntity recipeEntity);

    @Mapping(source = "ingredients", target = "ingredients", qualifiedByName = "toLowerCase")
    @Mapping(source = "recipeName", target = "recipeName", qualifiedByName = "toLowerCaseString")
    @Mapping(source = "instruction", target = "instruction", qualifiedByName = "toLowerCaseString")
    RecipeEntity toRecipeEntity(Recipe recipe);

    RecipeResponse toRecipeResponse(RecipeEntity recipeEntity);

    UpdateRecipeResponse toUpdateRecipeResponse(RecipeEntity recipeEntity);

    @Named("toLowerCase")
    static Set<String> toLowerCaseSetOfStrings(Set<String> ingredients) {
       return ingredients.stream()
                .map(String::toLowerCase)
                .collect(Collectors.toSet());
    }

    @Named("toLowerCaseString")
    static String toLowerCaseString(String value) {
        return value.toLowerCase();
    }

}
