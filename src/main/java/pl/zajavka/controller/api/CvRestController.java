package pl.zajavka.controller.api;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pl.zajavka.controller.dto.CvDTO;
import pl.zajavka.controller.dto.mapper.CvMapperDTO;
import pl.zajavka.infrastructure.business.CvService;
import pl.zajavka.infrastructure.business.EnumService;
import pl.zajavka.infrastructure.business.UserService;
import pl.zajavka.infrastructure.database.entity.ProgrammingLanguage;
import pl.zajavka.infrastructure.domain.Address;
import pl.zajavka.infrastructure.domain.CV;
import pl.zajavka.infrastructure.domain.User;

import java.util.Optional;
import java.util.Set;

@AllArgsConstructor
@RestController
@RequestMapping("/api")
public class CvRestController {


    private final CvService cvService;
    private final UserService userService;
    private final EnumService enumService;


    private CvMapperDTO cvMapperDTO;

    @PostMapping("/createCV")
    public ResponseEntity<?> createCV(@Valid @RequestBody CvDTO cvDTO,
                                      @RequestParam(name = "programmingLanguages", required = false) Set<String> programmingLanguagesNames,
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
        Set<ProgrammingLanguage> programmingLanguages = enumService.convertToProgrammingLanguages(programmingLanguagesNames);
        cv.setProgrammingLanguages(programmingLanguages);

        cvService.createCV(cv, loggedInUser);

        return ResponseEntity.status(HttpStatus.CREATED).body("CV created successfully");
    }
//    curl -i -H "Content-Type: application/json" -X POST http://localhost:8800/users -d "{\"userName\": \"john_doe\",\"email\": \"john.doe@example.com\",\"password\": \"secretpassword123\",\"active\": true,\"roles\": [{\"roleId\": 1}]}"

//    curl -i -H "Content-Type: application/json" -X POST http://localhost:8800/api/createCV -d "{\"name\": \"John\",\"surname\": \"Doe\",\"sex\": \"yes\",\"dateOfBirth\": \"1990-01-01\",\"maritalStatus\": \"Single\",\"phoneNumber\": \"+48 123 456 789\",\"contactEmail\": \"john.doe@example.com\",\"programmingLanguages\": [{\"Java", "Python", "SQL\}]",\"education\": \"Bachelor\",\"workExperience\": \"5 years\",\"skills\": \"Java\",\"language\": \"English\",\"languageLevel\": \"Advanced\",\"userName\": \"john_doe\",\"userName\": \"john_doe\",\"userName\": \"john_doe\",\"roles\": [{\"roleId\": 1}]}"


    @GetMapping("/ShowMyCV")
    public ResponseEntity<?> redirectToShowMyCV(Authentication authentication) {
        String username = authentication.getName();
        User loggedInUser = userService.findByUserName(username);
        if (loggedInUser != null) {
            Optional<CV> userCV = cvService.findByUser(loggedInUser);
            if (userCV.isPresent()) {
                CV cv = userCV.get();
                CvDTO cvDTO = cvMapperDTO.map(cv);
                return ResponseEntity.ok(cvDTO);
            }
        }
        // CV not found or user not logged in
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/showCV/{id}")
    public ResponseEntity<?> showMyCV(@PathVariable Integer id) {
        Optional<CV> cvOpt = cvService.findById(id);

        if (cvOpt.isPresent()) {
            CV cv = cvOpt.get();
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
            CV cv = cvService.findByUser2(loggedInUser);

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

            return ResponseEntity.status(HttpStatus.OK).body("CV updated successfully");
        }

    @DeleteMapping("/deleteCV/{id}")
    public ResponseEntity<String> deleteCV(@PathVariable("id") Integer cvId) {
        Optional<CV> optionalCV = cvService.findById(cvId);
        if (optionalCV.isPresent()) {
            CV cv = optionalCV.get();
            Address address = cv.getAddress();

            cvService.deleteCVAndSetNullInNotifications(cv, address);
            return ResponseEntity.status(HttpStatus.OK).body("CV deleted successfully");
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("CV not found");
    }

}







