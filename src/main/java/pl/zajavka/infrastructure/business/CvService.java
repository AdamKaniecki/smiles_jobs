package pl.zajavka.infrastructure.business;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.zajavka.controller.dto.CvDTO;
import pl.zajavka.controller.dto.mapper.CvMapperDTO;
import pl.zajavka.infrastructure.business.dao.CvDAO;
import pl.zajavka.infrastructure.business.dao.NotificationDAO;
import pl.zajavka.infrastructure.database.entity.CvEntity;
import pl.zajavka.infrastructure.database.entity.NotificationEntity;
import pl.zajavka.infrastructure.database.entity.Status;
import pl.zajavka.infrastructure.database.repository.jpa.CvJpaRepository;
import pl.zajavka.infrastructure.database.repository.mapper.AddressMapper;
import pl.zajavka.infrastructure.database.repository.mapper.CvMapper;
import pl.zajavka.infrastructure.domain.Address;
import pl.zajavka.infrastructure.domain.CV;
import pl.zajavka.infrastructure.domain.User;
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
    private final NotificationDAO notificationDAO;
    private final CvDAO cvDAO;
    private final CvMapperDTO cvMapperDTO;


    @Transactional
    public CV createCV(CV cv, User user) {

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

                .socialMediaProfil(cv.getSocialMediaProfil())
                .projects(cv.getProjects())
                .aboutMe(cv.getAboutMe())
                .certificatesOfCourses(cv.getCertificatesOfCourses())
                .programmingLanguage(cv.getProgrammingLanguage())
                .skillsAndTools(cv.getSkillsAndTools())
                .language(cv.getLanguage())
                .languageLevel(cv.getLanguageLevel())
                .hobby(cv.getHobby())
                .followPosition(cv.getFollowPosition())
                .visible(true)
                .user(userMapper.map(user))
                .address(addressMapper.map(addressCV))
                .build();


        cvDAO.saveCV(newEntity);
        return cvMapper.map(newEntity);
    }


    @Transactional
    public void updateCV(CV updatedCv) {
        if (updatedCv.getId() != null) {
            CV cv = cvDAO.findById(updatedCv.getId());
            CvEntity cvEntity = cvMapper.map(cv);

            cvEntity.setName(updatedCv.getName());
            cvEntity.setSurname(updatedCv.getSurname());
            cvEntity.setDateOfBirth(updatedCv.getDateOfBirth());
            cvEntity.setSex(updatedCv.getSex());
            cvEntity.setMaritalStatus(updatedCv.getMaritalStatus());
            cvEntity.setContactEmail(updatedCv.getContactEmail());
            cvEntity.setPhoneNumber(updatedCv.getPhoneNumber());
            cvEntity.setEducation(updatedCv.getEducation());
            cvEntity.setWorkExperience(updatedCv.getWorkExperience());
            cvEntity.setSocialMediaProfil(updatedCv.getSocialMediaProfil());
            cvEntity.setProjects(updatedCv.getProjects());
            cvEntity.setAboutMe(updatedCv.getAboutMe());
            cvEntity.setCertificatesOfCourses(updatedCv.getCertificatesOfCourses());
            cvEntity.setProgrammingLanguage(updatedCv.getProgrammingLanguage());
            cvEntity.setSkillsAndTools(updatedCv.getSkillsAndTools());
            cvEntity.setLanguage(updatedCv.getLanguage());
            cvEntity.setLanguageLevel(updatedCv.getLanguageLevel());
            cvEntity.setHobby(updatedCv.getHobby());
            cvEntity.setFollowPosition(updatedCv.getFollowPosition());

            cvDAO.saveCV(cvEntity);

        } else {
            throw new EntityNotFoundException("Not found CV entity with ID: " + updatedCv.getId());
        }
    }


    @Transactional
    public void deleteCVAndSetNullInNotifications(CV cv, Address address) {
        if (cv != null) {
            CvEntity cvEntity = cvMapper.map(cv);

            // Pobierz wszystkie powiązane notyfikacje z tym CV
            List<NotificationEntity> notifications = notificationDAO.findByCvId(cvEntity.getId());

            // Ustaw CV na null we wszystkich powiązanych notyfikacjach
            for (NotificationEntity notification : notifications) {
                notification.setCv(null);
                notification.setCompanyMessage("użytkownik usunął swoje CV");
                notification.setCandidateMessage("usunąłeś swoje CV");
                notification.setStatus(Status.REJECT);
            }

            // Usuń CV
            cvDAO.deleteById(cvEntity.getId());
        } else {
            throw new IllegalArgumentException("CV cannot be null");
        }
    }

    public boolean existByUser(User loggedInUser) {
        return cvDAO.existByUser(loggedInUser);
    }

    public CV findByUser(User loggedInUser) {
        return cvDAO.findByUser(loggedInUser);
    }

    public CV findById(Integer id) {
        return cvDAO.findById(id);
    }

    public List<CvDTO> searchCvByKeywordAndCategory(String keyword, String category) {
        return cvDAO.searchCvByKeywordAndCategory(keyword, category).stream()
                .map(cvMapperDTO::map)
                .toList();
    }
}
