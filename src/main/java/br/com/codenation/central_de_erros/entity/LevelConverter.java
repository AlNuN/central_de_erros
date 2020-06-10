package br.com.codenation.central_de_erros.entity;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter(autoApply = true)
public class LevelConverter implements AttributeConverter<Level, String> {

    @Override
    public String convertToDatabaseColumn(Level level) {
        if(level == null){ return null; }
        return level.getCode();
    }

    @Override
    public Level convertToEntityAttribute(String s) {
        if(s == null) { return null; }

        return Stream.of(Level.values())
                .filter(e -> e.getCode().equals(s))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
