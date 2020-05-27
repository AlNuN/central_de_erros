package br.com.codenation.central_de_erros.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum ErrorTypes {
    WARNING("W"), ERROR("E"), INFO("I");

    private String code;
}
