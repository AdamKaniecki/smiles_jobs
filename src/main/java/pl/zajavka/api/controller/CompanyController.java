package pl.zajavka.api.controller;

import jakarta.persistence.Column;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pl.zajavka.api.dto.mapper.CompanyMapper;
import pl.zajavka.business.CompanyService;

@Controller
@RequiredArgsConstructor
public class CompanyController {

    private static final String COMPANY = "/company";

    private final CompanyMapper companyMapper;
    private final CompanyService companyService;

    @GetMapping("/company")
    public String homePage(Model model){
        var companies = companyService.findCompanies().stream()
                .map(companyMapper::map)
                .toList();
        model.addAttribute("companiesDTOs", companies);
        return "company";

    }
}
