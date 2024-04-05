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
    public CV findById(Integer id) {
      Optional<CvEntity> cvEntityOptional = cvJpaRepository.findById(id);
      CvEntity cvEntity = cvEntityOptional.orElseThrow(() -> new EntityNotFoundException("CV not found"));
        return cvMapper.map(cvEntity);
    }

    public CV findByUser(User user) {
        Optional<CvEntity> cvEntityOptional = cvJpaRepository.findByUser(userMapper.map(user));
        CvEntity cvEntity = cvEntityOptional.orElseThrow(() -> new EntityNotFoundException("CV not found for the user"));
        return cvMapper.map(cvEntity);
    }




    public List<CV> searchCvByKeywordAndCategory(String keyword, String category) {
        List<CvEntity> searchResultEntities = cvJpaRepository.findCvByKeywordAndCategory(keyword, category);
        return searchResultEntities.stream()
                .map(cvMapper::map)
                .toList();
    }

    public void saveCV(CV cv) {
        CvEntity cvEntity = cvMapper.map(cv);
        cvJpaRepository.save(cvEntity);
    }

    @Override
    public void deleteById(Integer id) {
        cvJpaRepository.deleteById(id);
    }

    @Override
    public void saveCV(CvEntity newEntity) {
        cvJpaRepository.save(newEntity);
    }


}
