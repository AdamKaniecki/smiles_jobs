package pl.zajavka.infrastructure.business;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.zajavka.infrastructure.domain.Address;
import pl.zajavka.infrastructure.domain.CV;
import pl.zajavka.infrastructure.domain.User;
import pl.zajavka.infrastructure.database.entity.*;
import pl.zajavka.infrastructure.database.repository.jpa.CvJpaRepository;
import pl.zajavka.infrastructure.database.repository.jpa.NotificationJpaRepository;
import pl.zajavka.infrastructure.database.repository.mapper.AddressMapper;
import pl.zajavka.infrastructure.database.repository.mapper.CvMapper;
import pl.zajavka.infrastructure.security.mapper.UserMapper;

import java.util.List;

@Slf4j

@Service
@AllArgsConstructor
public class CvService {

    private CvMapper cvMapper;
    private CvJpaRepository cvRepository;
    private UserMapper userMapper;
    private AddressMapper addressMapper;

    private NotificationJpaRepository notificationJpaRepository;


//    public List<CV> findAll() {
//        return cvRepository.findAll().stream()
//                .map(cvMapper::map)
//                .toList();
//    }

@Transactional
public CV createCV(CV cv, User user) {
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
            .contactEmail(userMapper.map(user).getEmail())
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
    newEntity.setProgrammingLanguages(cv.getProgrammingLanguages());


    cvRepository.saveAndFlush(newEntity);
    return cvMapper.map(newEntity);
}
//    newEntity.setIt_specializations(cv.getIt_specializations());
//    // Ustawienie wybranych języków programowania
//    Set<ProgrammingLanguage> programmingLanguages = new HashSet<>();
//    if (programmingLanguagesNames != null) {
//        for (String languageName : programmingLanguagesNames) {
//            ProgrammingLanguage language = ProgrammingLanguage.valueOf(languageName);
//            programmingLanguages.add(language);
//        }
//    }
//    newEntity.setProgrammingLanguages(programmingLanguages);
//



    @Transactional
    public void updateCV(CV updatedCv) {
        if (updatedCv.getId() != null) {
            // Sprawdź, czy CV istnieje w bazie danych
            CvEntity cvEntity = cvRepository.findById(updatedCv.getId())
                    .orElseThrow(() -> new EntityNotFoundException("CV with ID " + updatedCv.getId() + " not found"));

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
            throw new EntityNotFoundException("CV ID cannot be null");
        }
    }


//    @Transactional
//    public void deleteCV(CV cv) {
//
//        if (cv != null) {
//            CvEntity cvEntity = cvMapper.map(cv);
//            cvRepository.deleteById(cvEntity.getId());
//        } else {
//            throw new IllegalArgumentException("CV cannot be null");
//        }
//    }


    @Transactional
    public void deleteCVAndSetNullInNotifications(CV cv, Address address) {
        if (cv != null) {
            CvEntity cvEntity = cvMapper.map(cv);

            // Pobierz wszystkie powiązane notyfikacje z tym CV
            List<NotificationEntity> notifications = notificationJpaRepository.findByCvId(cvEntity.getId());

            // Ustaw CV na null we wszystkich powiązanych notyfikacjach
            for (NotificationEntity notification : notifications) {
                notification.setCv(null);
                notification.setCompanyMessage("użytkownik usunął swoje CV");
                notification.setCandidateMessage("usunąłeś swoje CV");
                notification.setStatus(Status.REJECT);
            }

            // Usuń CV
            cvRepository.deleteById(cvEntity.getId());
        } else {
            throw new IllegalArgumentException("CV cannot be null");
        }
    }


//    public Page<CV> findAll(Pageable pageable) {
//        Page<CvEntity> cvEntities = cvRepository.findAll(pageable);
//        return cvEntities.map(cvMapper::map);
//    }







//
//    public CV getCVById(Integer cvId) {
//        CvEntity cvEntity = cvRepository.findById(cvId).orElseThrow(()-> new EntityNotFoundException("Not found CV with ID: " + cvId));
//
//        return cvMapper.map(cvEntity);
//    }
//    public Optional<CV> findCvByUserId(Integer id) {
//        return cvRepository.findByUserId(id);
//    }

//    public boolean existByUser(User loggedInUser) {
//        return cvRepository.existsByUser(userMapper.map(loggedInUser));
//    }

//    public Optional<CV> findById(Integer id) {
//
//        return cvRepository.findById(id).map(cvMapper::map);
//    }

//    public Optional<CV> findByUser(User user) {
//        Optional<CvEntity> cvEntityOptional = cvRepository.findByUser(userMapper.map(user));
//        return cvEntityOptional.map(cvMapper::map);
//    }

//    public CV findByUser2(User user){
//        CvEntity cvEntity = cvRepository.findByUser(userMapper.map(user))
//                .orElseThrow(()-> new EntityNotFoundException("Not found CVEntity for user: " + user.getUserName()));
//        return cvMapper.map(cvEntity);
//    }



}
