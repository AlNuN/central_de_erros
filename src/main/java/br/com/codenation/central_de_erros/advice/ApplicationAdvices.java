package br.com.codenation.central_de_erros.advice;

import br.com.codenation.central_de_erros.exception.EventNotFoundException;
import br.com.codenation.central_de_erros.exception.WrongUserInputException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class ApplicationAdvices  {

    @ResponseBody
    @ExceptionHandler(EventNotFoundException.class)
    ResponseEntity<Object> eventNotFoundHandler(EventNotFoundException ex){
        ApiError error = new ApiError(HttpStatus.NOT_FOUND, ex.getMessage());
        return buildResponseEntity(error);
    }

    @ResponseBody
    @ExceptionHandler(WrongUserInputException.class)
    ResponseEntity<Object> dateTimeParseExceptionHandler(WrongUserInputException ex) {
        ApiError error = new ApiError(HttpStatus.BAD_REQUEST, ex.getMessage());
        return buildResponseEntity(error);
    }

    private ResponseEntity<Object> buildResponseEntity(ApiError apiError){
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

}
