package pl.zajavka.api.controller;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.zajavka.api.dto.BusinessCardDTO;
import pl.zajavka.api.dto.CvDTO;
import pl.zajavka.api.dto.mapper.BusinessCardMapperDTO;
import pl.zajavka.api.dto.mapper.UserMapperDTO;
import pl.zajavka.business.AddressService;
import pl.zajavka.business.BusinessCardService;
import pl.zajavka.business.UserService;
import pl.zajavka.domain.Address;
import pl.zajavka.domain.BusinessCard;
import pl.zajavka.domain.CV;
import pl.zajavka.domain.User;

import java.util.Optional;

@Slf4j
@AllArgsConstructor
@Controller
public class BusinessCardController {

    private HttpSession httpSession;
    private BusinessCardService businessCardService;
    private AddressService addressService;
    private UserService userService;
    private BusinessCardMapperDTO businessCardMapperDTO;
    private UserMapperDTO userMapperDTO;

    @GetMapping("/BusinessCardForm")
    public String BusinessCardForm(@ModelAttribute ("businessCardDTO") BusinessCardDTO businessCardDTO,  Model model) {
        String username = (String) httpSession.getAttribute("username");
        if (username != null) {
            User user = userService.findByUserName(username);
             userMapperDTO.map(user);
            model.addAttribute("userDTO", user);
            model.addAttribute("businessCardDTO",businessCardDTO);
            return "create_business_card";
        } else {
            // Obsłuż brak zalogowanego użytkownika
            return "login";  // Przekieruj na stronę logowania
        }
    }

    @PostMapping("/createBusinessCard")
    public String createBusinessCard(
            @ModelAttribute("businessCardDTO") BusinessCardDTO businessCardDTO,
            Model model) {
//        log.info("Received business card: {}", businessCard);
        String username = (String) httpSession.getAttribute("username");

        if (username != null) {
            User loggedInUser = userService.findByUserName(username);
            if (businessCardService.existByUser(loggedInUser)){
                return "cv_already_created";
            }
//           UserDTO userDTO =  userMapperDTO.map(loggedInUser);

            BusinessCard businessCard = businessCardMapperDTO.map(businessCardDTO);
            // Adres
//            Address address = businessCard.getAddress();
            Address createdAddress = addressService.createAddress(businessCard.getAddress(), loggedInUser);

            // Karta biznesowa
            businessCard.setAddress(createdAddress);
            businessCard.setUser(loggedInUser);
            businessCardService.createBusinessCard(businessCard, loggedInUser);

            model.addAttribute("businessCardDTO", businessCard);
            model.addAttribute("userDTO", loggedInUser);

            return "business_card_created_successfully";
        } else {
            // Obsłuż brak zalogowanego użytkownika
            return "login";  // Przekieruj na stronę logowania
        }
    }



    @GetMapping("/redirectToShowMyBusinessCard")
    public String redirectToShowMyBusinessCard(HttpSession httpSession) {
        String username = (String) httpSession.getAttribute("username");

        if (username != null) {
            User loggedInUser = userService.findByUserName(username);

            if (loggedInUser != null) {
                // Sprawdź, czy użytkownik ma przypisane BusinessCard
                Optional<BusinessCard> userBusinessCard = businessCardService.findByUser(loggedInUser);

                if (userBusinessCard.isPresent()) {
                    Integer businessCardId = userBusinessCard.get().getId();

                    // Przekieruj na endpoint showBusinessCard z odpowiednim identyfikatorem
                    return "redirect:/showBusinessCard?id=" + businessCardId;
                }
            }
        }

        // Obsłuż sytuację, gdy użytkownik nie jest zalogowany, nie ma przypisanego CV lub wystąpił inny problem
        return "businessCard_not_found";  // Przekieruj na stronę główną lub obsłuż inaczej
    }

    @GetMapping("/showBusinessCard")
    public String showMyBusinessCard(@RequestParam Integer id, Model model) {
        Optional<BusinessCard> myBusinessCard = businessCardService.findById(id);

        if (myBusinessCard.isPresent()) {
            BusinessCard businessCard = myBusinessCard.get();
            model.addAttribute("businessCard", businessCardMapperDTO.map(businessCard));
            model.addAttribute("userDTO", userMapperDTO.map(businessCard.getUser()));
            return "show_my_businessCard";
        } else {
            return "businessCard_not_found";  // Możesz utworzyć osobny widok dla przypadku, gdy BusinessCard nie zostało znalezione
        }
    }


    @GetMapping("/businessCard/{id}")
    public String showBusinessCard(@PathVariable Integer id, Model model) {
        BusinessCard businessCard = businessCardService.findById(id).orElse(null);
        if (businessCard != null) {
            model.addAttribute("businessCard", businessCardMapperDTO.map(businessCard));
            model.addAttribute("userDTO", userMapperDTO.map(businessCard.getUser()));
            return "show_businessCard";
        } else {

            return "businessCard_not_found"; // Nazwa widoku Thymeleaf
        }
    }


    @GetMapping("/redirectToUpdateMyBusinessCard")
    public String redirectToUpdateMyBusinessCard(HttpSession httpSession) {
        String username = (String) httpSession.getAttribute("username");
        if (username != null) {
            User loggedInUser = userService.findByUserName(username);
            if (loggedInUser != null) {
                // Sprawdź, czy użytkownik ma przypisane CV
                Optional<BusinessCard> userBusinessCard = businessCardService.findByUser(loggedInUser);
                if (userBusinessCard.isPresent()) {
                    Integer cvId = userBusinessCard.get().getId();
                    // Przekieruj na endpoint showCV z odpowiednim identyfikatorem
                    return "redirect:/updateBusinessCardForm?id=" + cvId;
                }
            }
        }
        return "businessCard_not_found";  // Przekieruj na stronę główną lub obsłuż inaczej
    }

    @GetMapping("/updateBusinessCardForm")
    public String updateMyBusinessCard(@RequestParam Integer id, Model model) {
        Optional<BusinessCard> myBusinessCard = businessCardService.findById(id);
        if (myBusinessCard.isPresent()) {
            BusinessCard businessCard = myBusinessCard.get();
            model.addAttribute("businessCardDTO", businessCardMapperDTO.map(businessCard));
            model.addAttribute("userDTO", userMapperDTO.map(businessCard.getUser()));
            model.addAttribute("address", businessCard.getAddress());
            return "update_business_card_form";
        } else {
            return "businessCard_not_found";  // Możesz utworzyć osobny widok dla przypadku, gdy CV nie zostało znalezione
        }
    }

    @PutMapping("/updateBusinessCardDone")
    public String updateBusinessCard(

            @ModelAttribute("businessCardDTO") BusinessCardDTO updateBusinessCardDTO,
            Model model) {
        Optional<BusinessCard> myBusinessCard = businessCardService.findById(updateBusinessCardDTO.getId());
        if (myBusinessCard.isPresent()) {
            BusinessCard businessCard = myBusinessCard.get();

            businessCard.setOffice(updateBusinessCardDTO.getOffice());
            businessCard.setScopeOperations(updateBusinessCardDTO.getScopeOperations());
            businessCard.setRecruitmentEmail(updateBusinessCardDTO.getRecruitmentEmail());
            businessCard.setPhoneNumber(updateBusinessCardDTO.getPhoneNumber());
            businessCard.setCompanyDescription(updateBusinessCardDTO.getCompanyDescription());
            businessCard.setTechnologiesAndTools(updateBusinessCardDTO.getTechnologiesAndTools());
            businessCard.setCertificatesAndAwards(updateBusinessCardDTO.getCertificatesAndAwards());

          businessCardService.updateBusinessCard(businessCard);



            model.addAttribute("businessCardDTO", businessCardMapperDTO.map(businessCard));
            return "cv_created_successfully";
        } else {
            return "cv_not_found";
        }

    }

    @DeleteMapping("/deleteBusinessCard")
    public String deleteBusinessCard(@ModelAttribute("businessCardDTO") BusinessCardDTO updateBusinessCardDTO,
    Model model) {

        Optional<BusinessCard> myBusinessCard = businessCardService.findById(updateBusinessCardDTO.getId());
        if (myBusinessCard.isPresent()) {
            BusinessCard businessCard = myBusinessCard.get();
            Address address = businessCard.getAddress();

            businessCardService.deleteBusinessCard(businessCard);
            addressService.deleteAddress(address);
            model.addAttribute("businessCardDTO", businessCardMapperDTO.map(businessCard));

                        return "redirect: /company_portal";
                    }

        // Obsłuż sytuację, gdy użytkownik nie jest zalogowany, nie ma przypisanej BusinessCard lub wystąpił inny problem
        return "redirect:/redirectToShowMyBusinessCard";
    }


}
