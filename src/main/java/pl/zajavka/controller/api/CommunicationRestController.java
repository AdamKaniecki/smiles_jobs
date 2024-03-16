package pl.zajavka.controller.api;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.zajavka.infrastructure.business.*;
import pl.zajavka.infrastructure.domain.CV;
import pl.zajavka.infrastructure.domain.JobOffer;
import pl.zajavka.infrastructure.domain.User;

@AllArgsConstructor
@RestController
@RequestMapping("/api")
public class CommunicationRestController {

    private UserService userService;
    private JobOfferService jobOfferService;
    private CvService cvService;
    private NotificationService notificationService;

//    @PostMapping("/sendCV")
//    @Transactional
//    public ResponseEntity<String> sendCV(@RequestParam("jobOfferId") Integer jobOfferId, Authentication authentication) {
//        try {
//            String username = authentication.getName();
//            User loggedInUser = userService.findByUserName(username);
//            JobOffer jobOffer = jobOfferService.findById(jobOfferId);
//            CV cv = cvService.findByUser2(loggedInUser);
//            User adresat = jobOffer.getUser();
//
//            // Sprawdź, czy CV użytkownika istnieje
//            if (cv == null) {
//                // Jeśli CV nie istnieje, zwróć odpowiedź z błędem
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("CV not found");
//            }
//
//            if (notificationService.hasUserSentCVToJobOffer(loggedInUser, jobOffer)) {
//                // Jeśli CV już zostało wysłane do oferty pracy, zwróć odpowiedź z błędem
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("CV already sent");
//            } else {
//                // Wysłanie powiadomienia o przesłaniu CV
//                notificationService.createNotification(jobOffer, cv, loggedInUser, adresat);
//                userService.save(loggedInUser);
//                userService.save(adresat);
//
//                // Zwróć odpowiedź potwierdzającą
//                return ResponseEntity.status(HttpStatus.OK).body("CV sent successfully");
//            }
//        } catch (EntityNotFoundException e) {
//            // Obsłuż wyjątek EntityNotFoundException i zwróć odpowiedź z błędem
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("CV not found");
//        }
//    }

}
