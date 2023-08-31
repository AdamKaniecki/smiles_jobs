package pl.zajavka.api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import pl.zajavka.api.dto.CandidateDTO;
import pl.zajavka.api.dto.mapper.CandidateMapper;
import pl.zajavka.business.CandidateService;
import pl.zajavka.domain.Candidate;

@Controller
@RequiredArgsConstructor
public class CandidateController {

    private static final String CANDIDATE = "/candidate";
    private static final String CANDIDATE_REGISTRY = "/candidate_registry";
    private final CandidateMapper candidateMapper;
    private final CandidateService candidateService;
    private final CandidateDTO candidateDTO;




//    @GetMapping(value = PURCHASE)
//    public ModelAndView carPurchasePage(){
//        Map<String,?> model = prepareCarPurchaseData();
//        return new ModelAndView("car_purchase", model);



    @GetMapping(value = CANDIDATE)
    public String homePage(Model model) {

        var candidates = candidateService.findCandidates().stream()
                        .map((Candidate candidate) -> candidateMapper.map(candidateDTO))
                                .toList();
        model.addAttribute("candidatesDTOs", candidates);
        return "candidate";
    }

    @PostMapping(value = CANDIDATE)
    public String makeCandidate(
            @Valid @ModelAttribute("candidateDTO") CandidateDTO candidateDTO,
            BindingResult result,
            ModelMap model
    ) {
        if(result.hasErrors()){
            return "error";
        } else
//        CandidateDTO candidate = candidateMapper.mapToDTO(candidate);

//        Invoice invoice = carPurchaseService.purchase(request);

//        if (!candidateDTO.getExistingCandidateEmail().isBlank()){
//            model.addAttribute("existingCandidateEmail", candidateDTO.getExistingCandidateEmail());


        {


            model.addAttribute("customerName", candidateDTO.getCandidateName());
            model.addAttribute("customerSurname", candidateDTO.getCandidateSurname());
        }
//        model.addAttribute("invoiceNumber", invoice.getInvoiceNumber());

        return "candidate_registry";
    }
    @GetMapping(CANDIDATE_REGISTRY)
    public String showRegistrationForm(Model model) {
        model.addAttribute("candidateDto", new CandidateDTO());
        return "registry_candidate"; // Zwracany widok formularza rejestracji
    }
}
