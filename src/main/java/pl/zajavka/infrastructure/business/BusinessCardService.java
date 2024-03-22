package pl.zajavka.infrastructure.business;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pl.zajavka.infrastructure.domain.Address;
import pl.zajavka.infrastructure.domain.BusinessCard;
import pl.zajavka.infrastructure.domain.User;
import pl.zajavka.infrastructure.database.entity.BusinessCardEntity;
import pl.zajavka.infrastructure.database.repository.jpa.BusinessCardJpaRepository;
import pl.zajavka.infrastructure.database.repository.mapper.AddressMapper;
import pl.zajavka.infrastructure.database.repository.mapper.BusinessCardMapper;
import pl.zajavka.infrastructure.security.UserEntity;
import pl.zajavka.infrastructure.security.mapper.UserMapper;

import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class BusinessCardService {
    private BusinessCardJpaRepository businessCardJpaRepository;
    private BusinessCardMapper businessCardMapper;
    private UserMapper userMapper;
    private AddressMapper addressMapper;

    @Transactional
    public BusinessCard createBusinessCard(BusinessCard businessCard, User user) {

        if (businessCardJpaRepository.existsByUser(userMapper.map(user))) {
            return null;
        }

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

        businessCardJpaRepository.saveAndFlush(businessCardEntity);
        return businessCardMapper.map(businessCardEntity);
    }

//    public Optional<BusinessCard> findById2(Integer id) {
//        return businessCardJpaRepository.findById(id).map(businessCardMapper::map);
//    }

//   public BusinessCard findById(Integer businessCardId){
//        BusinessCardEntity businessCardEntity = businessCardJpaRepository.findById(businessCardId)
//                .orElseThrow(()-> new EntityNotFoundException("Not found Business Card with ID: " + businessCardId));
//        return businessCardMapper.map(businessCardEntity);
//   }

//    public Optional<BusinessCard> findByUser2(User loggedInUser) {
//        Optional<BusinessCardEntity> businessCardEntityOptional = businessCardJpaRepository.findByUser(userMapper.map(loggedInUser));
//        return businessCardEntityOptional.map(businessCardMapper::map);
//    }
//    public BusinessCard findByUser(User loggedInUser){
//        UserEntity userEntity = userMapper.map(loggedInUser);
//        BusinessCardEntity businessCardEntity = businessCardJpaRepository.findByUser(userEntity)
//                .orElseThrow(()-> new EntityNotFoundException("Not found Business Card from User: " + userEntity));
//        return businessCardMapper.map(businessCardEntity);
//    }



//    public boolean existByUser(User loggedInUser) {
//        return businessCardJpaRepository.existsByUser(userMapper.map(loggedInUser));
//    }

    @Transactional
    public BusinessCard updateBusinessCard(BusinessCard updateBusinessCard) {
        if (updateBusinessCard.getId() != null) {
            // Sprawdź, czy CV istnieje w bazie danych
            BusinessCardEntity businessCardEntity = businessCardJpaRepository.findById(updateBusinessCard.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Business Card with ID " + updateBusinessCard.getId() + " not found"));

            businessCardEntity.setOffice(updateBusinessCard.getOffice());
            businessCardEntity.setScopeOperations(updateBusinessCard.getScopeOperations());
            businessCardEntity.setRecruitmentEmail(updateBusinessCard.getRecruitmentEmail());
            businessCardEntity.setPhoneNumber(updateBusinessCard.getPhoneNumber());
            businessCardEntity.setCompanyDescription(updateBusinessCard.getCompanyDescription());
            businessCardEntity.setTechnologiesAndTools(updateBusinessCard.getTechnologiesAndTools());
            businessCardEntity.setCertificatesAndAwards(updateBusinessCard.getCertificatesAndAwards());


            // Zapisz zaktualizowany obiekt CV w bazie danych
            BusinessCardEntity businessCardEntityUpdate = businessCardJpaRepository.save(businessCardEntity);

            return businessCardMapper.map(businessCardEntityUpdate);
        } else {
            // Obsłuż sytuację, gdy CV nie zostało znalezione w bazie danych
            throw new EntityNotFoundException("Business Card ID cannot be null");
        }
    }


    public void deleteBusinessCard(BusinessCard businessCard) {

        if (businessCard != null){
            BusinessCardEntity businessCardEntity = businessCardMapper.map(businessCard);
            businessCardJpaRepository.deleteById(businessCardEntity.getId());
        }   else {
        throw new IllegalArgumentException("Business Card cannot be null");
    }
    }


}


