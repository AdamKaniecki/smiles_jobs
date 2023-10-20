package pl.zajavka.api.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@AllArgsConstructor
@Controller
public class CompanyPortalController {

    public static final String COMPANY_PORTAL = "/company_portal";

    @GetMapping(COMPANY_PORTAL)
    public String getCandidatePortalPage() {
        return "company_portal";
    }
}
