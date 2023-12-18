package pl.zajavka.business;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    AddressService addressService;
    private UserMapper userMapper;
    private AddressMapper addressMapper;

    @Transactional
    public BusinessCard createBusinessCard(BusinessCard businessCard, User user) {

        if(businessCardRepository.existsByUser(userMapper.map(user))){
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
}
