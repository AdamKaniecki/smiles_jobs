package pl.zajavka.controller.api;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pl.zajavka.controller.dto.JobOfferDTO;
import pl.zajavka.controller.dto.mapper.JobOfferMapperDTO;
import pl.zajavka.infrastructure.business.JobOfferService;
import pl.zajavka.infrastructure.business.UserService;
import pl.zajavka.infrastructure.domain.JobOffer;
import pl.zajavka.infrastructure.domain.SearchRequest;
import pl.zajavka.infrastructure.domain.User;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
@RequestMapping("/api")
public class JobOfferRestController {

    private UserService userService;
    private JobOfferMapperDTO jobOfferMapperDTO;
    private JobOfferService jobOfferService;


    @PostMapping("/createJobOffer")
    public ResponseEntity<String> createJobOffer(@RequestBody JobOfferDTO jobOfferDTO, Authentication authentication) {
        String username = authentication.getName();
        if (username == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }
        User loggedInUser = userService.findByUserName(username);
        if (loggedInUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        JobOffer jobOffer = jobOfferMapperDTO.map(jobOfferDTO);

        jobOfferService.create(jobOffer, loggedInUser);

        return ResponseEntity.status(HttpStatus.CREATED).body("Job offer created successfully");
    }

    @GetMapping("/ShowMyJobOffers")
    public ResponseEntity<?> ShowMyJobOffers(Authentication authentication) {
        String username = authentication.getName();
        User loggedInUser = userService.findByUserName(username);

        if (loggedInUser != null) {
            List<JobOfferDTO> jobOfferDTOs = jobOfferService.findListByUser(loggedInUser).stream()
                    .map(jobOfferMapperDTO::map)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(jobOfferDTOs);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Job offers Not Found");
    }

    @GetMapping("/showJobOffer/{id}")
    public ResponseEntity<?> showMyJobOffer(@PathVariable Integer id) {
       JobOffer jobOffer = jobOfferService.findById(id);

        if (jobOffer != null) {
            JobOfferDTO jobOfferDTO = jobOfferMapperDTO.map(jobOffer);
            return ResponseEntity.ok(jobOfferDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/deleteJobOffer/{id}")
    public ResponseEntity<String> deleteJobOffer(@PathVariable("id") Integer jobOfferId, Authentication authentication) {
        try {
            String username = authentication.getName();
            User loggedInUser = userService.findByUserName(username);

             JobOffer jobOffer = jobOfferService.findById(jobOfferId);
            if (jobOffer != null) {
                // Sprawdzenie, czy zalogowany użytkownik jest właścicielem oferty pracy
                if (!jobOffer.getUser().equals(loggedInUser)) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to delete this job offer");
                }
                jobOfferService.deleteJobOfferAndSetNullInNotifications(jobOffer.getId());
                return ResponseEntity.status(HttpStatus.OK).body("JobOffer deleted successfully");
            }

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Job Offer not found");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while deleting job offer");
        }
    }


    @PutMapping("/updateJobOffer")
    public ResponseEntity<String> updateJobOffer(@RequestBody JobOfferDTO updateJobOfferDTO, Authentication authentication) {
        try {
            String username = authentication.getName();
            User loggedInUser = userService.findByUserName(username);

            JobOffer jobOffer = jobOfferService.findById(updateJobOfferDTO.getId());

            // Sprawdzenie, czy zalogowany użytkownik jest właścicielem oferty pracy
            if (!jobOffer.getUser().equals(loggedInUser)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("You are not authorized to update this job offer");
            }

            jobOffer.setCompanyName(updateJobOfferDTO.getCompanyName());
            jobOffer.setPosition(updateJobOfferDTO.getPosition());
            jobOffer.setResponsibilities(updateJobOfferDTO.getResponsibilities());
            jobOffer.setRequiredTechnologies(updateJobOfferDTO.getRequiredTechnologies());
            jobOffer.setBenefits(updateJobOfferDTO.getBenefits());

            jobOfferService.updateJobOffer(jobOffer);

            return ResponseEntity.ok("Job offer updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while updating the job offer");
        }
    }

    @GetMapping("/searchJobOffers")
    public ResponseEntity<List<JobOfferDTO>> searchJobOffers(
            @RequestBody SearchRequest searchRequest,
            Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        String keyword = searchRequest.getKeyword();
        String category = searchRequest.getCategory();

        List<JobOffer> searchResults;
        if ("salaryMin".equals(category)) {
            try {
                BigDecimal salaryMinValue = BigDecimal.valueOf(Double.parseDouble(keyword));
                searchResults = jobOfferService.searchJobOffersBySalary(category, salaryMinValue);
            } catch (NumberFormatException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }
        } else {
            searchResults = jobOfferService.searchJobOffersByKeywordAndCategory(keyword, category);
        }

        List<JobOfferDTO> searchResultsDTO = searchResults.stream()
                .map(jobOfferMapperDTO::map)
                .collect(Collectors.toList());

        return ResponseEntity.ok(searchResultsDTO);
    }




}


