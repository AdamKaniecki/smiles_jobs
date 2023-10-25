package pl.zajavka.api.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import pl.zajavka.api.dto.AdvertisementDTO;
import pl.zajavka.api.dto.UserDTO;
import pl.zajavka.domain.Advertisement;
import pl.zajavka.domain.User;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MapperDTO {
    UserDTO map (User user);
    User map(UserDTO userDTO);

    Advertisement map(AdvertisementDTO advertisementDTO);
    AdvertisementDTO map(Advertisement advertisement);
}
