package pl.zajavka.business;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;
import pl.zajavka.domain.Address;
import pl.zajavka.domain.BusinessCard;
import pl.zajavka.domain.CV;
import pl.zajavka.domain.User;
import pl.zajavka.infrastructure.database.entity.BusinessCardEntity;
import pl.zajavka.infrastructure.database.entity.CvEntity;
import pl.zajavka.infrastructure.database.repository.BusinessCardRepository;
import pl.zajavka.infrastructure.database.repository.mapper.AddressMapper;
import pl.zajavka.infrastructure.database.repository.mapper.BusinessCardMapper;
import pl.zajavka.infrastructure.security.mapper.UserMapper;

import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class BusinessCardService {
    private BusinessCardRepository businessCardRepository;
    private BusinessCardMapper businessCardMapper;
    private AddressService addressService;
    private UserMapper userMapper;
    private AddressMapper addressMapper;

    @Transactional
    public BusinessCard createBusinessCard(BusinessCard businessCard, User user) {

        if (businessCardRepository.existsByUser(userMapper.map(user))) {
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

        businessCardRepository.saveAndFlush(businessCardEntity);
        return businessCardMapper.map(businessCardEntity);
    }

    public Optional<BusinessCard> findById(Integer id) {
        log.debug("szukaj id w serwisie: w ", id);
        return businessCardRepository.findById(id).map(businessCardMapper::map);
    }

    {

    }

    public Optional<BusinessCard> findByUser(User loggedInUser) {
        Optional<BusinessCardEntity> businessCardEntityOptional = businessCardRepository.findByUser(userMapper.map(loggedInUser));
        return businessCardEntityOptional.map(businessCardMapper::map);
    }

    public boolean existByUser(User loggedInUser) {
        return businessCardRepository.existsByUser(userMapper.map(loggedInUser));
    }

    @Transactional
    public BusinessCard updateBusinessCard(BusinessCard updateBusinessCard) {
        if (updateBusinessCard.getId() != null) {
            // Sprawdź, czy CV istnieje w bazie danych
            BusinessCardEntity businessCardEntity = businessCardRepository.findById(updateBusinessCard.getId())
                    .orElseThrow(() -> new NotFoundException("Business Card with ID " + updateBusinessCard.getId() + " not found"));

            businessCardEntity.setOffice(updateBusinessCard.getOffice());
            businessCardEntity.setScopeOperations(updateBusinessCard.getScopeOperations());
            businessCardEntity.setRecruitmentEmail(updateBusinessCard.getRecruitmentEmail());
            businessCardEntity.setPhoneNumber(updateBusinessCard.getPhoneNumber());
            businessCardEntity.setCompanyDescription(updateBusinessCard.getCompanyDescription());
            businessCardEntity.setTechnologiesAndTools(updateBusinessCard.getTechnologiesAndTools());
            businessCardEntity.setCertificatesAndAwards(updateBusinessCard.getCertificatesAndAwards());


            // Zapisz zaktualizowany obiekt CV w bazie danych
            BusinessCardEntity businessCardEntityUpdate = businessCardRepository.save(businessCardEntity);

            return businessCardMapper.map(businessCardEntityUpdate);
        } else {
            // Obsłuż sytuację, gdy CV nie zostało znalezione w bazie danych
            throw new NotFoundException("Business Card ID cannot be null");
        }
    }


    public void deleteBusinessCard(BusinessCard businessCard) {

        if (businessCard != null){
            BusinessCardEntity businessCardEntity = businessCardMapper.map(businessCard);
            businessCardRepository.deleteById(businessCardEntity.getId());
        }   else {
        throw new IllegalArgumentException("Business Card cannot be null");
    }
    }


}


