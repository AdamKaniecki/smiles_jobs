package pl.zajavka.infrastructure.database.repository;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.zajavka.infrastructure.business.dao.CvDAO;
import pl.zajavka.infrastructure.database.entity.CvEntity;
import pl.zajavka.infrastructure.database.repository.jpa.CvJpaRepository;
import pl.zajavka.infrastructure.database.repository.mapper.AddressMapper;
import pl.zajavka.infrastructure.database.repository.mapper.CvMapper;
import pl.zajavka.infrastructure.domain.Address;
import pl.zajavka.infrastructure.domain.CV;
import pl.zajavka.infrastructure.domain.User;
import pl.zajavka.infrastructure.security.mapper.UserMapper;

import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class CvRepository implements CvDAO {

    private final CvJpaRepository cvJpaRepository;
    private final CvMapper cvMapper;
    private final UserMapper userMapper;
    private final AddressMapper addressMapper;


    @Override
    public List<CV> findAll() {
        return cvJpaRepository.findAll().stream()
                .map(cvMapper::map)
                .toList();
    }

    @Override
    public boolean existByUser(User loggedInUser) {
        return cvJpaRepository.existsByUser(userMapper.map(loggedInUser));
    }

    @Override
    public CV findById(Integer id) {
        Optional<CvEntity> cvEntityOptional = cvJpaRepository.findById(id);
        CvEntity cvEntity = cvEntityOptional.orElseThrow(() -> new EntityNotFoundException("CV not found"));
        return cvMapper.map(cvEntity);
    }

//    @Override
//    public CV findByUser(User user) {
////        Optional<CvEntity> cvEntityOptional = cvJpaRepository.findByUser(userMapper.map(user));
////        CvEntity cvEntity = cvEntityOptional.orElseThrow(() -> new EntityNotFoundException("CV not found for the user"));
////        return cvMapper.map(cvEntity);
//        CvEntity cvEntity = cvJpaRepository.findByUser(userMapper.map(user))
//                .orElseThrow(()-> new EntityNotFoundException("Not found CV for user: " + user.getUserName()));
//        return cvMapper.map(cvEntity);
//    }


    @Override
    public List<CV> searchCvByKeywordAndCategory(String keyword, String category) {
        List<CvEntity> searchResultEntities = cvJpaRepository.findCvByKeywordAndCategory(keyword, category);
        return searchResultEntities.stream()
                .map(cvMapper::map)
                .toList();
    }

    @Override
    public void saveCV(CV cv) {
        CvEntity cvEntity = cvMapper.map(cv);
        cvJpaRepository.save(cvEntity);
    }

    @Override
    public void deleteById(Integer id) {
        cvJpaRepository.deleteById(id);
    }


    @Override
    public void updateCV(CV updateCV) {

        CvEntity cvEntity = cvMapper.map(updateCV);

        cvEntity.setName(updateCV.getName());
        cvEntity.setSurname(updateCV.getSurname());
        cvEntity.setDateOfBirth(updateCV.getDateOfBirth());
        cvEntity.setSex(updateCV.getSex());
        cvEntity.setMaritalStatus(updateCV.getMaritalStatus());
        cvEntity.setContactEmail(updateCV.getContactEmail());
        cvEntity.setPhoneNumber(updateCV.getPhoneNumber());
        cvEntity.setEducation(updateCV.getEducation());
        cvEntity.setWorkExperience(updateCV.getWorkExperience());
        cvEntity.setSocialMediaProfil(updateCV.getSocialMediaProfil());
        cvEntity.setProjects(updateCV.getProjects());
        cvEntity.setAboutMe(updateCV.getAboutMe());
        cvEntity.setCertificatesOfCourses(updateCV.getCertificatesOfCourses());
        cvEntity.setProgrammingLanguage(updateCV.getProgrammingLanguage());
        cvEntity.setSkillsAndTools(updateCV.getSkillsAndTools());
        cvEntity.setLanguage(updateCV.getLanguage());
        cvEntity.setLanguageLevel(updateCV.getLanguageLevel());
        cvEntity.setHobby(updateCV.getHobby());
        cvEntity.setFollowPosition(updateCV.getFollowPosition());

        cvJpaRepository.save(cvEntity);
    }

    @Override
    public CV createCV(CV cv, User user) {
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

        cvJpaRepository.save(newEntity);

        return cvMapper.map(newEntity);

    }

    @Override
    public Optional<CV> findByUserOpt(User user) {
        Optional<CvEntity> cvEntityOptional = cvJpaRepository.findByUser(userMapper.map(user));
        return cvEntityOptional.map(cvMapper::map);
    }

    @Override
    public CV findByUser(User user) {
       Optional <CvEntity> cvEntityOptional = cvJpaRepository.findByUser(userMapper.map(user));
        return cvEntityOptional.map(cvMapper::map).orElse(null);

    }
}
