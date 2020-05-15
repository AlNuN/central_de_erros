package br.com.codenation.central_de_erros.service.interfaces;

public interface ServiceInterface<T> {
    T save(T object);
}
