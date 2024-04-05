package pl.zajavka.controller.api;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pl.zajavka.controller.dto.CvDTO;
import pl.zajavka.controller.dto.mapper.CvMapperDTO;
import pl.zajavka.infrastructure.business.CvService;
import pl.zajavka.infrastructure.business.UserService;
import pl.zajavka.infrastructure.domain.Address;
import pl.zajavka.infrastructure.domain.CV;
import pl.zajavka.infrastructure.domain.SearchRequest;
import pl.zajavka.infrastructure.domain.User;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api")
public class CvRestController {


    private final CvService cvService;
    private final UserService userService;
    private CvMapperDTO cvMapperDTO;

    @PostMapping("/createCV")
    public ResponseEntity<?> createCV(@Valid @RequestBody CvDTO cvDTO,
                                      Authentication authentication) {
        String username = authentication.getName();
        if (username == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }

        User loggedInUser = userService.findByUserName(username);
        if (loggedInUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        if (cvService.existByUser(loggedInUser)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("CV already created");
        }

        CV cv = cvMapperDTO.map(cvDTO);
        cvService.createCV(cv, loggedInUser);

        return ResponseEntity.status(HttpStatus.CREATED).body("CV created successfully");
    }


    @GetMapping("/ShowMyCV")
    public ResponseEntity<?> redirectToShowMyCV(Authentication authentication) {
        String username = authentication.getName();
        User loggedInUser = userService.findByUserName(username);
        CV userCV = cvService.findByUser(loggedInUser);
        if (userCV != null) {
            CvDTO cvDTO = cvMapperDTO.map(userCV);
            return ResponseEntity.ok(cvDTO);
        }

        return ResponseEntity.notFound().build();

    }

    @GetMapping("/showCV/{id}")
    public ResponseEntity<?> showMyCV(@PathVariable Integer id) {
        CV cv = cvService.findById(id);
        if (cv != null) {
            CvDTO cvDTO = cvMapperDTO.map(cv);
            return ResponseEntity.ok(cvDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/updateCv")
    public ResponseEntity<String> updateCv(@Valid @RequestBody CvDTO updateCvDTO, Authentication authentication) {

        String username = authentication.getName();
        User loggedInUser = userService.findByUserName(username);
        CV cv = cvService.findByUser(loggedInUser);

        cv.setName(updateCvDTO.getName());
        cv.setSurname(updateCvDTO.getSurname());
        cv.setSex(updateCvDTO.getSex());
        cv.setDateOfBirth(updateCvDTO.getDateOfBirth());
        cv.setMaritalStatus(updateCvDTO.getMaritalStatus());
        cv.setPhoneNumber(updateCvDTO.getPhoneNumber());
        cv.setContactEmail(updateCvDTO.getContactEmail());
        cv.setSkillsAndTools(updateCvDTO.getSkillsAndTools());
        cv.setProgrammingLanguage(updateCvDTO.getProgrammingLanguage());
        cv.setFollowPosition(updateCvDTO.getFollowPosition());
        cv.setAboutMe(updateCvDTO.getAboutMe());
        cv.setCertificatesOfCourses(updateCvDTO.getCertificatesOfCourses());
        cv.setProjects(updateCvDTO.getProjects());
        cv.setSocialMediaProfil(updateCvDTO.getSocialMediaProfil());
        cv.setEducation(updateCvDTO.getEducation());
        cv.setWorkExperience(updateCvDTO.getWorkExperience());
        cv.setLanguage(updateCvDTO.getLanguage());
        cv.setLanguageLevel(updateCvDTO.getLanguageLevel());
        cv.setHobby(updateCvDTO.getHobby());

        cvService.updateCV(cv);

        return ResponseEntity.status(HttpStatus.OK).body("CV updated successfully");
    }


    @DeleteMapping("/deleteCV/{cvId}")
    public ResponseEntity<String> deleteCV(@PathVariable("cvId") Integer cvId, Authentication authentication) {
        try {
            String username = authentication.getName();
            User loggedInUser = userService.findByUserName(username);

             CV cv = cvService.findById(cvId);
            if (cv != null) {
                Address address = cv.getAddress();
                // Sprawdzenie, czy zalogowany użytkownik jest właścicielem oferty pracy
                if (!cv.getUser().equals(loggedInUser)) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to delete this CV");
                }

                cvService.deleteCVAndSetNullInNotifications(cv, address);
                return ResponseEntity.status(HttpStatus.OK).body("CV deleted successfully");
            }

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("CV not found");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while deleting CV");
        }
    }

    @GetMapping("/searchCV")
    public ResponseEntity<List<CvDTO>> searchCV(@RequestBody SearchRequest searchRequest, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        String keyword = searchRequest.getKeyword();
        String category = searchRequest.getCategory();
        List<CvDTO> searchResultsDTO = cvService.searchCvByKeywordAndCategory(keyword, category);
        return ResponseEntity.ok(searchResultsDTO);
    }

}







