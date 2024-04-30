package pl.zajavka.infrastructure.database.repository;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.zajavka.infrastructure.business.dao.BusinessCardDAO;
import pl.zajavka.infrastructure.database.entity.BusinessCardEntity;
import pl.zajavka.infrastructure.database.repository.jpa.BusinessCardJpaRepository;
import pl.zajavka.infrastructure.database.repository.mapper.AddressMapper;
import pl.zajavka.infrastructure.database.repository.mapper.BusinessCardMapper;
import pl.zajavka.infrastructure.domain.Address;
import pl.zajavka.infrastructure.domain.BusinessCard;
import pl.zajavka.infrastructure.domain.User;
import pl.zajavka.infrastructure.security.UserEntity;
import pl.zajavka.infrastructure.security.mapper.UserMapper;

import java.util.Optional;

@Repository
@AllArgsConstructor
public class BusinessCardRepository implements BusinessCardDAO {
    private final BusinessCardJpaRepository businessCardJpaRepository;
    private final BusinessCardMapper businessCardMapper;
    private final UserMapper userMapper;
    private final AddressMapper addressMapper;

    public Optional<BusinessCard> findById2(Integer id) {
        return businessCardJpaRepository.findById(id).map(businessCardMapper::map);
    }

    public BusinessCard findById(Integer businessCardId){
        BusinessCardEntity businessCardEntity = businessCardJpaRepository.findById(businessCardId)
                .orElseThrow(()-> new EntityNotFoundException("Not found entity Business Card with ID: " + businessCardId));
        return businessCardMapper.map(businessCardEntity);
    }


    public BusinessCard findByUser(User loggedInUser) {
        UserEntity userEntity = userMapper.map(loggedInUser);
        Optional<BusinessCardEntity> businessCardEntityOptional = businessCardJpaRepository.findByUser(userEntity);
        return businessCardEntityOptional.map(businessCardMapper::map).orElse(null);
    }


    public boolean existByUser(User loggedInUser) {
        return businessCardJpaRepository.existsByUser(userMapper.map(loggedInUser));
    }

    @Override
    public void save(BusinessCardEntity businessCardEntity) {
        businessCardJpaRepository.save(businessCardEntity);
    }

    @Override
    public void deleteById(Integer id) {
        businessCardJpaRepository.deleteById(id);
    }

    @Override
    public BusinessCard createBusinessCard(BusinessCard businessCard, User user) {
        Address address = businessCard.getAddress();
        BusinessCardEntity businessCardEntity = BusinessCardEntity.builder()
                .office(businessCard.getOffice())
                .scopeOperations(businessCard.getScopeOperations())
                .recruitmentEmail(businessCard.getRecruitmentEmail())
                .phoneNumber(businessCard.getPhoneNumber())
                .companyDescription(businessCard.getCompanyDescription())
                .technologiesAndTools(businessCard.getTechnologiesAndTools())
                .certificatesAndAwards(businessCard.getCertificatesAndAwards())
                .user(userMapper.map(user))
                .address(addressMapper.map(address))
                .build();
        businessCardJpaRepository.save(businessCardEntity);
        return businessCardMapper.map(businessCardEntity);
    }

    @Override
    public BusinessCard updateBusinessCard(BusinessCard updateBusinessCard) {
        BusinessCardEntity businessCardEntity = businessCardMapper.map(updateBusinessCard);

        businessCardEntity.setOffice(updateBusinessCard.getOffice());
        businessCardEntity.setScopeOperations(updateBusinessCard.getScopeOperations());
        businessCardEntity.setRecruitmentEmail(updateBusinessCard.getRecruitmentEmail());
        businessCardEntity.setPhoneNumber(updateBusinessCard.getPhoneNumber());
        businessCardEntity.setCompanyDescription(updateBusinessCard.getCompanyDescription());
        businessCardEntity.setTechnologiesAndTools(updateBusinessCard.getTechnologiesAndTools());
        businessCardEntity.setCertificatesAndAwards(updateBusinessCard.getCertificatesAndAwards());
        businessCardJpaRepository.save(businessCardEntity);

        return businessCardMapper.map(businessCardEntity);
    }
}
