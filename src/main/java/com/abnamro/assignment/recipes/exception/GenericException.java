package com.abnamro.assignment.recipes.exception;

import lombok.Getter;
import com.abnamro.assignment.recipes.api.model.Error;

/** GenericException Handler for the API
 * @author Proma Chowdhury
 * @version 1.0
 */
@Getter
public class GenericException extends RuntimeException {

    //private Error error;

    public GenericException(String description) {
        super(description);
    }

    public GenericException(String description, Exception cause) {
        super(description, cause);
    }

}
