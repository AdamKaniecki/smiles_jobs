package pl.zajavka.api.controller;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.zajavka.api.dto.BusinessCardDTO;
import pl.zajavka.api.dto.JobOfferDTO;
import pl.zajavka.api.dto.mapper.BusinessCardMapperDTO;
import pl.zajavka.api.dto.mapper.JobOfferMapperDTO;
import pl.zajavka.api.dto.mapper.UserMapperDTO;
import pl.zajavka.business.*;
import pl.zajavka.domain.*;
import pl.zajavka.infrastructure.database.repository.mapper.CvMapper;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Controller
public class JobOfferController {

    public static final String CREATE_JOB_OFFER = "/create_job_offer";
    private JobOfferService jobOfferService;
    private UserService userService;
    private UserMapperDTO userMapperDTO;
    private JobOfferMapperDTO jobOfferMapperDTO;
    private BusinessCardService businessCardService;
    private BusinessCardMapperDTO businessCardMapperDTO;

    @GetMapping(CREATE_JOB_OFFER)
    public String createJobOfferForm(Model model, Authentication authentication) {

        if (authentication != null && authentication.isAuthenticated()) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String username = userDetails.getUsername();
            model.addAttribute("username", username);

            return "create_job_offer";
        } else {

            return "login";
        }
    }


    @PostMapping("/createJobOffer")
    public String createdJobOffers(
            @ModelAttribute("jobOfferDTO") JobOfferDTO jobOfferDTO,
            Model model,
            Authentication authentication) {

        if (authentication != null && authentication.isAuthenticated()) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User loggedInUser = userService.findByUserName(userDetails.getUsername());
            if (loggedInUser != null) {
                JobOffer jobOffer = jobOfferMapperDTO.map(jobOfferDTO);
                jobOffer.setUser(loggedInUser);
                jobOfferService.create(jobOffer, loggedInUser);

                model.addAttribute("jobOfferDTO", jobOfferDTO);
                model.addAttribute("user", loggedInUser);

                return "job_offer_created_successfully";
            } else {

                return "login";
            }
        } else {

            return "login";
        }
    }

    // TUTAJ POPRAWIĆ NIE KASOWAĆ
    @GetMapping("/jobOffer/{jobOfferId}")
    public String showJobOfferDetails(@PathVariable Integer jobOfferId, Model model) {

        JobOffer jobOffer = jobOfferService.findById(jobOfferId);
        model.addAttribute("jobOfferDTO", jobOfferMapperDTO.map(jobOffer));
        Optional<BusinessCard> businessCard = businessCardService.findByUser2(jobOffer.getUser());

        if (businessCard.isPresent()) {
            model.addAttribute("businessCardDTO", businessCardMapperDTO.map(businessCard.get()));
        } else {
            model.addAttribute("businessCardDTO", new BusinessCardDTO());
        }

        return "job_offer_details";
    }


    @GetMapping("/showMyJobOffers")
    public String showMyJobOffers(Model model, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User loggedInUser = userService.findByUserName(userDetails.getUsername());

        if (loggedInUser != null) {
            List<JobOfferDTO> jobOffersDTO = jobOfferService.findByUser(loggedInUser).stream()
                    .map(jobOfferMapperDTO::map)
                    .collect(Collectors.toList());
            model.addAttribute("jobOffersDTO", jobOffersDTO);

            return "show_my_job_offers";
        } else {

            return "redirect:/login";
        }
    }

    @GetMapping("/redirectToUpdateMyJobOffer")
    public String redirectToUpdateMyJobOffer(HttpSession httpSession) {
        String username = (String) httpSession.getAttribute("username");

        if (username != null) {
            User loggedInUser = userService.findByUserName(username);
            if (loggedInUser != null) {
                Optional<JobOffer> userJobOffer = jobOfferService.findByUser(loggedInUser);
                if (userJobOffer.isPresent()) {
                    Integer jobOfferId = userJobOffer.get().getId();

                    return "redirect:/updateJobOfferForm?id=" + jobOfferId;
                }
            }
        }
        return "jobOffer_not_found";
    }


    @GetMapping("/updateJobOfferForm")
    public String updateMyJobOffer(@RequestParam Integer id, Model model) {
        JobOffer myJobOffer = jobOfferService.findById(id);

        model.addAttribute("jobOfferDTO", jobOfferMapperDTO.map(myJobOffer));
        model.addAttribute("userDTO", userMapperDTO.map(myJobOffer.getUser()));
        // model.addAttribute("address", cv.getAddress());
        return "update_job_offer_form";
    }


    @PutMapping("/updateJobOfferDone")
    public String updateJobOffer(
            @ModelAttribute("jobOfferDTO") JobOfferDTO updateJobOfferDTO,
            Model model) {
        JobOffer jobOffer = jobOfferService.findById(updateJobOfferDTO.getId());

        jobOffer.setCompanyName(updateJobOfferDTO.getCompanyName());
        jobOffer.setPosition(updateJobOfferDTO.getPosition());
        jobOffer.setResponsibilities(updateJobOfferDTO.getResponsibilities());
        jobOffer.setRequiredTechnologies(updateJobOfferDTO.getRequiredTechnologies());
        jobOffer.setBenefits(updateJobOfferDTO.getBenefits());

        jobOfferService.updateJobOffer(jobOffer);

        model.addAttribute("jobOfferDTO", jobOfferMapperDTO.map(jobOffer));

        return "job_offer_created_successfully";
    }


    @DeleteMapping("/deleteJobOffer/{jobOfferId}")
    public String deleteJobOffer(@PathVariable Integer jobOfferId, Model model, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User loggedInUser = userService.findByUserName(userDetails.getUsername());

        if (loggedInUser != null) {
            jobOfferService.deleteJobOfferAndSetNullInNotifications(jobOfferId);
        }

        return "redirect:/showMyJobOffers";
    }

}






