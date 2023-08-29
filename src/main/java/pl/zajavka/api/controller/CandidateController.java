package pl.zajavka.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pl.zajavka.api.dto.mapper.CandidateMapper;
import pl.zajavka.business.dao.CandidateDAO;

@Controller
@RequiredArgsConstructor
public class CandidateController {

    private static final String CANDIDATE = "/candidate";
    private final CandidateMapper candidateMapper;
    private final CandidateDAO candidateDAO;

    @GetMapping(value = CANDIDATE)
    public String homePage(Model model) {

        var candidates = candidateDAO.findCandidates().stream()
                        .map(candidateMapper::map)
                                .toList();
        model.addAttribute("candidatesDTOs", candidates);
        return "candidate";
    }
}
