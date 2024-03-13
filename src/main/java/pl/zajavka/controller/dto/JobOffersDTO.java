package pl.zajavka.controller.dto;

import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
@With
public class JobOffersDTO {
    private List<JobOfferDTO> jobOffersDTO;
}
