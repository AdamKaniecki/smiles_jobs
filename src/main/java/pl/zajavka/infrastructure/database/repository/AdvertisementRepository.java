package pl.zajavka.infrastructure.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.zajavka.domain.Advertisement;
import pl.zajavka.infrastructure.database.entity.AdvertisementEntity;

import java.util.List;

@Repository
public interface  AdvertisementRepository extends JpaRepository<AdvertisementEntity,Integer> {

    // Metoda do wyszukiwania ogłoszeń zawierających określone słowo kluczowe w jednym z pól
//    @Query("SELECT a FROM Advertisement a " +
//            "WHERE a.name LIKE %:keyword% OR " +
//            "a.surname LIKE %:keyword% OR " +
//            "a.workExperience LIKE %:keyword% OR " +
//            "a.knowledgeOfTechnologies LIKE %:keyword%")
//    @Query("SELECT a FROM AdvertisementEntity a WHERE LOWER(a.name) LIKE LOWER(:keyword) " +
//            "OR LOWER(a.surname) LIKE LOWER(:keyword) " +
//            "OR LOWER(a.workExperience) LIKE LOWER(:keyword) " +
//            "OR LOWER(a.knowledgeOfTechnologies) LIKE LOWER(:keyword)")
//    List<AdvertisementEntity> findAdvertisementsByKeyword(@Param("keyword") String keyword);

//    @Query("SELECT a FROM AdvertisementEntity a WHERE LOWER(a.name) LIKE LOWER(:keyword) " +
//            "OR LOWER(a.surname) LIKE LOWER(:keyword) " +
//            "OR LOWER(a.workExperience) LIKE LOWER(:keyword) " +
//            "OR LOWER(a.knowledgeOfTechnologies) LIKE LOWER(:keyword)")
//    List<AdvertisementEntity> findAdvertisementsByKeywordIgnoreCase(@Param("keyword") String keyword);

    @Query("SELECT a FROM AdvertisementEntity a WHERE " +
            "(:category = 'name' AND LOWER(a.name) LIKE LOWER(CONCAT('%', :keyword, '%'))) OR " +
            "(:category = 'surname' AND LOWER(a.surname) LIKE LOWER(CONCAT('%', :keyword, '%'))) OR " +
            "(:category = 'workExperience' AND LOWER(a.workExperience) LIKE LOWER(CONCAT('%', :keyword, '%'))) OR " +
            "(:category = 'knowledgeOfTechnologies' AND LOWER(a.knowledgeOfTechnologies) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<AdvertisementEntity> findAdvertisementsByKeywordAndCategory(
            @Param("keyword") String keyword,
            @Param("category") String category);
}


