package com.abnamro.assignment.recipes.exception;

import com.abnamro.assignment.recipes.api.model.Error;
import lombok.Getter;

/** RecipeApiDatabaseException Handler for the API.
 * @author Proma Chowdhury
 * @version 1.0
 */
@Getter
public class RecipeApiDatabaseException  extends GenericException {

    private Error error;

    public RecipeApiDatabaseException(Error error, String message) {
        super(message);
        this.error = error;
    }


    public RecipeApiDatabaseException(String message, Exception cause) {
        super(message, cause);
    }
}