package br.com.codenation.central_de_erros.entity;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter(autoApply = true)
public class ErrorTypeConverter implements AttributeConverter<ErrorTypes, String> {

    @Override
    public String convertToDatabaseColumn(ErrorTypes errorTypes) {
        if(errorTypes == null){ return null; }
        return errorTypes.getCode();
    }

    @Override
    public ErrorTypes convertToEntityAttribute(String s) {
        if(s == null) { return null; }

        return Stream.of(ErrorTypes.values())
                .filter(e -> e.getCode().equals(s))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
