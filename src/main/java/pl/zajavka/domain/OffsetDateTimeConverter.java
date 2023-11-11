package pl.zajavka.domain;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class OffsetDateTimeConverter implements Converter<String,OffsetDateTime> {

    public OffsetDateTime convert(String source) {
        try {
            return OffsetDateTime.parse(source, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        } catch (Exception e) {
            // Jeśli nie można dokonać konwersji, obsłuż wyjątek lub zwróć wartość domyślną
            return null;
        }
    }
}
