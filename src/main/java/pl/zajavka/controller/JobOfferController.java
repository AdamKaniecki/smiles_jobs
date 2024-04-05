package pl.zajavka.controller;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.zajavka.controller.dto.BusinessCardDTO;
import pl.zajavka.controller.dto.CvDTO;
import pl.zajavka.controller.dto.JobOfferDTO;
import pl.zajavka.controller.dto.mapper.BusinessCardMapperDTO;
import pl.zajavka.controller.dto.mapper.JobOfferMapperDTO;
import pl.zajavka.controller.dto.mapper.UserMapperDTO;
import pl.zajavka.infrastructure.business.BusinessCardService;
import pl.zajavka.infrastructure.business.JobOfferService;
import pl.zajavka.infrastructure.business.UserService;
import pl.zajavka.infrastructure.database.repository.BusinessCardRepository;
import pl.zajavka.infrastructure.database.repository.JobOfferRepository;
import pl.zajavka.infrastructure.domain.BusinessCard;
import pl.zajavka.infrastructure.domain.JobOffer;
import pl.zajavka.infrastructure.domain.User;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Controller
public class JobOfferController {


    private JobOfferService jobOfferService;
    private UserService userService;
    private UserMapperDTO userMapperDTO;
    private JobOfferMapperDTO jobOfferMapperDTO;
    private BusinessCardService businessCardService;
    private BusinessCardMapperDTO businessCardMapperDTO;


    @GetMapping("/JobOfferForm")
    public String jobOfferForm(
            @ModelAttribute("jobOfferDTO") JobOfferDTO jobOfferDTO,
            Model model,
            BindingResult bindingResult,
            Authentication authentication
    ) {
        if (bindingResult.hasErrors()) {
            return "error";
        }
        String username = authentication.getName();
        userService.findByUserName(username);
        model.addAttribute("jobOfferDTO", jobOfferDTO);

        return "create_job_offer";
    }


    @PostMapping("/createJobOffer")
    public String createdJobOffers(
            @ModelAttribute("jobOfferDTO") JobOfferDTO jobOfferDTO,
            Model model,
            Authentication authentication) {
        String username = authentication.getName();
        User loggedInUser = userService.findByUserName(username);
        JobOffer jobOffer = jobOfferMapperDTO.map(jobOfferDTO);
        jobOffer.setUser(loggedInUser);

        jobOfferService.create(jobOffer, loggedInUser);

        model.addAttribute("jobOfferDTO", jobOfferDTO);
//        model.addAttribute("userDTO", loggedInUser);

        return "job_offer_created_successfully";
    }


    @GetMapping("/jobOffer/{jobOfferId}")
    public String showJobOfferDetails(@PathVariable Integer jobOfferId, Model model) {
        JobOffer jobOffer = jobOfferService.findById(jobOfferId);
        model.addAttribute("jobOfferDTO", jobOfferMapperDTO.map(jobOffer));
        BusinessCard businessCard = businessCardService.findByUser(jobOffer.getUser());
        if (businessCard != null) {
            model.addAttribute("businessCardDTO", businessCardMapperDTO.map(businessCard));
        } else {
            model.addAttribute("businessCardDTO", new BusinessCardDTO());
        }
        return "job_offer_details";
    }


    @GetMapping("/showMyJobOffers")
    public String showMyJobOffers(Model model, Authentication authentication) {
        String username = authentication.getName();
        User loggedInUser = userService.findByUserName(username);

        if (loggedInUser != null) {
            List<JobOfferDTO> jobOffersDTO = jobOfferService.findListByUser(loggedInUser).stream()
                    .map(jobOfferMapperDTO::map)
                    .collect(Collectors.toList());
            model.addAttribute("jobOffersDTO", jobOffersDTO);

            return "show_my_job_offers";
        } else {

            return "redirect:/login";
        }
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
        jobOffer.setExperience(updateJobOfferDTO.getExperience());
        jobOffer.setSalaryMin(updateJobOfferDTO.getSalaryMin());
        jobOffer.setSalaryMax(updateJobOfferDTO.getSalaryMax());
        jobOffer.setJobLocation(updateJobOfferDTO.getJobLocation());
        jobOffer.setTypeOfContract(updateJobOfferDTO.getTypeOfContract());
        jobOffer.setTypeOfWork(updateJobOfferDTO.getTypeOfWork());
        jobOffer.setJobDescription(updateJobOfferDTO.getJobDescription());
        jobOffer.setRequiredLanguage(updateJobOfferDTO.getRequiredLanguage());
        jobOffer.setRequiredLanguageLevel(updateJobOfferDTO.getRequiredLanguageLevel());
        jobOffer.setBenefits(updateJobOfferDTO.getBenefits());

        jobOfferService.updateJobOffer(jobOffer);

        model.addAttribute("jobOfferDTO", jobOfferMapperDTO.map(jobOffer));

        return "job_offer_created_successfully";
    }


    @DeleteMapping("/deleteJobOffer/{jobOfferId}")
    public String deleteJobOffer(@PathVariable Integer jobOfferId, Authentication authentication) {
        String username = authentication.getName();
        User loggedInUser = userService.findByUserName(username);

        if (loggedInUser != null) {
            jobOfferService.deleteJobOfferAndSetNullInNotifications(jobOfferId);
        }

        return "redirect:/showMyJobOffers";
    }

}






