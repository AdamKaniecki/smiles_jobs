package pl.zajavka.infrastructure.business.dao;

import pl.zajavka.infrastructure.domain.BusinessCard;
import pl.zajavka.infrastructure.domain.User;

import java.util.Optional;

public interface BusinessCardDAO {

    Optional<BusinessCard> findById2(Integer id);
    BusinessCard findById(Integer businessCardId);
    Optional<BusinessCard> findByUser2(User loggedInUser);
    BusinessCard findByUser(User loggedInUser);
    boolean existByUser(User loggedInUser);
}
