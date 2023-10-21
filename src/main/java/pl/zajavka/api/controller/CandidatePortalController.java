package pl.zajavka.api.controller;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import pl.zajavka.api.dto.UserDTO;
import pl.zajavka.domain.Advertisement;
import pl.zajavka.infrastructure.database.entity.AdvertisementEntity;
import pl.zajavka.infrastructure.security.UserEntity;
import pl.zajavka.infrastructure.security.UserRepository;

@AllArgsConstructor
@Controller
public class CandidatePortalController {
    public static final String CANDIDATE_PORTAL = "/candidate_portal";
    public static final String CREATE_ADVERTISEMENT = "/create_advertisement";

    public static final String USER_ID = "/{userId}";



    private UserRepository userRepository;

    @GetMapping(CANDIDATE_PORTAL)
    public String getCandidatePortalPage() {
        return "candidate_portal";
    }
    @GetMapping(CREATE_ADVERTISEMENT)
    public String getCreateAdvertisement(UserDTO userDTO) {
        return "create_advertisement";
    }




    @PostMapping("/createAdvertisement")
    public String createAdvertisement(@PathVariable Integer userId, @ModelAttribute Advertisement advertisement) {
        // Pobierz użytkownika na podstawie userId
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Brak użytkownika o userId: " + userId));

        // Utwórz reklamę na podstawie danych przekazanych z formularza
        AdvertisementEntity advertisementEntity = AdvertisementEntity.builder()
                .name(advertisement.getName())
                .user(userEntity)
                .build();

        // Dodaj reklamę do użytkownika
        userEntity.getAdvertisements().add(advertisementEntity);

        // Zapisz użytkownika w bazie danych
        userRepository.save(userEntity);

        return  "user_created_successfully";
    }
    }






