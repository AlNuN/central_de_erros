package br.com.codenation.central_de_erros.exception;

public class WrongUserInputException extends RuntimeException{
    public WrongUserInputException(String input){
        super(input);
    }
}
