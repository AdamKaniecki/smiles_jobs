package pl.zajavka.business;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.zajavka.infrastructure.database.entity.ProgrammingLanguage;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Service
@AllArgsConstructor
public class EnumService {


    @Transactional
    public Set<ProgrammingLanguage> getAllProgrammingLanguages() {
        // Tworzymy zbiór, do którego dodamy wszystkie wartości enumeracji ProgrammingLanguage
        Set<ProgrammingLanguage> programmingLanguages = new HashSet<>(Arrays.asList(ProgrammingLanguage.values()));
        return programmingLanguages;
    }

}
