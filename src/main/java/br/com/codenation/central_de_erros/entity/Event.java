package br.com.codenation.central_de_erros.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long Id;

    @ManyToOne
    @NotNull
    private User user;

    @NotNull
    private ErrorTypes errorType;

    @NotNull
    @NotBlank
    @Size(max = 255)
    private String description;

    @NotNull
    @NotBlank
    @Size(max = 100)
    private String log;

    @NotNull
    @NotBlank
    @Size(max = 100)
    private String origin;

    @NotNull
    @NotBlank
    @Size(max = 100)
    private String dateTime;

    @NotNull
    private Long repeated;

    @CreatedDate
    protected String createdAt;
}
