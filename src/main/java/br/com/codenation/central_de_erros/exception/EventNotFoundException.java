package br.com.codenation.central_de_erros.exception;

public class EventNotFoundException extends RuntimeException{
    public EventNotFoundException(Long id) {
        super("Could not find Event " + id);
    }
}
