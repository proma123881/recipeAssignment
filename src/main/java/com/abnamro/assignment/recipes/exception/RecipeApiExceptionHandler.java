package com.abnamro.assignment.recipes.exception;

import com.abnamro.assignment.recipes.api.model.RecipeApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.apache.commons.lang3.StringUtils;
import com.abnamro.assignment.recipes.api.model.Error;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
@Slf4j
public class RecipeApiExceptionHandler extends ResponseEntityExceptionHandler  {


    /**
     * Builds a followUp uri
     * @param request
     * @return URI
     */
    private String getRequestUriWithParameters(HttpServletRequest request) {
        String queryParameters = request.getQueryString();
        return request.getRequestURI() + (StringUtils.isBlank(queryParameters) ? "" : "?" + request.getQueryString());
    }

    /**
     * Builds Error for the Response Entity
     * @param exception
     * @param loggingMessage
     * @param requestURI
     * @return ResponseEntity
     */
    private ResponseEntity<?> handleGenericError(GenericException exception, String loggingMessage, String requestURI) {
        log.error(loggingMessage, exception);
        String errorCode = exception.getError().getCode();
        String errorName = exception.getError().getName();
        String errorDescription = exception.getMessage();
        RecipeApiResponse recipeApiResponse = createErrorResponse(new Error(errorCode, errorName, errorDescription, requestURI));
        HttpStatus httpStatus = exception.getError() == null ? HttpStatus.INTERNAL_SERVER_ERROR : exception.getError().getHttpStatus();
        return ResponseEntity.status(httpStatus).body(recipeApiResponse);
    }

    /**
     * Adds Error for the MusicApiResponse
     * @param error
     * @return musicAPIResponse
     */
    private RecipeApiResponse createErrorResponse(Error error) {
        RecipeApiResponse recipeApiResponse = new RecipeApiResponse();
        recipeApiResponse.getErrors().add(error);
        return recipeApiResponse;
    }

//    @ExceptionHandler(ConstraintViolationException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException e) {
//        return new ResponseEntity<>("not valid due to validation error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
//    }

    /**
     * Handles mongodb exception
     * @param backendException
     * @param request
     * @return ResponseEntity
     */

    @org.springframework.web.bind.annotation.ExceptionHandler(RecipeApiDatabaseException.class)
    public ResponseEntity<?> handleBackendException(RecipeApiDatabaseException backendException, HttpServletRequest request) {
        String requestURI = getRequestUriWithParameters(request);
        String loggingMessage = String.format(backendException.getMessage() + " at '%s'", requestURI);
        return handleGenericError(backendException, loggingMessage, requestURI);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        List<String> errors = new ArrayList<String>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }

        return handleExceptionInternal(
                ex, errors, headers, status, request);
    }


}
