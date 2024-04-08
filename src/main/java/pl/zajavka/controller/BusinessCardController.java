package pl.zajavka.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.zajavka.controller.dto.BusinessCardDTO;
import pl.zajavka.controller.dto.UserDTO;
import pl.zajavka.controller.dto.mapper.BusinessCardMapperDTO;
import pl.zajavka.controller.dto.mapper.UserMapperDTO;
import pl.zajavka.infrastructure.business.AddressService;
import pl.zajavka.infrastructure.business.BusinessCardService;
import pl.zajavka.infrastructure.business.UserService;
import pl.zajavka.infrastructure.domain.Address;
import pl.zajavka.infrastructure.domain.BusinessCard;
import pl.zajavka.infrastructure.domain.User;

@Slf4j
@AllArgsConstructor
@Controller
public class BusinessCardController {


    private BusinessCardService businessCardService;
    private AddressService addressService;
    private UserService userService;
    private BusinessCardMapperDTO businessCardMapperDTO;
    private UserMapperDTO userMapperDTO;



    @GetMapping("/BusinessCardForm")
    public String businessCardForm(@ModelAttribute("businessCardDTO") BusinessCardDTO businessCardDTO, Model model,
                                   Authentication authentication) {
        String username = authentication.getName();
        User loggedInUser = userService.findByUserName(username);
        UserDTO userDTO = userMapperDTO.map(loggedInUser);
        model.addAttribute("userDTO", userDTO);
        model.addAttribute("businessCardDTO", businessCardDTO);

        return "create_business_card";
    }


    @PostMapping("/createBusinessCard")
    public String createBusinessCard(@Valid @ModelAttribute("businessCardDTO") BusinessCardDTO businessCardDTO, Model model,
                                     Authentication authentication) {
        String username = authentication.getName();
        User loggedInUser = userService.findByUserName(username);

        if (businessCardService.existByUser(loggedInUser)) {
            return "business_card_already_create";
        }

        BusinessCard businessCard = businessCardMapperDTO.map(businessCardDTO);
        Address createdAddress = addressService.createAddress(businessCard.getAddress(), loggedInUser);
        businessCard.setAddress(createdAddress);
        businessCard.setUser(loggedInUser);

        businessCardService.createBusinessCard(businessCard, loggedInUser);

        model.addAttribute("businessCardDTO", businessCard);

        return "business_card_created_successfully";
    }


    @GetMapping("/showMyBusinessCard")
    public String showMyBusinessCard(Authentication authentication, Model model) {
        String username = authentication.getName();
        User loggedInUser = userService.findByUserName(username);
        BusinessCard userBusinessCard = businessCardService.findByUser(loggedInUser);
        if (userBusinessCard != null) {
            model.addAttribute("businessCardDTO", businessCardMapperDTO.map(userBusinessCard));
            return "show_my_businessCard";
        }
        return "businessCard_not_found";
    }


    @GetMapping("/businessCard/{businessCardId}")
    public String showBusinessCard(@PathVariable Integer businessCardId, Model model) {
        BusinessCard businessCard = businessCardService.findById(businessCardId);
        if (businessCard != null) {
            model.addAttribute("businessCardDTO", businessCardMapperDTO.map(businessCard));
            return "show_businessCard";
        } else {
            model.addAttribute("businessCardDTO", new BusinessCardDTO());
            return "businessCard_not_found";
        }
    }


    @GetMapping("/updateBusinessCardForm")
    public String updateMyBusinessCard(Model model, Authentication authentication) {
        String username = authentication.getName();
        User loggedInUser = userService.findByUserName(username);
        BusinessCard userBusinessCard = businessCardService.findByUser(loggedInUser);
        if (userBusinessCard != null) {
            model.addAttribute("businessCardDTO", businessCardMapperDTO.map(userBusinessCard));
            model.addAttribute("userDTO", userMapperDTO.map(loggedInUser));
            model.addAttribute("address", userBusinessCard.getAddress());

            return "update_business_card_form";
        }
        return "businessCard_not_found";
    }


    @PutMapping("/updateBusinessCardDone")
    public String updateBusinessCard(
            @ModelAttribute("businessCardDTO") BusinessCardDTO updateBusinessCardDTO,
            Model model) {
        BusinessCard businessCard = businessCardService.findById(updateBusinessCardDTO.getId());


        businessCard.setOffice(updateBusinessCardDTO.getOffice());
        businessCard.setScopeOperations(updateBusinessCardDTO.getScopeOperations());
        businessCard.setRecruitmentEmail(updateBusinessCardDTO.getRecruitmentEmail());
        businessCard.setPhoneNumber(updateBusinessCardDTO.getPhoneNumber());
        businessCard.setCompanyDescription(updateBusinessCardDTO.getCompanyDescription());
        businessCard.setTechnologiesAndTools(updateBusinessCardDTO.getTechnologiesAndTools());
        businessCard.setCertificatesAndAwards(updateBusinessCardDTO.getCertificatesAndAwards());

        businessCardService.updateBusinessCard(businessCard);


        model.addAttribute("businessCardDTO", businessCardMapperDTO.map(businessCard));
        return "update_business_card_successfully";
    }


    @DeleteMapping("/deleteBusinessCard")
    public String deleteBusinessCard(@ModelAttribute("businessCardDTO") BusinessCardDTO updateBusinessCardDTO,
                                     Model model) {

        BusinessCard businessCard = businessCardService.findById(updateBusinessCardDTO.getId());
        Address address = businessCard.getAddress();
        businessCardService.deleteBusinessCard(businessCard);
        addressService.deleteAddress(address);
        model.addAttribute("businessCardDTO", businessCardMapperDTO.map(businessCard));

        return "business_card_deleted_successfully";
    }

    @GetMapping("/businessCardNotFound")
    public String getBusinessCardAlert() {
        return "businessCard_not_found";
    }

}
