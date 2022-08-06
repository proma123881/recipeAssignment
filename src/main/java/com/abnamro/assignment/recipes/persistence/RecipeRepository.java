package com.abnamro.assignment.recipes.persistence;

import com.abnamro.assignment.recipes.persistence.model.RecipeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeRepository  extends JpaRepository<RecipeEntity, Long>, JpaSpecificationExecutor<RecipeEntity> {

}
