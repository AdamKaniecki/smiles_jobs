package pl.zajavka.business;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;
import pl.zajavka.domain.Address;
import pl.zajavka.domain.CV;
import pl.zajavka.domain.JobOffer;
import pl.zajavka.domain.User;
import pl.zajavka.infrastructure.database.entity.*;
import pl.zajavka.infrastructure.database.repository.AddressRepository;
import pl.zajavka.infrastructure.database.repository.CvRepository;
import pl.zajavka.infrastructure.database.repository.JobOfferRepository;
import pl.zajavka.infrastructure.database.repository.NotificationRepository;
import pl.zajavka.infrastructure.database.repository.mapper.AddressMapper;
import pl.zajavka.infrastructure.database.repository.mapper.CvMapper;
import pl.zajavka.infrastructure.database.repository.mapper.JobOfferMapper;
import pl.zajavka.infrastructure.security.UserEntity;
import pl.zajavka.infrastructure.security.UserRepository;
import pl.zajavka.infrastructure.security.mapper.UserMapper;

import java.util.List;
import java.util.Optional;

@Slf4j

@Service
@AllArgsConstructor
public class CvService {

    private CvMapper cvMapper;
    private CvRepository cvRepository;
    private UserMapper userMapper;
    private AddressMapper addressMapper;
    private UserRepository userRepository;
    private final JobOfferRepository jobOfferRepository;
    private final JobOfferMapper jobOfferMapper;
    private AddressRepository addressRepository;
    private AddressService addressService;
    private JobOfferService jobOfferService;
    private NotificationRepository notificationRepository;


    @Transactional
    public CV createCV(CV cv, User user) {
        // Sprawdź, czy użytkownik już ma CV
        if (cvRepository.existsByUser(userMapper.map(user))) {
            return null;
        }
        Address addressCV = cv.getAddress();

        CvEntity newEntity = CvEntity.builder()
                .id(cv.getId())
                .name(cv.getName())
                .surname(cv.getSurname())
                .dateOfBirth(cv.getDateOfBirth())
                .sex(cv.getSex())
                .maritalStatus(cv.getMaritalStatus())
                .contactEmail(cv.getContactEmail())
                .phoneNumber(cv.getPhoneNumber())
                .education(cv.getEducation())
                .workExperience(cv.getWorkExperience())
                .skills(cv.getSkills())
                .language(cv.getLanguage())
                .languageLevel(cv.getLanguageLevel())
                .hobby(cv.getHobby())
                .user(userMapper.map(user))
                .address(addressMapper.map(addressCV))
                .build();

        cvRepository.saveAndFlush(newEntity);
        return cvMapper.map(newEntity);
    }


    public List<CV> findAll() {
        return cvRepository.findAll().stream()
                .map(cvMapper::map)
                .toList();
    }

    public List<CV> searchCvByKeywordAndCategory(String keyword, String category) {
        List<CvEntity> searchResultEntities = cvRepository.findCvByKeywordAndCategory(keyword, category);
        return searchResultEntities.stream()
                .map(cvMapper::map)
                .toList();
    }

    public CV getCVById(Integer cvId) {
        CvEntity cvEntity = cvRepository.findById(cvId).orElseThrow(()-> new NotFoundException("Not found CV with ID: " + cvId));

        return cvMapper.map(cvEntity);
    }
//    public Optional<CV> findCvByUserId(Integer id) {
//        return cvRepository.findByUserId(id);
//    }

    public boolean existByUser(User loggedInUser) {
        return cvRepository.existsByUser(userMapper.map(loggedInUser));
    }

    public Optional<CV> findById(Integer id) {
        ;
        return cvRepository.findById(id).map(cvMapper::map);
    }

    public Optional<CV> findByUser(User user) {
        Optional<CvEntity> cvEntityOptional = cvRepository.findByUser(userMapper.map(user));
        return cvEntityOptional.map(cvMapper::map);
    }

    public CV findByUser2(User user){
        CvEntity cvEntity = cvRepository.findByUser(userMapper.map(user))
                .orElseThrow(()-> new NotFoundException("Not found CV from user: " + user));
        return cvMapper.map(cvEntity);
    }


    @Transactional
    public void updateCV(CV updatedCv) {
        if (updatedCv.getId() != null) {
            // Sprawdź, czy CV istnieje w bazie danych
            CvEntity cvEntity = cvRepository.findById(updatedCv.getId())
                    .orElseThrow(() -> new NotFoundException("CV with ID " + updatedCv.getId() + " not found"));

            // Aktualizuj pola CV na podstawie danych z formularza
            cvEntity.setName(updatedCv.getName());
            cvEntity.setSurname(updatedCv.getSurname());
            cvEntity.setDateOfBirth(updatedCv.getDateOfBirth());
            cvEntity.setSex(updatedCv.getSex());
            cvEntity.setMaritalStatus(updatedCv.getMaritalStatus());
            cvEntity.setContactEmail(updatedCv.getContactEmail());
            cvEntity.setPhoneNumber(updatedCv.getPhoneNumber());
            cvEntity.setEducation(updatedCv.getEducation());
            cvEntity.setWorkExperience(updatedCv.getWorkExperience());
            cvEntity.setSkills(updatedCv.getSkills());
            cvEntity.setLanguage(updatedCv.getLanguage());
            cvEntity.setLanguageLevel(updatedCv.getLanguageLevel());
            cvEntity.setHobby(updatedCv.getHobby());

            // Zapisz zaktualizowany obiekt CV w bazie danych
            CvEntity cvEntityUpdate = cvRepository.save(cvEntity);

//            return cvMapper.map(cvEntityUpdate);
        } else {
            // Obsłuż sytuację, gdy CV nie zostało znalezione w bazie danych
            throw new NotFoundException("CV ID cannot be null");
        }
    }


    @Transactional
    public void deleteCV(CV cv) {
        System.out.println("Deleting CV: " + cv);

        if (cv != null) {
            CvEntity cvEntity = cvMapper.map(cv);
            System.out.println("Mapped CV to CVEntity: " + cvEntity);
            log.debug("co tu sie odjaniepawla: ",cvEntity);
            cvRepository.deleteById(cvEntity.getId());
            log.debug("co tu sie odjaniepawla2: ",cvEntity.getId());
            System.out.println("Deleted CV with ID: " + cvEntity.getId());
        } else {
            throw new IllegalArgumentException("CV cannot be null");
        }
    }


//    // Przyjmowanie identyfikatora użytkownika, do którego ma być wysłane CV
//    public void sendCv(User sender, Integer recipientUserId) {
//        // Pobierz CV użytkownika wysyłającego
//        CV cvToSend = sender.getCv();
//
//        // Pobierz użytkownika odbierającego
//        UserEntity recipientUserEntity = userRepository.findById(recipientUserId)
//                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono użytkownika o id: " + recipientUserId));
//        userRepository.save(recipientUserEntity);
//        User recipientUser = userMapper.map(recipientUserEntity);
//        // Przypisz CV do użytkownika odbierającego
//        recipientUser.setCv(cvToSend);
//
//        // Poniżej to jest przykładowe logowanie do konsoli
//        System.out.println("CV wysłane od " + sender.getUserName() + " do " + recipientUser.getUserName());
//    }

    @Transactional
    public void deleteCVAndSetNullInNotifications(CV cv, Address address) {
        if (cv != null) {
            CvEntity cvEntity = cvMapper.map(cv);

            // Pobierz wszystkie powiązane notyfikacje z tym CV
            List<NotificationEntity> notifications = notificationRepository.findByCvId(cvEntity.getId());

            // Ustaw CV na null we wszystkich powiązanych notyfikacjach
            for (NotificationEntity notification : notifications) {
                notification.setCv(null);
                notification.setCompanyMessage("użytkownik usunął swoje CV");
                notification.setStatus(Status.REJECT);
            }

            // Usuń CV
            cvRepository.deleteById(cvEntity.getId());
        } else {
            throw new IllegalArgumentException("CV cannot be null");
        }
    }


}
