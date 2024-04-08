package pl.zajavka.controller;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.zajavka.infrastructure.business.CvService;
import pl.zajavka.infrastructure.business.JobOfferService;
import pl.zajavka.infrastructure.business.NotificationService;
import pl.zajavka.infrastructure.business.UserService;
import pl.zajavka.infrastructure.domain.CV;
import pl.zajavka.infrastructure.domain.JobOffer;
import pl.zajavka.infrastructure.domain.Notification;
import pl.zajavka.infrastructure.domain.User;

import java.time.LocalDateTime;

@AllArgsConstructor
@Controller
public class CommunicationController {


    private UserService userService;
    private JobOfferService jobOfferService;
    private CvService cvService;
    private NotificationService notificationService;



    @PostMapping("/sendCV")
    @Transactional
    public String sendCV(@RequestParam("jobOfferId") Integer jobOfferId, Authentication authentication) {
        try {
            String username = authentication.getName();
            User loggedInUser = userService.findByUserName(username);
            JobOffer jobOffer = jobOfferService.findById(jobOfferId);
            CV cv = cvService.findByUser2(loggedInUser);
            User adresat = jobOffer.getUser();

            // Sprawdź, czy CV użytkownika istnieje
            if (cv == null) {
                // Jeśli CV nie istnieje, zwróć widok cv_not_found
                return "cv_not_found";
            }

            if (notificationService.hasUserSentCVToJobOffer(loggedInUser, jobOffer)) {
                return "cv_already_sent";
            } else {
                notificationService.createNotification(jobOffer, cv, loggedInUser, adresat);
                userService.save(loggedInUser);
                userService.save(adresat);

                return "cv_send_successfully";
            }
        } catch (EntityNotFoundException e) {
            // Obsłuż wyjątek EntityNotFoundException i zwróć widok cv_not_found
            return "cv_not_found";
        }
    }




    @PostMapping("/changeMeetingDate")
    public String changeMeetingDate (
            @RequestParam("notificationId") Integer notificationId,
            @RequestParam("jobOfferId") Integer jobOfferId,
            Authentication authentication
    ){
        String username = authentication.getName();
        User loggedInUser = userService.findByUserName(username);
        JobOffer jobOffer = jobOfferService.findById(jobOfferId);
        Notification notification = notificationService.findById(notificationId);
        User adresat = jobOffer.getUser();

        notificationService.changeMeetingDate(notification, loggedInUser, adresat);

        return "change_meeting_date_successfully";
    }


    @PostMapping("/acceptMeetingDate")
    public String acceptNotification (
            @RequestParam("notificationId") Integer notificationId,
            @RequestParam("jobOfferId") Integer jobOfferId,
            Authentication authentication
    ){
        String username = authentication.getName();
        User loggedInUser = userService.findByUserName(username);
        JobOffer jobOffer = jobOfferService.findById(jobOfferId);
        Notification notification = notificationService.findById(notificationId);
        User adresat = jobOffer.getUser();

        notificationService.acceptMeetingDateTime(notification, loggedInUser, adresat);

        return "meeting_date_successfully";
    }


    @PostMapping("/arrangeInterview")
    public String arrangeInterview(
            @RequestParam("cvId") Integer cvId,
            @RequestParam("notificationId") Integer notificationId,
            @RequestParam("proposedDateTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime proposedDateTime,
            Authentication authentication
    ) {
        String username = authentication.getName();
        User loggedInUser = userService.findByUserName(username);
        User cvUser = userService.getUserByCv(cvId);
        Notification notification = notificationService.findById(notificationId);

        notificationService.arrangeInterview(notification, loggedInUser, cvUser, proposedDateTime);

        return "arrange_interview_successfully";

    }


    @PostMapping("/decline")
    public String declineNotification(
            @RequestParam("notificationId") Integer notificationId,
            @RequestParam("cvId") Integer cvId,
            Authentication authentication
    ) {
        String username = authentication.getName();
        User loggedInUser = userService.findByUserName(username);
        User cvUser = userService.getUserByCv(cvId);
        Notification notification = notificationService.findById(notificationId);

        notificationService.declineCandidate(notification, loggedInUser, cvUser);

        return "decline_successfully";

    }

    @PostMapping("/hired")
    public String hiredCandidate(
            @RequestParam("notificationId") Integer notificationId,
            @RequestParam("cvId") Integer cvId,
            Authentication authentication
    ) {
        String username = authentication.getName();
        User loggedInUser = userService.findByUserName(username);
        User cvUser = userService.getUserByCv(cvId);
        Notification notification = notificationService.findById(notificationId);

        notificationService.hiredCandidate(notification, loggedInUser, cvUser);

        return "hired_successfully";
    }


}
