package pl.zajavka.infrastructure.business.dao;

import pl.zajavka.infrastructure.database.entity.CvEntity;
import pl.zajavka.infrastructure.domain.CV;
import pl.zajavka.infrastructure.domain.User;

import java.util.List;
import java.util.Optional;

public interface CvDAO {
    List<CV> findAll();
    boolean existByUser(User loggedInUser);
    CV findById(Integer id);

    CV findByUser(User user);

    void deleteById(Integer id);

    List<CV>searchCvByKeywordAndCategory(String keyword, String category);

    void saveCV(CvEntity newEntity);


}
