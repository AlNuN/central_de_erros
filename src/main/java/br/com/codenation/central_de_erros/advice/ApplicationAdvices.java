package br.com.codenation.central_de_erros.advice;

import br.com.codenation.central_de_erros.controller.EventController;
import br.com.codenation.central_de_erros.exception.EventNotFoundException;
import br.com.codenation.central_de_erros.exception.WrongUserInputException;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

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
    ResponseEntity<Resource<ApiError>> dateTimeParseExceptionHandler(WrongUserInputException ex) {
        ApiError error = new ApiError(HttpStatus.BAD_REQUEST, ex.getMessage());
        return buildResponseEntity(error);
    }

    private ResponseEntity<Resource<ApiError>> buildResponseEntity(ApiError apiError){
        Resource<ApiError> resource = new Resource<>(apiError);
        resource.add(ControllerLinkBuilder.linkTo(EventController.class) .withRel("events"));
        return new ResponseEntity<>(resource, apiError.getStatus());
    }

}
