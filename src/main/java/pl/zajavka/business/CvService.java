package pl.zajavka.business;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.zajavka.domain.Address;
import pl.zajavka.domain.CV;
import pl.zajavka.domain.User;
import pl.zajavka.infrastructure.database.entity.CvEntity;
import pl.zajavka.infrastructure.database.repository.CvRepository;
import pl.zajavka.infrastructure.database.repository.mapper.AddressMapper;
import pl.zajavka.infrastructure.database.repository.mapper.CvMapper;
import pl.zajavka.infrastructure.security.mapper.UserMapper;

import java.util.List;

@Service
@AllArgsConstructor
public class CvService {

    private CvMapper cvMapper;
    private CvRepository cvRepository;
    private UserMapper userMapper;
    private AddressMapper addressMapper;


    @Transactional
    public CV createCV(CV cv, User user) {
        Address addressCV = cv.getAddress();

        CvEntity newEntity = CvEntity.builder()
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
    return     cvRepository.findAll().stream()
                .map(cvMapper::map)
                .toList();
}

    public List<CV> searchCvByKeywordAndCategory(String keyword, String category) {
      List<CvEntity> searchResultEntities =  cvRepository.findCvByKeywordAndCategory(keyword, category);
        return searchResultEntities.stream()
                .map(cvMapper::map)
                .toList();
    }
}

