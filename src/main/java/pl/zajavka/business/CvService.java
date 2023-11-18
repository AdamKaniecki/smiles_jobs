package pl.zajavka.business;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.zajavka.domain.Address;
import pl.zajavka.domain.Advertisement;
import pl.zajavka.domain.CV;
import pl.zajavka.domain.User;
import pl.zajavka.infrastructure.database.entity.CvEntity;
import pl.zajavka.infrastructure.database.repository.CvRepository;
import pl.zajavka.infrastructure.database.repository.mapper.AddressMapper;
import pl.zajavka.infrastructure.database.repository.mapper.AdvertisementMapper;
import pl.zajavka.infrastructure.database.repository.mapper.CvMapper;
import pl.zajavka.infrastructure.security.mapper.UserMapper;

@Service
@AllArgsConstructor
public class CvService {

    private CvMapper cvMapper;
    private CvRepository cvRepository;
    private UserMapper userMapper;
    private AdvertisementMapper advertisementMapper;
    private AddressMapper addressMapper;

//    @Transactional
//    public CV createCV(
//            CV cv, Advertisement advertisement, Address address, User user) {
//        Address addressCV = cv.getAddress();
//        Advertisement advertisementCV = cv.getAdvertisement();
//
//        CvEntity newEntity = CvEntity.builder()
//                .dateOfBirth(cv.getDateOfBirth())
//                .sex(cv.getSex())
//                .maritalStatus(cv.getMaritalStatus())
//                .phoneNumber(cv.getPhoneNumber())
//                .education(cv.getEducation())
//                .skills(cv.getSkills())
//                .language(cv.getLanguage())
//                .languageLevel(cv.getLanguageLevel())
//                .hobby(cv.getHobby())
//                .user(userMapper.map(user))
//                .advertisement(advertisementMapper.map(advertisementCV).)
//                .name(name)
////                .surname(surname)
////                .workExperience(workExperience)
//                .address(addressMapper.map(addressCV))
//                .build();
//
//        cvRepository.saveAndFlush(newEntity);
//    return cvMapper.map(newEntity);



    @Transactional
    public CV createCV(CV cv, User user) {
        Address addressCV = cv.getAddress();
//        Advertisement advertisementCV = cv.getAdvertisement();

        CvEntity newEntity = CvEntity.builder()
                .cvName(cv.getCvName())
                .cvSurname(cv.getCvSurname())
                .dateOfBirth(cv.getDateOfBirth())
                .sex(cv.getSex())
                .maritalStatus(cv.getMaritalStatus())
                .contactEmail(cv.getContactEmail())
                .phoneNumber(cv.getPhoneNumber())
                .education(cv.getEducation())
                .cvWorkExperience(cv.getCvWorkExperience())
                .skills(cv.getSkills())
                .language(cv.getLanguage())
                .languageLevel(cv.getLanguageLevel())
                .hobby(cv.getHobby())
                .user(userMapper.map(user))
//                .advertisement(advertisementMapper.map(advertisementCV))
//                .name(advertisementCV.getName())  // Pobierz nazwę z obiektu Advertisement
//                .surname(advertisementCV.getSurname())  // Pobierz nazwisko z obiektu Advertisement
//                .workExperience(advertisementCV.getWorkExperience())  // Pobierz doświadczenie z obiektu Advertisement
                .address(addressMapper.map(addressCV))
                .build();

        cvRepository.saveAndFlush(newEntity);
        return cvMapper.map(newEntity);
    }
}

