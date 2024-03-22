package pl.zajavka.controller.api;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pl.zajavka.infrastructure.business.*;
import pl.zajavka.infrastructure.database.repository.CvRepository;
import pl.zajavka.infrastructure.database.repository.JobOfferRepository;
import pl.zajavka.infrastructure.database.repository.NotificationRepository;
import pl.zajavka.infrastructure.domain.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api")
public class CommunicationRestController {

    private UserService userService;
    private JobOfferService jobOfferService;
    private CvService cvService;
    private NotificationService notificationService;
    private CvRepository cvRepository;
    private JobOfferRepository jobOfferRepository;
    private NotificationRepository notificationRepository;

    @PostMapping("/sendCV")
    @Transactional
    public ResponseEntity<String> sendCV(@RequestBody MeetingInterviewRequest request, Authentication authentication) {
        try {
            String username = authentication.getName();
            User loggedInUser = userService.findByUserName(username);
            JobOffer jobOffer = jobOfferRepository.findById(request.getJobOfferId());
            CV cv = cvRepository.findByUser2(loggedInUser);
            User adresat = jobOffer.getUser();

            // Sprawdź, czy CV użytkownika istnieje
            if (cv == null) {
                // Jeśli CV nie istnieje, zwróć odpowiedź z błędem
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("CV not found");
            }

            if (notificationService.hasUserSentCVToJobOffer(loggedInUser, jobOffer)) {
                // Jeśli CV już zostało wysłane do oferty pracy, zwróć odpowiedź z błędem
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("CV already sent");
            } else {
                // Wysłanie powiadomienia o przesłaniu CV
                notificationService.createNotification(jobOffer, cv, loggedInUser, adresat);
                userService.save(loggedInUser);
                userService.save(adresat);

                // Zwróć odpowiedź potwierdzającą
                return ResponseEntity.status(HttpStatus.OK).body("CV sent successfully");
            }
        } catch (EntityNotFoundException e) {
            // Obsłuż wyjątek EntityNotFoundException i zwróć odpowiedź z błędem
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
            Notification notification = notificationRepository.findById(request.getNotificationId());

            notificationService.arrangeInterview(notification, loggedInUser, cvUser, request.getProposedDateTime());

            return ResponseEntity.ok("Interview arranged successfully");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Notification or user not found");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while arranging interview");
        }
    }

    @PostMapping("/changeMeetingDate")
    public ResponseEntity<String> changeMeetingDate(@RequestBody MeetingInterviewRequest request, Authentication authentication) {
        try {
            String username = authentication.getName();
            User loggedInUser = userService.findByUserName(username);
            JobOffer jobOffer = jobOfferRepository.findById(request.getJobOfferId());
            Notification notification = notificationRepository.findById(request.getNotificationId());
            User adresat = jobOffer.getUser();

            notificationService.changeMeetingDate(notification, loggedInUser, adresat);

            return ResponseEntity.ok("Meeting date changed successfully");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Notification or job offer not found");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while changing meeting date");
        }
    }

    @PostMapping("/acceptMeetingDate")
    public ResponseEntity<String> acceptNotification(@RequestBody MeetingInterviewRequest request, Authentication authentication) {
        try {
            String username = authentication.getName();
            User loggedInUser = userService.findByUserName(username);
            JobOffer jobOffer = jobOfferRepository.findById(request.getJobOfferId());
            Notification notification = notificationRepository.findById(request.getNotificationId());
            User adresat = jobOffer.getUser();

            notificationService.acceptMeetingDateTime(notification, loggedInUser, adresat);

            return ResponseEntity.ok("Meeting date accepted successfully");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Notification or job offer not found");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while accepting meeting date");
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
            Notification notification = notificationRepository.findById(request.getNotificationId());

            notificationService.declineCandidate(notification, loggedInUser, cvUser);

            return ResponseEntity.ok("Notification declined successfully");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Notification or user not found");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while declining notification");
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
            Notification notification = notificationRepository.findById(request.getNotificationId());

            notificationService.hiredCandidate(notification, loggedInUser, cvUser);

            return ResponseEntity.ok("Candidate hired successfully");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Notification or user not found");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while hiring candidate");
        }


    }
}