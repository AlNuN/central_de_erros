package br.com.codenation.central_de_erros.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.hateoas.Identifiable;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDateTime;

//@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "event_sequence")
//@SequenceGenerator(name = "event_sequence")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name="events")
public class Event implements Identifiable<Long>, Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Level level;

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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @ApiModelProperty(dataType = "java.lang.String", example = "2020-05-26 13:45")
    private LocalDateTime dateTime;

    @NotNull
    @PositiveOrZero
    private Long number;

    @Column
    @CreatedDate
    @JsonIgnore
    protected LocalDateTime createdAt;
}
