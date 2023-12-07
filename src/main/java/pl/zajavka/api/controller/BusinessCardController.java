package pl.zajavka.api.controller;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
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
          UserDTO userDTO = userMapperDTO.map(user);
            model.addAttribute("userDTO", userDTO);
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
           UserDTO userDTO =  userMapperDTO.map(loggedInUser);

            BusinessCard businessCard = businessCardMapperDTO.map(businessCardDTO);
            // Adres
            Address address = businessCard.getAddress();
            Address createdAddress = addressService.createAddress(address, loggedInUser);

            // Karta biznesowa
            businessCard.setAddress(createdAddress);
            businessCard.setUser(loggedInUser);
            businessCardService.createBusinessCard(businessCard, loggedInUser);

            model.addAttribute("businessCardDTO", businessCardDTO);
            model.addAttribute("userDTO", userDTO);

            return "business_card_created_successfully";
        } else {
            // Obsłuż brak zalogowanego użytkownika
            return "login";  // Przekieruj na stronę logowania
        }
    }
}
