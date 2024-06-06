package pl.zajavka.controller.api;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.zajavka.infrastructure.business.CvService;
import pl.zajavka.infrastructure.business.JobOfferService;
import pl.zajavka.infrastructure.business.NotificationService;
import pl.zajavka.infrastructure.business.UserService;
import pl.zajavka.infrastructure.database.repository.CvRepository;
import pl.zajavka.infrastructure.domain.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api")
public class CommunicationRestController {

    private UserService userService;
    private JobOfferService jobOfferService;
    private NotificationService notificationService;
    private CvService cvService;


    @PostMapping("/sendCV")
    @Transactional
    public ResponseEntity<String> sendCV(@RequestBody MeetingInterviewRequest request, Authentication authentication) {
        try {
            String username = authentication.getName();
            User loggedInUser = userService.findByUserName(username);
            JobOffer jobOffer = jobOfferService.findById(request.getJobOfferId());
            CV cv = cvService.findByUser(loggedInUser);
            User adresat = jobOffer.getUser();


            if (notificationService.hasUserSentCVToJobOffer(loggedInUser, jobOffer)) {

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("CV already sent");
            } else {
                // Wysłanie powiadomienia o przesłaniu CV
                notificationService.createNotification(jobOffer, cv, loggedInUser, adresat);
                userService.save(loggedInUser);
                userService.save(adresat);

                return ResponseEntity.status(HttpStatus.OK).body("CV sent successfully");
            }
        } catch (EntityNotFoundException e) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("CV not found");
        }
    }

    @PostMapping("/arrangeInterview")
    public ResponseEntity<String> arrangeInterview(
            @RequestBody MeetingInterviewRequest request,
            Authentication authentication
    ) {
        try {
            String username = authentication.getName();
            User loggedInUser = userService.findByUserName(username);
            User cvUser = userService.getUserByCv(request.getCvId());
            Notification notification = notificationService.findById(request.getNotificationId());

            notificationService.arrangeInterview(notification, loggedInUser, cvUser, request.getProposedDateTime());

            return ResponseEntity.ok("Interview arranged successfully");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Notification or user not found");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("An error occurred while arranging interview");
        }
    }

    @PostMapping("/changeMeetingDate")
    public ResponseEntity<String> changeMeetingDate(@RequestBody MeetingInterviewRequest request, Authentication authentication) {
        try {
            String username = authentication.getName();
            User loggedInUser = userService.findByUserName(username);
            JobOffer jobOffer = jobOfferService.findById(request.getJobOfferId());
            Notification notification = notificationService.findById(request.getNotificationId());
            User adresat = jobOffer.getUser();

            notificationService.changeMeetingDate(notification, loggedInUser, adresat);

            return ResponseEntity.ok("Meeting date changed successfully");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Notification or job offer not found");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("An error occurred while changing meeting date");
        }
    }

    @PostMapping("/acceptMeetingDate")
    public ResponseEntity<String> acceptNotification(@RequestBody MeetingInterviewRequest request, Authentication authentication) {
        try {
            String username = authentication.getName();
            User loggedInUser = userService.findByUserName(username);
            JobOffer jobOffer = jobOfferService.findById(request.getJobOfferId());
            Notification notification = notificationService.findById(request.getNotificationId());
            User adresat = jobOffer.getUser();

            notificationService.acceptMeetingDateTime(notification, loggedInUser, adresat);

            return ResponseEntity.ok("Meeting date accepted successfully");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Notification or job offer not found");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("An error occurred while accepting meeting date");
        }
    }

    @PostMapping("/decline")
    public ResponseEntity<String> declineNotification(
            @RequestBody MeetingInterviewRequest request,
            Authentication authentication
    ) {
        try {
            String username = authentication.getName();
            User loggedInUser = userService.findByUserName(username);
            User cvUser = userService.getUserByCv(request.getCvId());
            Notification notification = notificationService.findById(request.getNotificationId());

            notificationService.declineCandidate(notification, loggedInUser, cvUser);

            return ResponseEntity.ok("Notification declined successfully");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Notification or user not found");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("An error occurred while declining notification");
        }
    }

    @PostMapping("/hired")
    public ResponseEntity<String> hiredNotification(
            @RequestBody MeetingInterviewRequest request,
            Authentication authentication
    ) {
        try {
            String username = authentication.getName();
            User loggedInUser = userService.findByUserName(username);
            User cvUser = userService.getUserByCv(request.getCvId());
            Notification notification = notificationService.findById(request.getNotificationId());

            notificationService.hiredCandidate(notification, loggedInUser, cvUser);

            return ResponseEntity.ok("Candidate hired successfully");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Notification or user not found");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("An error occurred while hiring candidate");
        }


    }
}