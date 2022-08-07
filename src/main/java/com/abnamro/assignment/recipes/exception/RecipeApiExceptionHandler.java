package com.abnamro.assignment.recipes.exception;

import com.abnamro.assignment.recipes.api.model.Error;
import com.abnamro.assignment.recipes.api.model.RecipeApiResponse;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


/** RecipeApiExceptionHandler Handler for the API.
 * @author Proma Chowdhury
 * @version 1.0
 */

@ControllerAdvice
@Slf4j
public class RecipeApiExceptionHandler extends ResponseEntityExceptionHandler {


    /**
     * Builds a followUp uri.
     *
     * @param request httpServletRequest
     * @return URI
     */
    private String getRequestUriWithParameters(HttpServletRequest request) {
        String queryParameters = request.getQueryString();
        return request.getRequestURI() + (StringUtils.isBlank(queryParameters) ? "" : "?" + request.getQueryString());
    }

    /**
     * Builds Error for the Response Entity.
     *
     * @param exception GenericException
     * @param loggingMessage error message to log
     * @param requestUri request uri
     * @return ResponseEntity response entity to return
     */
    private ResponseEntity<?> handleGenericError(GenericException exception, String loggingMessage,
                                                 String requestUri) {
        log.error(loggingMessage, exception);
        Error error = exception.getError();
        String errorCode = error.getCode();
        String errorName = error.getName();
        String errorDescription = exception.getMessage();
        RecipeApiResponse recipeApiResponse = createErrorResponse(new Error(errorCode,
                errorName, errorDescription, requestUri));
        HttpStatus httpStatus = exception.getError() == null ? HttpStatus.INTERNAL_SERVER_ERROR :
                exception.getError().getHttpStatus();
        return ResponseEntity.status(httpStatus).body(recipeApiResponse);
    }

    /**
     * Adds Error for the RecipeApiResponse.
     *
     * @param error error
     * @return RecipeApiResponse
     */
    private RecipeApiResponse createErrorResponse(Error error) {
        RecipeApiResponse recipeApiResponse = new RecipeApiResponse();
        recipeApiResponse.getErrors().add(error);
        return recipeApiResponse;
    }

    /**
     * Handles RecipeApiDatabaseException exception.
     *
     * @param backendException backend exception
     * @param request request
     * @return ResponseEntity response entity to return
     */

    @org.springframework.web.bind.annotation.ExceptionHandler(RecipeApiDatabaseException.class)
    public ResponseEntity<?> handleBackendException(RecipeApiDatabaseException backendException,
                                                    HttpServletRequest request) {
        String requestUri = getRequestUriWithParameters(request);
        String loggingMessage = String.format(backendException.getMessage() + " at '%s'", requestUri);
        return handleGenericError(backendException, loggingMessage, requestUri);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        List<String> errors = new ArrayList<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }

        return handleExceptionInternal(
                ex, errors, headers, status, request);
    }


}
