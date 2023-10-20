package pl.zajavka.api.controller;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@AllArgsConstructor
@Controller
public class CandidatePortalController {
    public static final String CANDIDATE_PORTAL = "/candidate_portal";
//    public static final String USER_ID = "/{userId}";

    @GetMapping(CANDIDATE_PORTAL)
    public String getCandidatePortalPage() {
        return "candidate_portal";
    }



}
