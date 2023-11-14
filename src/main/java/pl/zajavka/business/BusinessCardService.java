package pl.zajavka.business;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.zajavka.domain.Address;
import pl.zajavka.domain.BusinessCard;
import pl.zajavka.domain.User;
import pl.zajavka.infrastructure.database.entity.AddressEntity;
import pl.zajavka.infrastructure.database.entity.BusinessCardEntity;
import pl.zajavka.infrastructure.database.repository.BusinessCardRepository;
import pl.zajavka.infrastructure.database.repository.mapper.AddressMapper;
import pl.zajavka.infrastructure.database.repository.mapper.BusinessCardMapper;
import pl.zajavka.infrastructure.security.mapper.UserMapper;

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
        Address address = businessCard.getAddress();
//        Address createdAddress = addressService.createAddress(businessCard.getAddress(), user);


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

        BusinessCardEntity created = businessCardRepository.saveAndFlush(businessCardEntity);
        return businessCardMapper.map(created);
    }
}
