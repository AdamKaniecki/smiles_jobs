package pl.zajavka.domain;

import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.With;

@Data
@With
@Builder
@EqualsAndHashCode(of = "id")
public class Advertisement {

    private Integer id;
    private String name;
}
