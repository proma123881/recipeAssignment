package com.abnamro.assignment.recipes.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
//@Setter
public class ErrorResponse {
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<Error> errors = new ArrayList<>();

//    public void addError(Error error) {
//        if (error != null && !errors.stream().anyMatch(e -> e.getCode().equals(error.getCode()))) {
//            errors.add(error);
//        }
//    }

//    public Error getError(String errorCode) {
//        return errors.stream()
//                .filter(error -> error.getCode().equals(errorCode))
//                .findFirst()
//                .orElse(null);
//    }
}
