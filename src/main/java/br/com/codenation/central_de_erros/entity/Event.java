package br.com.codenation.central_de_erros.entity;

import br.com.codenation.central_de_erros.validationGroups.OnCreate;
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
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(groups = OnCreate.class)
    private Level level;

    @NotNull(groups = OnCreate.class)
    @NotBlank(groups = OnCreate.class)
    @Size(max = 255)
    private String description;

    @NotNull(groups = OnCreate.class)
    @NotBlank(groups = OnCreate.class)
    @Size(max = 255)
    private String log;

    @NotNull(groups = OnCreate.class)
    @NotBlank(groups = OnCreate.class)
    @Size(max = 100)
    private String origin;

    @Column
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @ApiModelProperty(dataType = "java.lang.String", example = "2020-05-26 13:45")
    private LocalDateTime dateTime;

    @NotNull(groups = OnCreate.class)
    @PositiveOrZero
    private Long number;

    @Column
    @CreatedDate
    @JsonIgnore
    protected LocalDateTime createdAt;
}
