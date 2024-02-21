package pl.zajavka.api.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.zajavka.api.dto.BusinessCardDTO;
import pl.zajavka.api.dto.UserDTO;
import pl.zajavka.api.dto.mapper.BusinessCardMapperDTO;
import pl.zajavka.api.dto.mapper.UserMapperDTO;
import pl.zajavka.business.AddressService;
import pl.zajavka.business.BusinessCardService;
import pl.zajavka.business.UserService;
import pl.zajavka.domain.Address;
import pl.zajavka.domain.BusinessCard;
import pl.zajavka.domain.User;

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
        User loggedInUser = userService.getLoggedInUser((authentication));
        UserDTO userDTO = userMapperDTO.map(loggedInUser);
        model.addAttribute("userDTO", userDTO);
        model.addAttribute("businessCardDTO", businessCardDTO);
        return "create_business_card";
    }


    @PostMapping("/createBusinessCard")
    public String createBusinessCard(@ModelAttribute("businessCardDTO") BusinessCardDTO businessCardDTO, Model model,
                                     Authentication authentication) {
        User loggedInUser = userService.getLoggedInUser((authentication));

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


@GetMapping("/redirectToShowMyBusinessCard")
public String redirectToShowMyBusinessCard(Authentication authentication) {
    User loggedInUser = userService.getLoggedInUser((authentication));

            Optional<BusinessCard> userBusinessCard = businessCardService.findByUser2(loggedInUser);
            if (userBusinessCard.isPresent()) {
                Integer businessCardId = userBusinessCard.get().getId();
                // Przekieruj na endpoint showBusinessCard z odpowiednim identyfikatorem
                return "redirect:/showBusinessCard?id=" + businessCardId;
            }
    return "businessCard_not_found";
}


    @GetMapping("/showBusinessCard")
    public String showMyBusinessCard(@RequestParam Integer id, Model model) {
        BusinessCard businessCard = businessCardService.findById(id);

        model.addAttribute("businessCardDTO", businessCardMapperDTO.map(businessCard));
        model.addAttribute("userDTO", userMapperDTO.map(businessCard.getUser()));
        return "show_my_businessCard";

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




    @GetMapping("/redirectToUpdateMyBusinessCard")
    public String redirectToUpdateMyBusinessCard(Authentication authentication) {
        User loggedInUser = userService.getLoggedInUser((authentication));
        // Sprawdź, czy użytkownik ma przypisane CV
        BusinessCard userBusinessCard = businessCardService.findByUser(loggedInUser);

        Integer cvId = userBusinessCard.getId();
        // Przekieruj na endpoint showCV z odpowiednim identyfikatorem
        return "redirect:/updateBusinessCardForm?id=" + cvId;
    }


    @GetMapping("/updateBusinessCardForm")
    public String updateMyBusinessCard(@RequestParam Integer id, Model model) {
        BusinessCard businessCard = businessCardService.findById(id);

        model.addAttribute("businessCardDTO", businessCardMapperDTO.map(businessCard));
        model.addAttribute("userDTO", userMapperDTO.map(businessCard.getUser()));
        model.addAttribute("address", businessCard.getAddress());
        return "update_business_card_form";
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


        return"redirect:/redirectToShowMyBusinessCard";
}


}
