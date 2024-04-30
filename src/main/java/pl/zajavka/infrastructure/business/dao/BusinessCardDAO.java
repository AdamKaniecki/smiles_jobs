package pl.zajavka.infrastructure.business.dao;

import pl.zajavka.infrastructure.database.entity.BusinessCardEntity;
import pl.zajavka.infrastructure.domain.BusinessCard;
import pl.zajavka.infrastructure.domain.User;
import pl.zajavka.infrastructure.security.UserEntity;

import java.util.Optional;

public interface BusinessCardDAO {

    Optional<BusinessCard> findById2(Integer id);
    BusinessCard findById(Integer businessCardId);
//    Optional<BusinessCard> findByUser2(User loggedInUser);
    BusinessCard findByUser(User loggedInUser);
    boolean existByUser(User loggedInUser);

    void save(BusinessCardEntity businessCardEntity);

    void deleteById(Integer id);

    BusinessCard createBusinessCard(BusinessCard businessCard, User user);

    BusinessCard updateBusinessCard(BusinessCard updateBusinessCard);
}
