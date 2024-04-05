package pl.zajavka.infrastructure.database.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.zajavka.infrastructure.database.entity.CvEntity;
import pl.zajavka.infrastructure.security.UserEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface CvJpaRepository extends JpaRepository<CvEntity, Integer> {

    @Query("SELECT a FROM CvEntity a WHERE a.visible = true AND (" +
            "(:category = 'followPosition' AND LOWER(a.followPosition) LIKE LOWER(CONCAT('%', :keyword, '%'))) OR " +
            "(:category = 'language' AND LOWER(a.language) LIKE LOWER(CONCAT('%', :keyword, '%'))) OR " +
            "(:category = 'education' AND LOWER(a.education) LIKE LOWER(CONCAT('%', :keyword, '%'))) OR " +
            "(:category = 'programmingLanguage' AND LOWER(a.programmingLanguage) LIKE LOWER(CONCAT('%', :keyword, '%'))) OR " +
            "(:category = 'languageLevel' AND LOWER(a.languageLevel) LIKE LOWER(CONCAT('%', :keyword, '%'))) OR " +
            "(:category = 'skillsAndTools' AND LOWER(a.skillsAndTools) LIKE LOWER(CONCAT('%', :keyword, '%'))))"
    )
    List<CvEntity> findCvByKeywordAndCategory(
            @Param("keyword") String keyword,
            @Param("category") String category);

    boolean existsByUserId(Integer userId);
    Optional<CvEntity> findByUser(UserEntity userEntity);
    boolean existsByUser(UserEntity map);

   Optional <CvEntity> findById(Integer id);


    List<CvEntity> findAll();


}
