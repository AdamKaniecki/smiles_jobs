package pl.zajavka.infrastructure.business.dao;

import pl.zajavka.infrastructure.domain.CV;
import pl.zajavka.infrastructure.domain.User;

import java.util.List;
import java.util.Optional;

public interface CvDAO {
    List<CV> findAll();
    boolean existByUser(User loggedInUser);
    Optional<CV> findById(Integer id);

    Optional<CV> findByUser(User user);
    CV findByUser2(User user);

}
