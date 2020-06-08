package br.com.codenation.central_de_erros.assembler;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SuccessJSON {
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime timeStamp;
    private String message;

    private SuccessJSON() {
        timeStamp = LocalDateTime.now();
    }

    public SuccessJSON(String message) {
        this();
        this.message = message;
    }
}
