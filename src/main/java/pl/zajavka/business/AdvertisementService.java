package pl.zajavka.business;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.zajavka.domain.Advertisement;
import pl.zajavka.domain.User;
import pl.zajavka.infrastructure.database.entity.AdvertisementEntity;
import pl.zajavka.infrastructure.database.repository.AdvertisementRepository;
import pl.zajavka.infrastructure.database.repository.mapper.AdvertisementMapper;
import pl.zajavka.infrastructure.security.UserRepository;
import pl.zajavka.infrastructure.security.mapper.UserMapper;
import java.time.OffsetDateTime;
import java.util.List;


@AllArgsConstructor
@Service
public class AdvertisementService {

    private AdvertisementRepository advertisementRepository;
    private UserRepository userRepository;
    private UserMapper userMapper;
    private AdvertisementMapper advertisementMapper;
    private UserService userService;

    @Transactional
    public Advertisement create(Advertisement advertisement, User user) {
//        UserEntity userEntity = userRepository.findById(userId)
//                .orElseThrow(() -> new EntityNotFoundException("Brak użytkownika o userId: " + userId));
        System.out.println("twórz psie");
       OffsetDateTime currentDateTime = OffsetDateTime.now();
        // Stwórz nową reklamę
        AdvertisementEntity newAdvertisementEntity = AdvertisementEntity.builder()
                .name(advertisement.getName())
                .surname(advertisement.getSurname())
                .workExperience(advertisement.getWorkExperience())
                .knowledgeOfTechnologies(advertisement.getKnowledgeOfTechnologies())
                .dateTime(OffsetDateTime.from(currentDateTime))
                .user(userMapper.map(user))
                .build();

        advertisementRepository.saveAndFlush(newAdvertisementEntity);
        return    advertisementMapper.map(newAdvertisementEntity);



        // Zapisz użytkownika w bazie danych
//        userRepository.save(userEntity);
//       advertisementRepository.save(advertisementEntity);



    }

    public List<AdvertisementEntity> getAllAdvertisements() {

        return advertisementRepository.findAll();
    }
//    public List<AdvertisementEntity> searchAdvertisementsByKeyword(String keyword) {
//        // Wykorzystaj metody repozytorium Spring Data JPA do zapytań niestandardowych
//        return advertisementRepository.findAdvertisementsByKeyword(keyword);
//
//    }
//@Transactional(readOnly = true)
//public List<AdvertisementEntity> searchAdvertisementsByKeyword(String keyword) {
//    // Wykorzystaj metody repozytorium Spring Data JPA do zapytań niestandardowych
//    return advertisementRepository.findAdvertisementsByKeywordIgnoreCase("%" + keyword + "%");
//}
public List<AdvertisementEntity> searchAdvertisementsByKeywordAndCategory(String keyword, String category) {
    return advertisementRepository.findAdvertisementsByKeywordAndCategory(keyword, category);
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
