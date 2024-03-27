package pl.zajavka.infrastructure.database.repository;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.zajavka.infrastructure.business.dao.CvDAO;
import pl.zajavka.infrastructure.database.entity.CvEntity;
import pl.zajavka.infrastructure.database.repository.jpa.CvJpaRepository;
import pl.zajavka.infrastructure.database.repository.mapper.CvMapper;
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


    @Override
    public List<CV> findAll() {
        return cvJpaRepository.findAll().stream()
                .map(cvMapper::map)
                .toList();
    }

    public boolean existByUser(User loggedInUser) {
        return cvJpaRepository.existsByUser(userMapper.map(loggedInUser));
    }
    public Optional<CV> findById(Integer id) {

        return cvJpaRepository.findById(id).map(cvMapper::map);
    }

    public Optional<CV> findByUser(User user) {
        Optional<CvEntity> cvEntityOptional = cvJpaRepository.findByUser(userMapper.map(user));
        return cvEntityOptional.map(cvMapper::map);
    }

    public CV findByUser2(User user){

        CvEntity cvEntity = cvJpaRepository.findByUser(userMapper.map(user))
                .orElseThrow(()-> new EntityNotFoundException("Not found CVEntity for user: " + user.getUserName()));
        return cvMapper.map(cvEntity);
    }



    public List<CV> searchCvByKeywordAndCategory(String keyword, String category) {
        List<CvEntity> searchResultEntities = cvJpaRepository.findCvByKeywordAndCategory(keyword, category);
        return searchResultEntities.stream()
                .map(cvMapper::map)
                .toList();
    }


//
//    public CV getCVById(Integer cvId) {
//        CvEntity cvEntity = cvRepository.findById(cvId).orElseThrow(()-> new EntityNotFoundException("Not found CV with ID: " + cvId));
//
//        return cvMapper.map(cvEntity);
//    }
//    public Optional<CV> findCvByUserId(Integer id) {
//        return cvRepository.findByUserId(id);
//    }
//
//    public boolean existByUser(User loggedInUser) {
//        return cvRepository.existsByUser(userMapper.map(loggedInUser));
//    }
//
//    public Optional<CV> findById(Integer id) {
//
//        return cvRepository.findById(id).map(cvMapper::map);
//    }
//
//    public Optional<CV> findByUser(User user) {
//        Optional<CvEntity> cvEntityOptional = cvRepository.findByUser(userMapper.map(user));
//        return cvEntityOptional.map(cvMapper::map);
//    }
//
//    public CV findByUser2(User user){
//        CvEntity cvEntity = cvRepository.findByUser(userMapper.map(user))
//                .orElseThrow(()-> new EntityNotFoundException("Not found CVEntity for user: " + user.getUserName()));
//        return cvMapper.map(cvEntity);
//    }

}
