package pl.zajavka.api.controller;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import pl.zajavka.api.dto.CvDTO;
import pl.zajavka.api.dto.mapper.CvMapperDTO;
import pl.zajavka.api.dto.mapper.UserMapperDTO;
import pl.zajavka.business.AddressService;
import pl.zajavka.business.BusinessCardService;
import pl.zajavka.business.CvService;
import pl.zajavka.business.UserService;
import pl.zajavka.domain.*;

@Slf4j
@AllArgsConstructor
@Controller
public class CvController {


    private HttpSession httpSession;
    private CvService cvService;
    private AddressService addressService;
    private UserService userService;
    private CvMapperDTO cvMapperDTO;
    private UserMapperDTO userMapperDTO;

    @GetMapping("/CvForm")
    public String CvForm(
            @ModelAttribute("cvDTO") CvDTO cvDTO,
            Model model) {
        String username = (String) httpSession.getAttribute("username");
        if (username != null) {
            User user = userService.findByUserName(username);
            userMapperDTO.map(user);
            model.addAttribute("userDTO", user);
            model.addAttribute("cvDTO", cvDTO);
            return "create_cv";
        } else {
            // Obsłuż brak zalogowanego użytkownika
            return "login";  // Przekieruj na stronę logowania
        }
    }

    @PostMapping("/createCV")
    public String createCV(@ModelAttribute("cvDTO") CvDTO cvDTO, Model model) {
//        log.info("Received CV: {}", cv);
        String username = (String) httpSession.getAttribute("username");

        if (username != null) {
            User loggedInUser = userService.findByUserName(username);
            CV cv = cvMapperDTO.map(cvDTO);
//             Adres
            Address createdAddress = addressService.createAddress(cv.getAddress(), loggedInUser);
//
//            // Reklama
//            Advertisement advertisement = cv.getAdvertisement();
//
            // CV
            cv.setAddress(createdAddress);
            cv.setUser(loggedInUser);
            cvService.createCV(cv, loggedInUser);

            model.addAttribute("cvDTO", cv);
            model.addAttribute("userDTO", loggedInUser);

            return "cv_created_successfully";
        } else {
            // Obsłuż brak zalogowanego użytkownika
            return "login";  // Przekieruj na stronę logowania
        }
    }
    }


