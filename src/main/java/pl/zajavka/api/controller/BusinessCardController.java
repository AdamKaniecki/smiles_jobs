package pl.zajavka.api.controller;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
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

    @GetMapping("/BusinessCardForm")
    public String BusinessCardForm(Model model) {
        String username = (String) httpSession.getAttribute("username");
        if (username != null) {
            User user = userService.findByUserName(username);
            model.addAttribute("user", user);
            model.addAttribute("businessCard", BusinessCard.builder().build());
            return "create_business_card";
        } else {
            // Obsłuż brak zalogowanego użytkownika
            return "login";  // Przekieruj na stronę logowania
        }
    }

    @PostMapping("/createBusinessCard")
    public String createBusinessCard(
            @ModelAttribute("businessCard") BusinessCard businessCard,
            Model model) {
        log.info("Received business card: {}", businessCard);
        String username = (String) httpSession.getAttribute("username");

        if (username != null) {
            User loggedInUser = userService.findByUserName(username);

            // Adres
            Address address = businessCard.getAddress();
            Address createdAddress = addressService.createAddress(address, loggedInUser);

            // Karta biznesowa
            businessCard.setAddress(createdAddress);
            businessCard.setUser(loggedInUser);
            businessCardService.createBusinessCard(businessCard, loggedInUser);

            model.addAttribute("businessCard", businessCard);
            model.addAttribute("user", loggedInUser);

            return "business_card_created_successfully";
        } else {
            // Obsłuż brak zalogowanego użytkownika
            return "login";  // Przekieruj na stronę logowania
        }
    }
}
