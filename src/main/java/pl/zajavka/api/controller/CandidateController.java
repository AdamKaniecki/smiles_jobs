package pl.zajavka.api.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import pl.zajavka.api.dto.CandidateDTO;
import pl.zajavka.api.dto.mapper.CandidateMapper;
import pl.zajavka.business.CandidateService;
import pl.zajavka.domain.Candidate;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class CandidateController {

    private static final String CANDIDATE = "/candidate";

    private final CandidateMapper candidateMapper;
    private final CandidateService candidateService;
    private  Candidate candidate;

    @GetMapping(value = CANDIDATE)
    public List<CandidateDTO> getAllCandidates() {
        List<Candidate> candidates = candidateService.findCandidates();
        return candidates.stream()
                .map(candidateMapper::mapToCandidateDTO)
                .collect(Collectors.toList());
    }


    @PostMapping(value = CANDIDATE)
    public String makeCandidate(
            @Valid @ModelAttribute("candidateDTO") CandidateDTO candidateDTO,
            ModelMap model
    ) {
        model.addAttribute("candidateName", candidateDTO.getCandidateName());

        return "candidate";
    }
//    @PostMapping("/create")
//    public ModelAndView createCandidate(CandidateDTO candidateDTO,final Candidate candidate) {
//        ModelAndView modelAndView = new ModelAndView("candidate-form");
//
//        try {
//            // Tworzenie nowego kandydata na podstawie danych z formularza
//            Candidate newCandidate = new Candidate();
//            newCandidate.setName(candidateDTO.getCandidateName());
//
//
//            // Zapis nowego kandydata
//            candidateService.saveCandidate(newCandidate);
//
//            modelAndView.addObject("message", "Kandydat został dodany pomyślnie.");
//        } catch (Exception e) {
//            modelAndView.addObject("error", "Wystąpił błąd podczas dodawania kandydata.");
//        }

//        return modelAndView;
//    }
}


