//package pl.zajavka.api.controller;
//
//import jakarta.servlet.http.HttpSession;
//import lombok.AllArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//import pl.zajavka.api.dto.CvDTO;
//import pl.zajavka.api.dto.UserDTO;
//import pl.zajavka.api.dto.mapper.CvMapperDTO;
//import pl.zajavka.api.dto.mapper.UserMapperDTO;
//import pl.zajavka.business.AddressService;
//import pl.zajavka.business.BusinessCardService;
//import pl.zajavka.business.CvService;
//import pl.zajavka.business.UserService;
//import pl.zajavka.domain.*;
//
//import java.util.List;
//import java.util.Optional;
//
//@Slf4j
//@AllArgsConstructor
//@Controller
//public class CvController {
//
//
//    private HttpSession httpSession;
//    private CvService cvService;
//    private AddressService addressService;
//    private UserService userService;
//    private CvMapperDTO cvMapperDTO;
//    private UserMapperDTO userMapperDTO;
//
//    @GetMapping("/CvForm")
//    public String CvForm(
//            @ModelAttribute("cvDTO") CvDTO cvDTO,
//            Model model) {
//        String username = (String) httpSession.getAttribute("username");
//        if (username != null) {
//            User user = userService.findByUserName(username);
//            userMapperDTO.map(user);
//            model.addAttribute("userDTO", user);
//            model.addAttribute("cvDTO", cvDTO);
//            return "create_cv";
//        } else {
//            // Obsłuż brak zalogowanego użytkownika
//            return "login";  // Przekieruj na stronę logowania
//        }
//    }
//
//    @PostMapping("/createCV")
//    public String createCV(@ModelAttribute("cvDTO") CvDTO cvDTO, Model model) {
////        log.info("Received CV: {}", cv);
//        String username = (String) httpSession.getAttribute("username");
//
//        if (username != null) {
//
//            User loggedInUser = userService.findByUserName(username);
//            if (cvService.existByUser(loggedInUser)) {
//                log.info("co tu sie odwala?: ", loggedInUser);
//                return "cv_already_created";
//
//            }
//            CV cv = cvMapperDTO.map(cvDTO);
////             Adres
//            Address createdAddress = addressService.createAddress(cv.getAddress(), loggedInUser);
////
////            // Reklama
////            Advertisement advertisement = cv.getAdvertisement();
////
//            // CV
//            cv.setAddress(createdAddress);
//            cv.setUser(loggedInUser);
//            cvService.createCV(cv, loggedInUser);
//
//            model.addAttribute("cvDTO", cv);
//            model.addAttribute("userDTO", loggedInUser);
//
//            return "cv_created_successfully";
//        } else {
//            // Obsłuż brak zalogowanego użytkownika
//            return "login";  // Przekieruj na stronę logowania
//        }
//    }
//
//
//    @GetMapping("/redirectToShowMyCV")
//    public String redirectToShowMyCV(HttpSession httpSession) {
//        String username = (String) httpSession.getAttribute("username");
//
//        if (username != null) {
//            User loggedInUser = userService.findByUserName(username);
//
//            if (loggedInUser != null) {
//                // Sprawdź, czy użytkownik ma przypisane CV
//                Optional<CV> userCV = cvService.findByUser(loggedInUser);
//
//                if (userCV.isPresent()) {
//                    Integer cvId = userCV.get().getId();
//
//                    // Przekieruj na endpoint showCV z odpowiednim identyfikatorem
//                    return "redirect:/showCV?id=" + cvId;
//                }
//            }
//        }
//
//        // Obsłuż sytuację, gdy użytkownik nie jest zalogowany, nie ma przypisanego CV lub wystąpił inny problem
//        return "redirect:/";  // Przekieruj na stronę główną lub obsłuż inaczej
//    }
//
//
//    @GetMapping("/showCV")
//    public String showMyCV(@RequestParam Integer id, Model model) {
//        Optional<CV> myCV = cvService.findById(id);
//
//        if (myCV.isPresent()) {
//            CV cv = myCV.get();
//            model.addAttribute("cv", cvMapperDTO.map(cv));
//            model.addAttribute("userDTO", userMapperDTO.map(cv.getUser()));
//            return "show_cv";
//        } else {
//            return "cv_not_found";  // Możesz utworzyć osobny widok dla przypadku, gdy CV nie zostało znalezione
//        }
//    }
//
//    @GetMapping("/cv/{cvId}")
//    public String showCvDetails(@PathVariable Integer cvId, Model model) {
//        Optional<CV> cv = cvService.findById(cvId);
//
//        if (cv.isPresent()) {
//            model.addAttribute("cv", cvMapperDTO.map(cv.get()));
//            return "show_cv";  // Użyj istniejącego widoku show_cv
//        } else {
//            return "cv_not_found";  // Stwórz odpowiedni widok dla przypadku, gdy CV nie zostanie znalezione
//        }
//    }
//
//}
//
//
//
//
//
package pl.zajavka.api.controller;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.zajavka.api.dto.CvDTO;
import pl.zajavka.api.dto.UpdateCvDTO;
import pl.zajavka.api.dto.mapper.CvMapperDTO;
import pl.zajavka.api.dto.mapper.UserMapperDTO;
import pl.zajavka.business.AddressService;
import pl.zajavka.business.CvService;
import pl.zajavka.business.UserService;
import pl.zajavka.domain.*;
import pl.zajavka.infrastructure.database.repository.mapper.AddressMapper;

import java.util.Optional;

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
    private AddressMapper addressMapper;

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
            if (cvService.existByUser(loggedInUser)) {
//                log.info("co tu sie odwala?: ", loggedInUser);
                return "cv_already_created";

            }
            CV cv = cvMapperDTO.map(cvDTO);
//             Adres
            Address createdAddress = addressService.createAddress(cv.getAddress(), loggedInUser);
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


    @GetMapping("/redirectToShowMyCV")
    public String redirectToShowMyCV(HttpSession httpSession) {
        String username = (String) httpSession.getAttribute("username");
        if (username != null) {
            User loggedInUser = userService.findByUserName(username);
            if (loggedInUser != null) {
                // Sprawdź, czy użytkownik ma przypisane CV
                Optional<CV> userCV = cvService.findByUser(loggedInUser);
                if (userCV.isPresent()) {
                    Integer cvId = userCV.get().getId();
                    // Przekieruj na endpoint showCV z odpowiednim identyfikatorem
                    return "redirect:/showCV?id=" + cvId;
                }
            }
        }
        return "cv_not_found";  // Przekieruj na stronę główną lub obsłuż inaczej
    }


    @GetMapping("/showCV")
    public String showMyCV(@RequestParam Integer id, Model model) {
        Optional<CV> myCV = cvService.findById(id);
        if (myCV.isPresent()) {
            CV cv = myCV.get();
            model.addAttribute("cvDTO", cvMapperDTO.map(cv));
            model.addAttribute("userDTO", userMapperDTO.map(cv.getUser()));

            return "show_my_cv";
        } else {
            return "cv_not_found";  // Możesz utworzyć osobny widok dla przypadku, gdy CV nie zostało znalezione
        }
    }


    @GetMapping("/redirectToUpdateMyCV")
    public String redirectToUpdateMyCV(HttpSession httpSession) {
        String username = (String) httpSession.getAttribute("username");
        if (username != null) {
            User loggedInUser = userService.findByUserName(username);
            if (loggedInUser != null) {
                // Sprawdź, czy użytkownik ma przypisane CV
                Optional<CV> userCV = cvService.findByUser(loggedInUser);
                if (userCV.isPresent()) {
                    Integer cvId = userCV.get().getId();
                    // Przekieruj na endpoint showCV z odpowiednim identyfikatorem
                    return "redirect:/updateCvForm?id=" + cvId;
                }
            }
        }
        return "cv_not_found";  // Przekieruj na stronę główną lub obsłuż inaczej
    }

    @GetMapping("/updateCvForm")
    public String updateMyCV(@RequestParam Integer id, Model model) {
        Optional<CV> myCV = cvService.findById(id);
        if (myCV.isPresent()) {
            CV cv = myCV.get();
            model.addAttribute("cvDTO", cvMapperDTO.map(cv));
            model.addAttribute("userDTO", userMapperDTO.map(cv.getUser()));
            model.addAttribute("address", cv.getAddress());

            return "update_cv_form";
        } else {
            return "cv_not_found";  // Możesz utworzyć osobny widok dla przypadku, gdy CV nie zostało znalezione
        }
    }

    @PutMapping("/updateCVDone")
    public String updateCv(@ModelAttribute("cvDTO") CvDTO updateCvDTO, Model model) {
        Optional<CV> myCV = cvService.findById(updateCvDTO.getId());
        if (myCV.isPresent()) {
            CV cv = myCV.get();

            // Aktualizuj pola CV na podstawie danych z formularza
            cv.setName(updateCvDTO.getName());
            cv.setSurname(updateCvDTO.getSurname());
            cv.setSex(updateCvDTO.getSex());
            cv.setDateOfBirth(updateCvDTO.getDateOfBirth());
            cv.setMaritalStatus(updateCvDTO.getMaritalStatus());
            cv.setPhoneNumber(updateCvDTO.getPhoneNumber());
            cv.setContactEmail(updateCvDTO.getContactEmail());
            cv.setEducation(updateCvDTO.getEducation());
            cv.setWorkExperience(updateCvDTO.getWorkExperience());
            cv.setSkills(updateCvDTO.getSkills());
            cv.setLanguage(updateCvDTO.getLanguage());
            cv.setLanguageLevel(updateCvDTO.getLanguageLevel());
            cv.setHobby(updateCvDTO.getHobby());

            cvService.updateCV(cv);

            model.addAttribute("cvDTO", cvMapperDTO.map(cv));
            return "cv_created_successfully";
        } else {
            return "cv_not_found";
        }


    }


    @PutMapping("/updateAddressDone")
    public String updateAddress(
            @ModelAttribute("address") Address updateAddress,
            Model model){
        Optional<Address> myAddress = addressService.findById(updateAddress.getId());
        if (myAddress.isPresent()) {
            Address address = myAddress.get();
            address.setCountry(updateAddress.getCountry());
            address.setCity(updateAddress.getCity());
            address.setStreetAndNumber(updateAddress.getStreetAndNumber());
            addressService.updateAddress(address);
            model.addAttribute("address", address);
//            model.addAttribute("address"),addressMapper.map(address);
            return "cv_created_successfully";
        } else {
            return "cv_not_found";
        }
    }

    @DeleteMapping("/deleteCV")
    public String deleteCV(@ModelAttribute("cvDTO") CvDTO deleteCvDTO, Model model) {
        Optional<CV> myCV = cvService.findById(deleteCvDTO.getId());
        if (myCV.isPresent()) {
            CV cv = myCV.get();
            cvService.deleteCV(cv);
            model.addAttribute("cvDTO", cvMapperDTO.map(cv));
            return "cv_created_successfully";
        } else {
            return "cv_not_found";
        }
    }

}

