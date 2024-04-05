package pl.zajavka.infrastructure.business;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.zajavka.infrastructure.business.dao.BusinessCardDAO;
import pl.zajavka.infrastructure.database.entity.BusinessCardEntity;
import pl.zajavka.infrastructure.database.repository.mapper.AddressMapper;
import pl.zajavka.infrastructure.database.repository.mapper.BusinessCardMapper;
import pl.zajavka.infrastructure.domain.Address;
import pl.zajavka.infrastructure.domain.BusinessCard;
import pl.zajavka.infrastructure.domain.User;
import pl.zajavka.infrastructure.security.mapper.UserMapper;

@Slf4j
@Service
@AllArgsConstructor
public class BusinessCardService {
    private BusinessCardMapper businessCardMapper;
    private UserMapper userMapper;
    private AddressMapper addressMapper;
    private final BusinessCardDAO businessCardDAO;


    @Transactional
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

        businessCardDAO.save(businessCardEntity);
        return businessCardMapper.map(businessCardEntity);
    }


    @Transactional
    public BusinessCard updateBusinessCard(BusinessCard updateBusinessCard) {
        if (updateBusinessCard.getId() != null) {
            businessCardDAO.findById(updateBusinessCard.getId());

            BusinessCardEntity businessCardEntity = businessCardMapper.map(updateBusinessCard);

            businessCardEntity.setOffice(updateBusinessCard.getOffice());
            businessCardEntity.setScopeOperations(updateBusinessCard.getScopeOperations());
            businessCardEntity.setRecruitmentEmail(updateBusinessCard.getRecruitmentEmail());
            businessCardEntity.setPhoneNumber(updateBusinessCard.getPhoneNumber());
            businessCardEntity.setCompanyDescription(updateBusinessCard.getCompanyDescription());
            businessCardEntity.setTechnologiesAndTools(updateBusinessCard.getTechnologiesAndTools());
            businessCardEntity.setCertificatesAndAwards(updateBusinessCard.getCertificatesAndAwards());


            // Zapisz zaktualizowany obiekt CV w bazie danych
            businessCardDAO.save(businessCardEntity);

            return businessCardMapper.map(businessCardEntity);
        } else {
            // Obsłuż sytuację, gdy CV nie zostało znalezione w bazie danych
            throw new EntityNotFoundException("Business Card ID cannot be null");
        }
    }

    @Transactional
    public void deleteBusinessCard(BusinessCard businessCard) {

        if (businessCard != null) {
            BusinessCardEntity businessCardEntity = businessCardMapper.map(businessCard);
            businessCardDAO.deleteById(businessCardEntity.getId());
        } else {
            throw new IllegalArgumentException("Business Card cannot be null");
        }
    }


    public BusinessCard findByUser(User user) {
        return businessCardDAO.findByUser(user);
    }

    public boolean existByUser(User loggedInUser) {
        return businessCardDAO.existByUser(loggedInUser);
    }

    public BusinessCard findById(Integer businessCardId) {
        return businessCardDAO.findById(businessCardId);
    }
}


