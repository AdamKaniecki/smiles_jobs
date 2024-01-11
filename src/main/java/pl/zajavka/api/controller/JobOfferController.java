package pl.zajavka.api.controller;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.zajavka.api.dto.JobOfferDTO;
import pl.zajavka.api.dto.mapper.JobOfferMapperDTO;
import pl.zajavka.api.dto.mapper.UserMapperDTO;
import pl.zajavka.business.CvService;
import pl.zajavka.business.JobOfferService;
import pl.zajavka.business.NotificationService;
import pl.zajavka.business.UserService;
import pl.zajavka.domain.CV;
import pl.zajavka.domain.JobOffer;
import pl.zajavka.domain.Notification;
import pl.zajavka.domain.User;
import pl.zajavka.infrastructure.database.repository.mapper.CvMapper;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@AllArgsConstructor
@Controller
public class JobOfferController {

    public static final String CREATE_JOB_OFFER = "/create_job_offer";
    private HttpSession httpSession;
    private JobOfferService jobOfferService;
    private UserService userService;
    private UserMapperDTO userMapperDTO;
    private JobOfferMapperDTO jobOfferMapperDTO;
    private CvService cvService;
    private NotificationService notificationService;
    private CvMapper cvMapper;

    @GetMapping(CREATE_JOB_OFFER)
    public String createJobOfferForm(Model model) {
        String username = (String) httpSession.getAttribute("username");
        if (username != null) {
            model.addAttribute("username", username);
            return "create_job_offer";
        } else {
            // Obsłuż brak zalogowanego użytkownika
            return "login";  // Przekieruj na stronę logowania
        }
    }


    @PostMapping("/createJobOffer")
    public String createdJobOffers(
            @ModelAttribute("jobOfferDTO") JobOfferDTO jobOfferDTO,
            Model model) {

        String username = (String) httpSession.getAttribute("username");

        if (username != null) {
            // Uzyskaj informacje o użytkowniku, takie jak jego identyfikator
            User loggedInUser = userService.findByUserName(username);
//            Integer userId = loggedInUser.getId(); // Załóżmy, że masz dostęp do identyfikatora użytkownika

            if (loggedInUser != null) {
                JobOffer jobOffer = jobOfferMapperDTO.map(jobOfferDTO);
                jobOffer.setUser(loggedInUser);

                // Przekaż identyfikator użytkownika do metody create w serwisie oferty pracy
                jobOfferService.create(jobOffer, loggedInUser);

                model.addAttribute("jobOfferDTO", jobOfferDTO);
                model.addAttribute("user", loggedInUser);

                return "job_offer_created_successfully";
            } else {
                // Obsłuż brak zalogowanego użytkownika
                return "login";  // Przekieruj na stronę logowania
            }
        } else {
            // Obsłuż brak zalogowanego użytkownika
            return "login";  // Przekieruj na stronę logowania
        }
    }


    @GetMapping("/showMyJobOffers")
    public String showMyJobOffers(Model model, HttpSession httpSession) {
        String username = (String) httpSession.getAttribute("username");

        if (username != null) {
            User loggedInUser = userService.findByUserName(username);

            if (loggedInUser != null) {
                List<JobOffer> userJobOffers = jobOfferService.findListByUser(loggedInUser);

                if (userJobOffers != null && !userJobOffers.isEmpty()) {
                    model.addAttribute("jobOffersDTO", userJobOffers);
                } else {
                    model.addAttribute("jobOffersDTO", List.of()); // Pusta lista, jeśli brak ofert
                }

                model.addAttribute("userDTO", userMapperDTO.map(loggedInUser));

                return "show_my_job_offers";
            }
        }

        // Obsłuż sytuację, gdy użytkownik nie jest zalogowany, nie ma przypisanej oferty pracy lub wystąpił inny problem
        return "{user}/company_portal";  // Przekieruj na stronę główną lub obsłuż inaczej
    }

    @GetMapping("/jobOffer/{jobOfferId}")
    public String showJobOfferDetails(@PathVariable Integer jobOfferId, Model model, HttpSession httpSession) {
        String username = (String) httpSession.getAttribute("username");

        if (username != null) {
            User loggedInUser = userService.findByUserName(username);
            // Pobierz szczegóły oferty pracy jako Optional
            Optional<JobOffer> optionalJobOffer = jobOfferService.findById(jobOfferId);
            if (optionalJobOffer.isPresent()) {
                JobOffer jobOffer = optionalJobOffer.get();
                model.addAttribute("jobOfferDTO", jobOffer);
                return "job_offer_details";
            }
        }

        // Obsłuż sytuację, gdy użytkownik nie jest zalogowany lub oferta pracy nie istnieje
        return "redirect:/showMyJobOffers";  // Przekieruj na listę ofert pracy użytkownika
    }

    @GetMapping("/redirectToUpdateMyJobOffer")
    public String redirectToUpdateMyJobOffer(HttpSession httpSession) {
        String username = (String) httpSession.getAttribute("username");
        if (username != null) {
            User loggedInUser = userService.findByUserName(username);
            if (loggedInUser != null) {
                // Sprawdź, czy użytkownik ma przypisane CV
                Optional<JobOffer> userJobOffer = jobOfferService.findByUser(loggedInUser);
                if (userJobOffer.isPresent()) {
                    Integer jobOfferId = userJobOffer.get().getId();

                    return "redirect:/updateJobOfferForm?id=" + jobOfferId;
                }
            }
        }
        return "jobOffer_not_found";  // Przekieruj na stronę główną lub obsłuż inaczej
    }

    @GetMapping("/updateJobOfferForm")
    public String updateMyJobOffer(@RequestParam Integer id, Model model) {
        Optional<JobOffer> myJobOffer = jobOfferService.findById(id);
        if (myJobOffer.isPresent()) {
            JobOffer jobOffer = myJobOffer.get();
            model.addAttribute("jobOfferDTO", jobOfferMapperDTO.map(jobOffer));
            model.addAttribute("userDTO", userMapperDTO.map(jobOffer.getUser()));
//            model.addAttribute("address", cv.getAddress());
            return "update_job_offer_form";
        } else {
            return "job_offer_not_found";  // Możesz utworzyć osobny widok dla przypadku, gdy CV nie zostało znalezione
        }
    }

    @PutMapping("/updateJobOfferDone")
    public String updateJobOffer(
            @ModelAttribute("jobOfferDTO") JobOfferDTO updateJobOfferDTO,
            Model model) {
        Optional<JobOffer> myJobOffer = jobOfferService.findById(updateJobOfferDTO.getId());
        if (myJobOffer.isPresent()) {
            JobOffer jobOffer = myJobOffer.get();
            jobOffer.setCompanyName(updateJobOfferDTO.getCompanyName());
            jobOffer.setPosition(updateJobOfferDTO.getPosition());
            jobOffer.setResponsibilities(updateJobOfferDTO.getResponsibilities());
            jobOffer.setRequiredTechnologies(updateJobOfferDTO.getRequiredTechnologies());
            jobOffer.setBenefits(updateJobOfferDTO.getBenefits());
            // Aktualizuj pola CV na podstawie danych z formularza

            jobOfferService.updateJobOffer(jobOffer);

            model.addAttribute("jobOfferDTO", jobOfferMapperDTO.map(jobOffer));
            return "job_offer_created_successfully";
        } else {
            return "job_offer_not_found";
        }

    }

    @DeleteMapping("/deleteJobOffer/{jobOfferId}")
    public String deleteJobOffer(@PathVariable Integer jobOfferId, Model model, HttpSession httpSession) {
        String username = (String) httpSession.getAttribute("username");

        if (username != null) {
            User loggedInUser = userService.findByUserName(username);
            if (loggedInUser != null) {
                Optional<JobOffer> optionalJobOffer = jobOfferService.findById(jobOfferId);
                if (optionalJobOffer.isPresent() && optionalJobOffer.get().getUser().equals(loggedInUser)) {
                    jobOfferService.deleteJobOffer(jobOfferId);
                    return "redirect:/showMyJobOffers";
                }
            }
        }
        return "redirect:/showMyJobOffers";
    }


    @PostMapping("/sendCV")
    public String sendCV(@RequestParam("jobOfferId") Integer jobOfferId, Model model, HttpSession httpSession) {
        System.out.println("czy ty tu wchodzisz?");
        String username = (String) httpSession.getAttribute("username");

        if (username != null) {
            User loggedInUser = userService.findByUserName(username);
            System.out.println("czy ty tu wchodzisz?2");
            if (loggedInUser != null) {
                Optional<JobOffer> optionalJobOffer = jobOfferService.findById(jobOfferId);
                if (optionalJobOffer.isPresent()) {
                    System.out.println("czy ty tu wchodzisz?3");
                    JobOffer jobOffer = optionalJobOffer.get();

                    System.out.println("czy ty tu wchodzisz?4");
                    Optional<CV> myCV = cvService.findByUser(loggedInUser);
                    if (myCV.isPresent()) {
                        System.out.println("czy ty tu wchodzisz?5");
                        CV cv = myCV.get();
                        // Utwórz obiekt Notification
                        System.out.println("czy ty tu wchodzisz?6");
                        User adresat = jobOffer.getUser();
                        Notification notification = notificationService.createNotification(jobOffer, cv, loggedInUser, adresat);

                        // Zapisz zmiany w użytkowniku
                        userService.save(loggedInUser);
                        userService.save(adresat);

                        return "candidate_created_successfully";
                    } else {
                        // Obsłuż sytuację, gdy użytkownik nie ma przypisanego CV
                        return "cv_not_found"; // Przekieruj na stronę główną lub obsłuż inaczej
                    }
                }
            }
        }

        return "redirect:/"; // Przekieruj w przypadku problemu
    }




    @PostMapping("/arrangeInterview")
    public String arrangeInterview(
            @RequestParam("jobOfferId") Integer jobOfferId,
            @RequestParam("cvId") Integer cvId,
            @RequestParam("notificationId") Integer notificationId,
            @RequestParam("proposedDateTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime proposedDateTime,
            HttpSession httpSession
    ) {
        String username = (String) httpSession.getAttribute("username");
        if (username != null) {
            User loggedInUser = userService.findByUserName(username);
            if (loggedInUser != null) {
                Optional<JobOffer> optionalJobOffer = jobOfferService.findByUser(loggedInUser);

                if (optionalJobOffer.isPresent()) {
                    JobOffer jobOffer = optionalJobOffer.get();
                    Optional<CV> myCV = cvService.findById(cvId);
                    if (myCV.isPresent()) {
                        CV cv = myCV.get();
                        // Pobierz encję Notification
                        Notification notification = notificationService.findById(notificationId);

                        // Zaktualizuj encję Notification i zapisz zmiany
                        notificationService.updateNotificationAndUsers(notification, loggedInUser, cv.getUser(), proposedDateTime);

                        return "job_offer_created_successfully";
                    }
                }
            }
        }
        return "home";
    }

//    @PostMapping("/changeDateTime")
//    public String changeDateTime(@RequestParam Integer notificationId) {
//        notificationService.changeCompanyMessage(notificationId, "Proszę o wybranie innego terminu");
//        return "redirect:/candidate_portal"; // Dodaj odpowiednią ścieżkę przekierowania
//    }
@PostMapping("/meetingDate")
public String changeTermin(
        @RequestParam("notificationId") Integer notificationId,
        @RequestParam("jobOfferId") Integer jobOfferId,
//        @RequestParam("proposedDateTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime proposedDateTime,
        HttpSession httpSession
) {
    String username = (String) httpSession.getAttribute("username");
    if (username != null) {
        User loggedInUser = userService.findByUserName(username);
        System.out.println("czy ty tu wchodzisz?2");
        if (loggedInUser != null) {
            Optional<JobOffer> optionalJobOffer = jobOfferService.findById(jobOfferId);
            if (optionalJobOffer.isPresent()) {
                System.out.println("czy ty tu wchodzisz?3");
                JobOffer jobOffer = optionalJobOffer.get();
                Notification notification = notificationService.findById(notificationId);
                User adresat = jobOffer.getUser();
                // Tutaj możesz dodać kod do zmiany pola companyMessage
                notificationService.changeMeetingDate(notification, loggedInUser, adresat);
                return "candidate_created_successfully";
            }
        }
    }
    return "home";
}



}






