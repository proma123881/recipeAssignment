package com.abnamro.assignment.recipes.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class Error {



    public Error(String code, String name, String description) {
        this.code = code;
        this.name = name;
        this.description = description;
    }


    public Error(String code, String name, String description, HttpStatus httpStatus) {
        this.code = code;
        this.name = name;
        this.description = description;
        this.httpStatus = httpStatus;
    }

    public Error(String code, String name, String description, String followupURI) {
        this(code, name, description);
        this.name = name;
        this.followupURI = followupURI;
    }



    private String code;


    private String name;

    private String description;

    @JsonIgnore
    private HttpStatus httpStatus;

    private String followupURI;

}

