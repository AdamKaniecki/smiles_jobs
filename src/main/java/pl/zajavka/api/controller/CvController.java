package pl.zajavka.api.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.zajavka.api.dto.CvDTO;
import pl.zajavka.api.dto.mapper.CvMapperDTO;
import pl.zajavka.api.dto.mapper.UserMapperDTO;
import pl.zajavka.business.*;
import pl.zajavka.domain.Address;
import pl.zajavka.domain.CV;
import pl.zajavka.domain.User;
import pl.zajavka.infrastructure.database.entity.ProgrammingLanguage;
import pl.zajavka.infrastructure.database.repository.mapper.AddressMapper;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

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
    private EnumService enumService;

    @GetMapping("/CvForm")
    public String cvForm(
            @ModelAttribute("cvDTO") CvDTO cvDTO,
            Model model,
            BindingResult bindingResult,
            Authentication authentication,
            @RequestParam(name = "programmingLanguage", required = false) Set<String> programmingLanguageName
//            @RequestParam(name = "it_specialization", required = false) Set<String> it_specializationName
    )

    {
        if (bindingResult.hasErrors()) {
            return "error";
        }

        String username = authentication.getName();
        if (username != null) {
            User user = userService.findByUserName(username);
            userMapperDTO.map(user);
            model.addAttribute("userDTO", user);
            model.addAttribute("cvDTO", cvDTO);

            Set<ProgrammingLanguage> programmingLanguages = enumService.getAllProgrammingLanguages();
            model.addAttribute("programmingLanguages", programmingLanguages);


//            Set<IT_Specializations> it_specializations = enumService.getAll_it_specializations();
//            model.addAttribute("it_specializations", it_specializations);

            Optional<ProgrammingLanguage> optionalProgrammingLanguage = programmingLanguages.stream()
                        .filter(language -> language.name().equals(programmingLanguageName))
                        .findFirst();
                optionalProgrammingLanguage.ifPresent(language -> model.addAttribute("selectedProgrammingLanguage", language));

//            Optional<IT_Specializations> optionalIT_specializations = it_specializations.stream()
//                    .filter(specialization -> specialization.name().equals(it_specializationName))
//                    .findFirst();
//            optionalIT_specializations.ifPresent(specialization -> model.addAttribute("selected_IT_specialization", specialization));
//

            return "create_cv";
        } else {

            return "login";
        }
    }


    @PostMapping("/createCV")
    @PreAuthorize("hasAuthority('ROLE_CANDIDATE')")
    public String createCV(@Valid @ModelAttribute("cvDTO") CvDTO cvDTO, Model model,
                           BindingResult bindingResult, Authentication authentication,
                           @RequestParam(name = "programmingLanguages", required = false) Set<String> programmingLanguagesNames
//                           @RequestParam(name = "it_specializations", required = false) Set<String> it_specializationNames
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
            Set<ProgrammingLanguage> programmingLanguages = enumService.convertToProgrammingLanguages(programmingLanguagesNames);
//            Set<IT_Specializations> it_specializations = enumService.convertToSpecializations(it_specializationNames);

            cv.setProgrammingLanguages(programmingLanguages);
//            cv.setIt_specializations(it_specializations);

            cvService.createCV(cv, loggedInUser);

            model.addAttribute("cvDTO", cv);
            model.addAttribute("userDTO", loggedInUser);
            return "cv_created_successfully";
        } else {
            return "login";
        }
    }

    @GetMapping("/redirectToShowMyCV")
    public String redirectToShowMyCV() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User loggedInUser = userService.findByUserName(username);

        if (loggedInUser != null) {
            Optional<CV> userCV = cvService.findByUser(loggedInUser);
            if (userCV.isPresent()) {
                Integer cvId = userCV.get().getId();

                return "redirect:/showCV?id=" + cvId;
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
    @PreAuthorize("hasAuthority('ROLE_CANDIDATE')")
    public String redirectToUpdateMyCV() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.getPrincipal() instanceof UserDetails userDetails) {
            String username = userDetails.getUsername();
            User loggedInUser = userService.findByUserName(username);
            if (loggedInUser != null) {
                Optional<CV> userCV = cvService.findByUser(loggedInUser);
                if (userCV.isPresent()) {
                    Integer cvId = userCV.get().getId();

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

            return "cv_not_found";
        }
    }

    @PreAuthorize("hasAuthority('ROLE_CANDIDATE')")
    @PutMapping("/updateCVDone")
    public String updateCv(
            @Valid @ModelAttribute("cvDTO") CvDTO updateCvDTO, Model model) {

        Optional<CV> myCV = cvService.findById(updateCvDTO.getId());
        if (myCV.isPresent()) {
            CV cv = myCV.get();

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
            @ModelAttribute("address") Address updateAddress, Model model, Authentication authentication
    ) {
        User loggedInUser = userService.getLoggedInUser((authentication));
        httpSession.setAttribute("user", loggedInUser);
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
        // Mapuj CvDTO na CV
        CV cvToDelete = cvMapperDTO.map(deleteCvDTO);

        Optional<CV> optionalCV = cvService.findById(cvToDelete.getId());
        if (optionalCV.isPresent()) {
            CV cv = optionalCV.get();
            Address address = cv.getAddress();

            cvService.deleteCVAndSetNullInNotifications(cvToDelete, address);
            model.addAttribute("cvDTO", cvMapperDTO.map(cv));
            return "cv_created_successfully";
        }

        return "home";
    }


    public static String displaySelectedLanguages(Set<String> selectedLanguages) {
        return "Selected Languages: " + String.join(", ", selectedLanguages);
    }

    public static Set<String> addSelectedLanguages(Set<String> currentLanguages, Set<String> newLanguages) {
        Set<String> selectedLanguagesSet = new HashSet<>();

        if (currentLanguages != null) {
            selectedLanguagesSet.addAll(currentLanguages);
        }
        if (newLanguages != null) {
            selectedLanguagesSet.addAll(newLanguages);
        }

        return selectedLanguagesSet;
    }

}









