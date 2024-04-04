package pl.zajavka.infrastructure.domain;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@With
@Builder
public class SearchRequest {

    String category;
    String keyword;
}
