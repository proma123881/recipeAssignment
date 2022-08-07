package com.abnamro.assignment.recipes.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Getter
public class ErrorResponse {
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private final List<Error> errors = new ArrayList<>();
}
