package pl.zajavka.infrastructure.database.repository;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.zajavka.infrastructure.business.dao.BusinessCardDAO;
import pl.zajavka.infrastructure.database.entity.BusinessCardEntity;
import pl.zajavka.infrastructure.database.repository.jpa.BusinessCardJpaRepository;
import pl.zajavka.infrastructure.database.repository.mapper.BusinessCardMapper;
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

    public Optional<BusinessCard> findById2(Integer id) {
        return businessCardJpaRepository.findById(id).map(businessCardMapper::map);
    }

    public BusinessCard findById(Integer businessCardId){
        BusinessCardEntity businessCardEntity = businessCardJpaRepository.findById(businessCardId)
                .orElseThrow(()-> new EntityNotFoundException("Not found entity Business Card with ID: " + businessCardId));
        return businessCardMapper.map(businessCardEntity);
    }

    public Optional<BusinessCard> findByUser2(User loggedInUser) {
        Optional<BusinessCardEntity> businessCardEntityOptional = businessCardJpaRepository.findByUser(userMapper.map(loggedInUser));
        return businessCardEntityOptional.map(businessCardMapper::map);
    }

    public BusinessCard findByUser(User loggedInUser){
        UserEntity userEntity = userMapper.map(loggedInUser);
        BusinessCardEntity businessCardEntity = businessCardJpaRepository.findByUser(userEntity)
                .orElseThrow(()-> new EntityNotFoundException("Not found Business Card from User: " + userEntity));
        return businessCardMapper.map(businessCardEntity);
    }

    public boolean existByUser(User loggedInUser) {
        return businessCardJpaRepository.existsByUser(userMapper.map(loggedInUser));
    }



}
