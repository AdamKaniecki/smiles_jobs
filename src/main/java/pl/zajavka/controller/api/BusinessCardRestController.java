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
        Address createdAddress = addressService.createAddress(businessCard.getAddress());
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
          BusinessCard businessCard  = businessCardService.findByUser(loggedInUser);
            if (businessCard != null ) {
                BusinessCardDTO businessCardDTO = businessCardMapperDTO.map(businessCard);
                return ResponseEntity.ok(businessCardDTO);
            }

        return ResponseEntity.notFound().build();
    }
//
    @GetMapping("/showBusinessCard/{businessCardId}")
    public ResponseEntity<?> showBusinessCard(@PathVariable Integer businessCardId) {
        BusinessCard businessCard = businessCardService.findById(businessCardId);
        if (businessCard != null) {
            BusinessCardDTO businessCardDTO = businessCardMapperDTO.map(businessCard);
            return ResponseEntity.ok(businessCardDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/deleteBusinessCard/{businessCardId}")
    public ResponseEntity<?> deleteBusinessCard(@PathVariable Integer businessCardId) {
        BusinessCard businessCard = businessCardService.findById(businessCardId);
        if (businessCard != null) {
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
        if (loggedInUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        BusinessCard businessCard = businessCardService.findByUser(loggedInUser);
        if (businessCard == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Business card not found");
        }

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
