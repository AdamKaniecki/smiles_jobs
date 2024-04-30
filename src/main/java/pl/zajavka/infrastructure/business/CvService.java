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
import pl.zajavka.infrastructure.domain.Notification;
import pl.zajavka.infrastructure.domain.User;
import pl.zajavka.infrastructure.security.mapper.UserMapper;

import java.util.List;
import java.util.Optional;

@Slf4j

@Service
@AllArgsConstructor
public class CvService {

    private CvMapper cvMapper;
    private CvJpaRepository cvRepository;
    private UserMapper userMapper;
    private final NotificationDAO notificationDAO;
    private final CvDAO cvDAO;
    private final CvMapperDTO cvMapperDTO;


    @Transactional
    public CV createCV(CV cv, User user) {
       return cvDAO.createCV(cv, user);

    }


    @Transactional
    public void updateCV(CV updatedCv) {
        if (updatedCv != null) {
          cvDAO.updateCV(updatedCv);
        } else {
            throw new EntityNotFoundException("Not found CV entity with ID: " + updatedCv.getId());
        }
    }


    @Transactional
    public void deleteCVAndSetNullInNotifications(Integer cvId) {
            CV cv = cvDAO.findById(cvId);
            notificationDAO.findByCvIdToDelete(cv.getId());
            cvDAO.deleteById(cvId);
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

    public Optional<CV> findByUserOpt(User user) {
      return cvDAO.findByUserOpt(user);
    }

//    public CV findByUser(User user){
//      return   cvDAO.findByUser(user);
////        CvEntity cvEntity = cvRepository.findByUser(userMapper.map(user))
////                .orElseThrow(()-> new EntityNotFoundException("Not found CV for user: " + user.getUserName()));
////        return cvMapper.map(cvEntity);
//    }

    public void saveCV(CV cv){
        cvDAO.saveCV(cv);
    }

}
