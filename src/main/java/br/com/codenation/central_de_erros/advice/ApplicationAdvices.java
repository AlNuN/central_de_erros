package br.com.codenation.central_de_erros.advice;

import br.com.codenation.central_de_erros.exception.EventNotFoundException;
import br.com.codenation.central_de_erros.exception.WrongUserInputException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ApplicationAdvices {

    @ResponseBody
    @ExceptionHandler(EventNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String eventNotFoundHandler(EventNotFoundException ex){
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(WrongUserInputException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String dateTimeParseExceptionHandler(WrongUserInputException ex) {
        return ex.getMessage();
    }

}
