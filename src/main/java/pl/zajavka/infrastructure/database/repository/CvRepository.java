package pl.zajavka.infrastructure.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.zajavka.infrastructure.database.entity.CvEntity;

import java.util.List;

@Repository
public interface CvRepository extends JpaRepository<CvEntity, Integer> {

    @Query("SELECT a FROM CvEntity a WHERE " +
            "(:category = 'name' AND LOWER(a.name) LIKE LOWER(CONCAT('%', :keyword, '%'))) OR " +
            "(:category = 'surname' AND LOWER(a.surname) LIKE LOWER(CONCAT('%', :keyword, '%'))) OR " +
            "(:category = 'workExperience' AND LOWER(a.workExperience) LIKE LOWER(CONCAT('%', :keyword, '%'))) OR " +
            "(:category = 'skills' AND LOWER(a.skills) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<CvEntity> findCvByKeywordAndCategory(
            @Param("keyword") String keyword,
            @Param("category") String category);
}
