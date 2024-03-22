package pl.zajavka.controller.api;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pl.zajavka.controller.dto.BusinessCardDTO;
import pl.zajavka.controller.dto.mapper.BusinessCardMapperDTO;
import pl.zajavka.infrastructure.business.AddressService;
import pl.zajavka.infrastructure.business.BusinessCardService;
import pl.zajavka.infrastructure.business.UserService;
import pl.zajavka.infrastructure.domain.Address;
import pl.zajavka.infrastructure.domain.BusinessCard;
import pl.zajavka.infrastructure.domain.User;

import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping("/api")
public class BusinessCardRestController {

    private UserService userService;
    private BusinessCardService businessCardService;
    private BusinessCardMapperDTO businessCardMapperDTO;
    private AddressService addressService;

    @PostMapping("/createBusinessCard")
    public ResponseEntity<String> createBusinessCard(@Valid @RequestBody BusinessCardDTO businessCardDTO,
                                                     Authentication authentication) {
        String username = authentication.getName();
        User loggedInUser = userService.findByUserName(username);

        if (businessCardService.existByUser(loggedInUser)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Business card already created");
        }

        BusinessCard businessCard = businessCardMapperDTO.map(businessCardDTO);
        Address createdAddress = addressService.createAddress(businessCard.getAddress(), loggedInUser);
        businessCard.setAddress(createdAddress);
        businessCard.setUser(loggedInUser);

        businessCardService.createBusinessCard(businessCard, loggedInUser);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Business card created successfully");
    }


    @GetMapping("/showMyBusinessCard")
    public ResponseEntity<?> showMyBusinessCard(Authentication authentication) {
        String username = authentication.getName();
        User loggedInUser = userService.findByUserName(username);
        if (loggedInUser != null) {
            Optional<BusinessCard> businessCardOpt = businessCardService.findByUser2(loggedInUser);
            if (businessCardOpt.isPresent()) {
                BusinessCard businessCard = businessCardOpt.get();
                BusinessCardDTO businessCardDTO = businessCardMapperDTO.map(businessCard);
                return ResponseEntity.ok(businessCardDTO);
            }
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/showBusinessCard/{businessCardId}")
    public ResponseEntity<?> showBusinessCard(@PathVariable Integer businessCardId) {
        Optional<BusinessCard> businessCard = businessCardService.findById2(businessCardId);
        if (businessCard.isPresent()) {
            BusinessCardDTO businessCardDTO = businessCardMapperDTO.map(businessCard.get());
            return ResponseEntity.ok(businessCardDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/deleteBusinessCard/{businessCardId}")
    public ResponseEntity<?> deleteBusinessCard(@PathVariable Integer businessCardId) {
        Optional<BusinessCard> businessCardOptional = businessCardService.findById2(businessCardId);
        if (businessCardOptional.isPresent()) {
            BusinessCard businessCard = businessCardOptional.get();
            Address address = businessCard.getAddress();
            businessCardService.deleteBusinessCard(businessCard);
            addressService.deleteAddress(address);
            return ResponseEntity.ok("Business card deleted successfully");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/updateBusinessCard")
    public ResponseEntity<String> updateCv(@Valid @RequestBody BusinessCardDTO updateBusinessCardDTO, Authentication authentication) {

        String username = authentication.getName();
        User loggedInUser = userService.findByUserName(username);
        BusinessCard businessCard = businessCardService.findByUser(loggedInUser);


        businessCard.setOffice(updateBusinessCardDTO.getOffice());
        businessCard.setScopeOperations(updateBusinessCardDTO.getScopeOperations());
        businessCard.setRecruitmentEmail(updateBusinessCardDTO.getRecruitmentEmail());
        businessCard.setPhoneNumber(updateBusinessCardDTO.getPhoneNumber());
        businessCard.setCompanyDescription(updateBusinessCardDTO.getCompanyDescription());
        businessCard.setTechnologiesAndTools(updateBusinessCardDTO.getTechnologiesAndTools());
        businessCard.setCertificatesAndAwards(updateBusinessCardDTO.getCertificatesAndAwards());

        businessCardService.updateBusinessCard(businessCard);

        return ResponseEntity.status(HttpStatus.OK).body("Business Card updated successfully");
    }

}
