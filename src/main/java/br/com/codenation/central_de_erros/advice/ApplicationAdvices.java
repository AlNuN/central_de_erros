package br.com.codenation.central_de_erros.advice;

import br.com.codenation.central_de_erros.controller.EventController;
import br.com.codenation.central_de_erros.exception.EventNotFoundException;
import br.com.codenation.central_de_erros.exception.WrongUserInputException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.ConstraintViolationException;
import java.time.format.DateTimeParseException;

@ControllerAdvice
public class ApplicationAdvices  {

    @ResponseBody
    @ExceptionHandler(EventNotFoundException.class)
    ResponseEntity<Resource<ApiError>> eventNotFoundHandler(EventNotFoundException ex){
        ApiError error = new ApiError(HttpStatus.NOT_FOUND, ex.getMessage());
        return buildResponseEntity(error);
    }

    @ResponseBody
    @ExceptionHandler(WrongUserInputException.class)
    ResponseEntity<Resource<ApiError>> WrongInputExceptionHandler(WrongUserInputException ex) {
        ApiError error = new ApiError(HttpStatus.BAD_REQUEST, ex.getMessage());
        return buildResponseEntity(error);
    }

    @ResponseBody
    @ExceptionHandler(DateTimeParseException.class)
    ResponseEntity<Resource<ApiError>> dateTimeParseExceptionHandler(DateTimeParseException ex) {
        ApiError error = new ApiError(HttpStatus.BAD_REQUEST, ex.getMessage());
        return buildResponseEntity(error);
    }

    @ResponseBody
    @ExceptionHandler(IllegalArgumentException.class)
    ResponseEntity<Resource<ApiError>> IllegalArgumentExceptionHandler(IllegalArgumentException ex) {
        ApiError error = new ApiError(HttpStatus.BAD_REQUEST, ex.getMessage());
        return buildResponseEntity(error);
    }

    @ResponseBody
    @ExceptionHandler(IllegalStateException.class)
    ResponseEntity<Resource<ApiError>> IllegalStateExceptionHandler(IllegalStateException ex) {
        ApiError error = new ApiError(HttpStatus.BAD_REQUEST, getCause(ex).getMessage());
        return buildResponseEntity(error);
    }

    @ResponseBody
    @ExceptionHandler(DataIntegrityViolationException.class)
    ResponseEntity<Resource<ApiError>> DataIntegrityExceptionHandler(DataIntegrityViolationException ex) {
        ApiError error = new ApiError(HttpStatus.BAD_REQUEST, getCause(ex).getMessage());
        return buildResponseEntity(error);
    }

    @ResponseBody
    @ExceptionHandler(ConstraintViolationException.class)
    ResponseEntity<Resource<ApiError>> ConstraintViolationHandler(ConstraintViolationException ex) {
        ApiError error = new ApiError(HttpStatus.BAD_REQUEST, getCause(ex).getMessage());
        return buildResponseEntity(error);
    }

    @ResponseBody
    @ExceptionHandler(HttpMessageNotReadableException.class)
    ResponseEntity<Resource<ApiError>> HttpMessageNotReadableHandler(HttpMessageNotReadableException ex) {
        ApiError error = new ApiError(HttpStatus.BAD_REQUEST, getCause(ex).getMessage());
        return buildResponseEntity(error);
    }

    private ResponseEntity<Resource<ApiError>> buildResponseEntity(ApiError apiError){
        Resource<ApiError> resource = new Resource<>(apiError);
        resource.add(ControllerLinkBuilder.linkTo(EventController.class) .withRel("events"));
        return new ResponseEntity<>(resource, apiError.getStatus());
    }

    Throwable getCause(Throwable e) {
        Throwable cause = null;
        Throwable result = e;

        while(null != (cause = result.getCause())  && (result != cause) ) {
            result = cause;
        }
        return result;
    }
}
