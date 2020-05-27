package br.com.codenation.central_de_erros.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.hateoas.Identifiable;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name="events")
public class Event implements Identifiable<Long>, Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    private ErrorTypes errorType;

    @NotNull
    @NotBlank
    @Size(max = 255)
    private String description;

    @NotNull
    @NotBlank
    @Size(max = 255)
    private String log;

    @NotNull
    @NotBlank
    @Size(max = 100)
    private String origin;

    @Column
    private LocalDateTime dateTime;

    @NotNull
    private Long repeated;

    @Column
    @CreatedDate
    @JsonIgnore
    protected LocalDateTime createdAt;
}
