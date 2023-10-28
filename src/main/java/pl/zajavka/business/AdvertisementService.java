package pl.zajavka.business;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.zajavka.domain.Advertisement;
import pl.zajavka.domain.User;
import pl.zajavka.infrastructure.database.entity.AdvertisementEntity;
import pl.zajavka.infrastructure.database.repository.AdvertisementRepository;
import pl.zajavka.infrastructure.security.UserEntity;
import pl.zajavka.infrastructure.security.UserRepository;
import pl.zajavka.infrastructure.security.mapper.UserMapper;

import java.util.Optional;
import java.util.Set;

@AllArgsConstructor
@Service
public class AdvertisementService {

    private AdvertisementRepository advertisementRepository;
    private UserRepository userRepository;
    private UserMapper userMapper;
    private UserService userService;

    @Transactional
    public AdvertisementEntity create(Advertisement advertisement) {
//        UserEntity userEntity = userRepository.findById(userId)
//                .orElseThrow(() -> new EntityNotFoundException("Brak użytkownika o userId: " + userId));

        // Stwórz nową reklamę
        AdvertisementEntity advertisementEntity = AdvertisementEntity.builder()
                .name(advertisement.getName())
                .build();

        return advertisementRepository.save(advertisementEntity);


        // Zapisz użytkownika w bazie danych
//        userRepository.save(userEntity);
//       advertisementRepository.save(advertisementEntity);



    }


//
//    public Advertisement createAdvertisement(Integer userId, Advertisement advertisement) {
//       UserEntity user = userRepository.findById(userId)
//////                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));
//
//        Advertisement advertisement = Advertisement.builder()
//                .name()
//                .build()
//        advertisement.setName(advertisement.getName());
//
//        // Ustaw pozostałe właściwości reklamy z obiektu DTO
//
//        // Dodaj reklamę do użytkownika
//        user.getAdvertisements().add(advertisement);
//        userRepository.save(user);
//
//        // Zapisz reklamę w bazie danych
//        return advertisementRepository.save(advertisement);
//    }

}
