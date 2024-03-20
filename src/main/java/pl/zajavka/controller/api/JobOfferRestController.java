package pl.zajavka.controller.api;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pl.zajavka.controller.dto.CvDTO;
import pl.zajavka.controller.dto.JobOfferDTO;
import pl.zajavka.controller.dto.mapper.CvMapperDTO;
import pl.zajavka.controller.dto.mapper.JobOfferMapperDTO;
import pl.zajavka.infrastructure.business.JobOfferService;
import pl.zajavka.infrastructure.business.UserService;
import pl.zajavka.infrastructure.domain.Address;
import pl.zajavka.infrastructure.domain.CV;
import pl.zajavka.infrastructure.domain.JobOffer;
import pl.zajavka.infrastructure.domain.User;

import java.util.List;
import java.util.Optional;
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
        Optional<JobOffer> jobOfferOpt = jobOfferService.findById2(id);

        if (jobOfferOpt.isPresent()) {
            JobOffer jobOffer = jobOfferOpt.get();
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

            Optional<JobOffer> optionalJobOffer = jobOfferService.findById2(jobOfferId);
            if (optionalJobOffer.isPresent()) {
                JobOffer jobOffer = optionalJobOffer.get();

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

    @PutMapping("/updateJobOffer/{jobOfferId}")
    public ResponseEntity<String> updateJobOffer(
            @PathVariable Integer jobOfferId,
            @RequestBody JobOfferDTO jobOfferDTO,
            Authentication authentication) {

        try {
            String username = authentication.getName();
            User loggedInUser = userService.findByUserName(username);

            Optional<JobOffer> optionalJobOffer = jobOfferService.findByUser(loggedInUser);
            if (optionalJobOffer.isPresent()) {
                JobOffer jobOffer = optionalJobOffer.get();

                // Sprawdzenie, czy zalogowany użytkownik jest właścicielem oferty pracy
                if (!jobOffer.getUser().equals(loggedInUser)) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to update this job offer");
                }
                jobOffer.setCompanyName(jobOfferDTO.getCompanyName());
                jobOffer.setBenefits(jobOfferDTO.getBenefits());
                jobOffer.setPosition(jobOfferDTO.getPosition());
                jobOffer.setRequiredTechnologies(jobOfferDTO.getRequiredTechnologies());
                jobOffer.setResponsibilities(jobOfferDTO.getResponsibilities());

                jobOfferService.updateJobOffer(jobOffer);

                return ResponseEntity.status(HttpStatus.OK).body("JobOffer update successfully");
            }

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Job Offer not found");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while updating job offer");
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

}


