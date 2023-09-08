package pl.zajavka.api.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pl.zajavka.api.dto.CandidateDTO;
import pl.zajavka.api.dto.mapper.CandidateMapper;
import pl.zajavka.business.CandidateService;

import java.util.List;
@AllArgsConstructor
@Controller

public class CandidateController {

    public static final String CANDIDATE = "/candidate";
    public static final String CANDIDATE_ID = "/candidate/{id}";
    private CandidateService candidateService;
    private CandidateMapper candidateMapper;

    @GetMapping(CANDIDATE)
    public String getCandidatePage() {
        return "candidate"; // Tutaj zwracamy nazwę widoku "candidate.html" lub "candidate" z katalogu "templates"
    }


    @GetMapping(CANDIDATE_ID)
    public Object getCandidateById(@PathVariable Long id) {
        CandidateDTO candidateDTO = candidateService.getCandidateById(id);
        if (candidateDTO != null) {
            return ResponseEntity.ok();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<CandidateDTO>> getAllCandidates() {
        List<CandidateDTO> candidates = candidateService.getAllCandidates();
        return ResponseEntity.ok(candidates);
    }

//    @PostMapping("/create")
//    public ResponseEntity<CandidateDTO> createCandidate(@RequestBody CandidateDTO candidateDTO) {
//        CandidateDTO createdCandidate = candidateService.createCandidate(candidateDTO);
//        return ResponseEntity.ok(createdCandidate);
//    }

    @PostMapping(CANDIDATE)
    public String createCandidate (
    @RequestParam("candidateName") String candidateName,
    @RequestParam("candidateSurname") String candidateSurname,
    @RequestParam("candidateEmail") String candidateEmail,
    @RequestParam("candidatePhoneNumber") String candidatePhoneNumber)
    {
        // Tworzenie nowego kandydata na podstawie podanej nazwy

        CandidateDTO candidateDTO = new CandidateDTO();
        candidateDTO.setCandidateName(candidateName);
        candidateDTO.setCandidateSurname(candidateSurname);
        candidateDTO.setCandidateEmail(candidateEmail);
        candidateDTO.setCandidatePhoneNumber(candidatePhoneNumber);

        // Zapis kandydata w serwisie
        CandidateDTO createdCandidate = candidateService.createCandidate(candidateDTO);

        // Możesz dodać odpowiednią obsługę, np. przekierowanie na stronę z informacją o sukcesie
        return "candidate_done"; // Tutaj przekierowujemy użytkownika na inną stronę po utworzeniu kandydata
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCandidate(@PathVariable Long id) {
        boolean deleted = candidateService.deleteCandidate(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

//        @PutMapping("/{id}")
//    public ResponseEntity<CandidateDTO> updateCandidate(@PathVariable Long id, @RequestBody CandidateDTO candidateDTO) {
//        CandidateDTO updatedCandidate = candidateService.updateCandidate(id, candidateDTO);
//        if (updatedCandidate != null) {
//            return ResponseEntity.ok(updatedCandidate);
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }



