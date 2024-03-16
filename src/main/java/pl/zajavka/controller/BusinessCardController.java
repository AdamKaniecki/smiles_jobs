package pl.zajavka.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.zajavka.controller.dto.BusinessCardDTO;
import pl.zajavka.controller.dto.CvDTO;
import pl.zajavka.controller.dto.UserDTO;
import pl.zajavka.controller.dto.mapper.BusinessCardMapperDTO;
import pl.zajavka.controller.dto.mapper.UserMapperDTO;
import pl.zajavka.infrastructure.business.AddressService;
import pl.zajavka.infrastructure.business.BusinessCardService;
import pl.zajavka.infrastructure.business.UserService;
import pl.zajavka.infrastructure.domain.Address;
import pl.zajavka.infrastructure.domain.BusinessCard;
import pl.zajavka.infrastructure.domain.CV;
import pl.zajavka.infrastructure.domain.User;

import java.util.Optional;

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
            return "cv_already_created";
        }

        BusinessCard businessCard = businessCardMapperDTO.map(businessCardDTO);
        Address createdAddress = addressService.createAddress(businessCard.getAddress(), loggedInUser);
        businessCard.setAddress(createdAddress);
        businessCard.setUser(loggedInUser);

        businessCardService.createBusinessCard(businessCard, loggedInUser);

        model.addAttribute("businessCardDTO", businessCard);
        model.addAttribute("userDTO", loggedInUser);

        return "business_card_created_successfully";
    }


    @GetMapping("/showMyBusinessCard")
    public String showMyBusinessCard(Authentication authentication, Model model) {
        String username = authentication.getName();
        User loggedInUser = userService.findByUserName(username);
        Optional<BusinessCard> userBusinessCard = businessCardService.findByUser2(loggedInUser);
        if (userBusinessCard.isPresent()) {
            BusinessCard businessCard = userBusinessCard.get();

            model.addAttribute("businessCardDTO", businessCardMapperDTO.map(businessCard));
            model.addAttribute("userDTO", userMapperDTO.map(loggedInUser));
            return "show_my_businessCard";
        }
        return "businessCard_not_found";
    }


    @GetMapping("/businessCard/{businessCardId}")
    public String showBusinessCard(@PathVariable Integer businessCardId, Model model) {
        Optional<BusinessCard> businessCard = businessCardService.findById2(businessCardId);
        if (businessCard.isPresent()) {
            model.addAttribute("businessCardDTO", businessCardMapperDTO.map(businessCard.get()));
//            model.addAttribute("userDTO", userMapperDTO.map(businessCard.getUser()));
            return "show_businessCard";
        } else {
            return "businessCard_not_found";
        }
    }



    @GetMapping("/updateBusinessCardForm")
    public String updateMyBusinessCard(Model model, Authentication authentication) {
        String username = authentication.getName();
        User loggedInUser = userService.findByUserName(username);
        UserDTO userDTO = userMapperDTO.map(loggedInUser);
        if (loggedInUser != null) {
            Optional<BusinessCard> userBusinessCard = businessCardService.findByUser2(loggedInUser);
            if (userBusinessCard.isPresent()) {
                BusinessCard businessCard = userBusinessCard.get();
                BusinessCardDTO businessCardDTO = businessCardMapperDTO.map(businessCard);

                model.addAttribute("businessCardDTO", businessCardDTO);
                model.addAttribute("userDTO", userMapperDTO.map(userDTO));
                model.addAttribute("address", businessCard.getAddress());

                return "update_business_card_form";
            }
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
        return "business_card_created_successfully";
    }


    @DeleteMapping("/deleteBusinessCard")
    public String deleteBusinessCard(@ModelAttribute("businessCardDTO") BusinessCardDTO updateBusinessCardDTO,
                                     Model model) {

        BusinessCard businessCard = businessCardService.findById(updateBusinessCardDTO.getId());
        Address address = businessCard.getAddress();
        businessCardService.deleteBusinessCard(businessCard);
        addressService.deleteAddress(address);
        model.addAttribute("businessCardDTO", businessCardMapperDTO.map(businessCard));


        return "business_card_created_successfully";
    }


}
