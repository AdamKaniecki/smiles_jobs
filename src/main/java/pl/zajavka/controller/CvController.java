package pl.zajavka.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.zajavka.controller.dto.CvDTO;
import pl.zajavka.controller.dto.UserDTO;
import pl.zajavka.controller.dto.mapper.CvMapperDTO;
import pl.zajavka.controller.dto.mapper.UserMapperDTO;
import pl.zajavka.infrastructure.business.AddressService;
import pl.zajavka.infrastructure.business.CvService;
import pl.zajavka.infrastructure.business.UserService;
import pl.zajavka.infrastructure.database.repository.AddressRepository;
import pl.zajavka.infrastructure.database.repository.CvRepository;
import pl.zajavka.infrastructure.domain.Address;
import pl.zajavka.infrastructure.domain.CV;
import pl.zajavka.infrastructure.domain.User;

import java.util.Optional;

@Slf4j
@AllArgsConstructor
@Controller
public class CvController {


    private CvService cvService;
    private AddressService addressService;
    private UserService userService;
    private CvMapperDTO cvMapperDTO;
    private UserMapperDTO userMapperDTO;


    @GetMapping("/CvForm")
    public String cvForm(
            @ModelAttribute("cvDTO") CvDTO cvDTO,
            Model model,
            BindingResult bindingResult,
            Authentication authentication
    ) {
        if (bindingResult.hasErrors()) {
            return "error";
        }

        String username = authentication.getName();
        if (username != null) {
            User user = userService.findByUserName(username);
            userMapperDTO.map(user);
            model.addAttribute("userDTO", user);
            model.addAttribute("cvDTO", cvDTO);


            return "create_cv";
        } else {

            return "login";
        }
    }


    @PostMapping("/createCV")
    @PreAuthorize("hasAuthority('ROLE_CANDIDATE')")
    public String createCV(@Valid @ModelAttribute("cvDTO") CvDTO cvDTO, Model model,
                           BindingResult bindingResult, Authentication authentication

//                           @RequestParam("photo") MultipartFile photo
    ) {
        if (bindingResult.hasErrors()) {
            return "error";
        }
        String username = authentication.getName();
        if (username != null) {
            User loggedInUser = userService.findByUserName(username);
            if (cvService.existByUser(loggedInUser)) {
                return "cv_already_created";
            }

            CV cv = cvMapperDTO.map(cvDTO);
            cvService.createCV(cv, loggedInUser);

            model.addAttribute("cvDTO", cv);
            model.addAttribute("userDTO", loggedInUser);
            return "cv_created_successfully";
        } else {
            return "login";
        }
    }


    @GetMapping("/ShowMyCV")
    public String redirectToShowMyCV(
            Authentication authentication,
            Model model
    ) {
        String username = authentication.getName();
        User loggedInUser = userService.findByUserName(username);
        if (loggedInUser != null) {
            CV userCV = cvService.findByUser(loggedInUser);
            if (userCV != null) {
                CvDTO cvDTO = cvMapperDTO.map(userCV);
                model.addAttribute("cvDTO", cvDTO);
                return "show_my_cv";

            }
        }

        return "cv_not_found";  // Przekieruj na stronę główną lub obsłuż inaczej
    }


    @GetMapping("/showCV")
    public String showMyCV(@RequestParam Integer id, Model model) {

        CV cv = cvService.findById(id);

        if (cv != null) {
            CvDTO cvDTO = cvMapperDTO.map(cv);
            model.addAttribute("cvDTO", cvDTO);
            model.addAttribute("userDTO", userMapperDTO.map(cv.getUser()));
            return "redirect:/showCV?id=" + id;

        } else {

            return "cv_not_found";
        }
    }


    @GetMapping("/updateCvForm")
    public String updateMyCV(Authentication authentication,
                             Model model) {
        String username = authentication.getName();
        User loggedInUser = userService.findByUserName(username);
        UserDTO userDTO = userMapperDTO.map(loggedInUser);

        if (loggedInUser != null) {
            CV userCV = cvService.findByUser(loggedInUser);
            if (userCV != null) {
                CvDTO cvDTO = cvMapperDTO.map(userCV);

                model.addAttribute("cvDTO", cvDTO);
                model.addAttribute("userDTO", userDTO);
                model.addAttribute("address", userCV.getAddress());

                return "update_cv_form";
            }
        }
        return "cv_not_found";
    }


    @PreAuthorize("hasAuthority('ROLE_CANDIDATE')")
    @PutMapping("/updateCVDone")
    public String updateCv(
            @Valid @ModelAttribute("cvDTO") CvDTO updateCvDTO, Model model) {

        CV cv = cvService.findById(updateCvDTO.getId());
        if (cv != null) {


            cv.setName(updateCvDTO.getName());
            cv.setSurname(updateCvDTO.getSurname());
            cv.setSex(updateCvDTO.getSex());
            cv.setDateOfBirth(updateCvDTO.getDateOfBirth());
            cv.setMaritalStatus(updateCvDTO.getMaritalStatus());
            cv.setPhoneNumber(updateCvDTO.getPhoneNumber());
            cv.setContactEmail(updateCvDTO.getContactEmail());
            cv.setEducation(updateCvDTO.getEducation());
            cv.setWorkExperience(updateCvDTO.getWorkExperience());
            cv.setSocialMediaProfil(updateCvDTO.getSocialMediaProfil());
            cv.setProjects(updateCvDTO.getProjects());
            cv.setAboutMe(updateCvDTO.getAboutMe());
            cv.setCertificatesOfCourses(updateCvDTO.getCertificatesOfCourses());
            cv.setProgrammingLanguage(updateCvDTO.getProgrammingLanguage());
            cv.setSkillsAndTools(updateCvDTO.getSkillsAndTools());
            cv.setLanguage(updateCvDTO.getLanguage());
            cv.setLanguageLevel(updateCvDTO.getLanguageLevel());
            cv.setHobby(updateCvDTO.getHobby());

            cvService.updateCV(cv);

            model.addAttribute("cvDTO", cvMapperDTO.map(cv));

            return "cv_update successfully";
        } else {

            return "cv_not_found";
        }


    }

    @PutMapping("/updateAddressDone")
    public String updateAddress(
            @ModelAttribute("address") Address updateAddress, Model model, Authentication authentication
    ) {
        String username = authentication.getName();
        User loggedInUser = userService.findByUserName(username);

        Address address = addressService.findById(updateAddress.getId());

        address.setCountry(updateAddress.getCountry());
        address.setCity(updateAddress.getCity());
        address.setStreetAndNumber(updateAddress.getStreetAndNumber());
        addressService.updateAddress(address);

        model.addAttribute("address", address);

        return addressService.determineRoleSpecificString(loggedInUser);
    }

    @DeleteMapping("/deleteCV")
    public String deleteCV(@ModelAttribute("cvDTO") CvDTO deleteCvDTO, Model model) {
        CV cvToDelete = cvMapperDTO.map(deleteCvDTO);
        cvService.findById(cvToDelete.getId());
        if (cvToDelete != null) {
            Address address = cvToDelete.getAddress();
            cvService.deleteCVAndSetNullInNotifications(cvToDelete, address);
            model.addAttribute("cvDTO", cvMapperDTO.map(cvToDelete));
            return "cv_deleted_successfully";
        }

        return "home";
    }

    @GetMapping("/cv/{cvId}")
    public String showCvDetails(@PathVariable Integer cvId, Model model) {
           CV cv = cvService.findById(cvId);
        if (cv != null) {
            model.addAttribute("cvDTO", cvMapperDTO.map(cv));

            return "show_cv";
        } else {
            return "cv_not_found";
        }
    }


}









