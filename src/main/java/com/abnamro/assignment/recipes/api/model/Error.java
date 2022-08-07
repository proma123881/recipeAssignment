package com.abnamro.assignment.recipes.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/** Error model class for Recipe API application.
 * @author Proma Chowdhury
 * @version 1.0
 */
@Getter
public class Error {

    /**
     * Constructor to create a new Error.
     * @param code error code
     * @param name error name
     * @param description  error description
     */
    public Error(String code, String name, String description) {
        this.code = code;
        this.name = name;
        this.description = description;
    }

    /**
     * Constructor to create a new Error.
     * @param code error code
     * @param name error name
     * @param description  error description
     * @param httpStatus http status for the error code
     */
    public Error(String code, String name, String description, HttpStatus httpStatus) {
        this.code = code;
        this.name = name;
        this.description = description;
        this.httpStatus = httpStatus;
    }
    /**
     * Constructor to create a new Error.
     * @param code error code
     * @param name error name
     * @param description  error description
     * @param followupUri http uri at which the error happened
     */

    public Error(String code, String name, String description, String followupUri) {
        this(code, name, description);
        this.name = name;
        this.followupUri = followupUri;
    }


    private final String code;

    private String name;

    private final String description;

    @JsonIgnore
    private HttpStatus httpStatus;

    private String followupUri;

}

