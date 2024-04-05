package pl.zajavka.infrastructure.database.repository.jpa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.zajavka.infrastructure.database.entity.JobOfferEntity;
import pl.zajavka.infrastructure.security.UserEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface JobOfferJpaRepository extends JpaRepository<JobOfferEntity, Integer> {

    @Query("SELECT j FROM JobOfferEntity j WHERE j.active = true AND (" +
            "(:category = 'companyName' AND LOWER(j.companyName) LIKE LOWER(CONCAT('%', :keyword, '%'))) OR " +
            "(:category = 'position' AND LOWER(j.position) LIKE LOWER(CONCAT('%', :keyword, '%'))) OR " +
            "(:category = 'requiredTechnologies' AND LOWER(j.requiredTechnologies) LIKE LOWER(CONCAT('%', :keyword, '%'))) OR " +
            "(:category = 'experience' AND LOWER(j.experience) LIKE LOWER(CONCAT('%', :keyword, '%'))) OR " +
            "(:category = 'jobLocation' AND LOWER(j.jobLocation) LIKE LOWER(CONCAT('%', :keyword, '%'))) OR " +
            "(:category = 'typeOfWork' AND LOWER(j.typeOfWork) LIKE LOWER(CONCAT('%', :keyword, '%'))) OR " +
            "(:category = 'requiredLanguage' AND LOWER(j.requiredLanguage) LIKE LOWER(CONCAT('%', :keyword, '%'))))"
    )
    List<JobOfferEntity> findActiveJobOffersByKeywordAndCategory(
            @Param("keyword") String keyword,
            @Param("category") String category);

    @Query("SELECT j FROM JobOfferEntity j WHERE j.active = true AND " +
            "(:category = 'salaryMin' AND j.salaryMin >= CAST(:salary AS java.math.BigDecimal))")
    List<JobOfferEntity> findActiveJobOffersBySalaryAndCategory(
            @Param("category") String category,
            @Param("salary") BigDecimal salary);

    Optional<JobOfferEntity> findByUser(UserEntity userEntity);

    Optional<JobOfferEntity> findById(Integer id);

    List<JobOfferEntity> findListByUser(UserEntity user);

    Page<JobOfferEntity> findAll(Pageable pageable);


            ;


}
