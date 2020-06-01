package br.com.codenation.central_de_erros.advice;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
public class ApiError {
    private HttpStatus status;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime timeStamp;
    private String message;

    private ApiError() {
        timeStamp = LocalDateTime.now();
    }

    ApiError(HttpStatus status){
        this();
        this.status = status;
    }

    ApiError(HttpStatus status, String message) {
        this();
        this.status = status;
        this.message = message;
    }
}
