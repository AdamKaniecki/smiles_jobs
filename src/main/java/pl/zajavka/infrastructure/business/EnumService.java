//package pl.zajavka.infrastructure.business;
//
//import jakarta.transaction.Transactional;
//import lombok.AllArgsConstructor;
//import org.springframework.stereotype.Service;
//import pl.zajavka.infrastructure.database.entity.ProgrammingLanguage;
//
//import java.util.Arrays;
//import java.util.HashSet;
//import java.util.Set;
//
//@Service
//@AllArgsConstructor
//public class EnumService {
//
//
//    @Transactional
//    public Set<ProgrammingLanguage> getAllProgrammingLanguages() {
//        // Tworzymy zbiór, do którego dodamy wszystkie wartości enumeracji ProgrammingLanguage
//        Set<ProgrammingLanguage> programmingLanguages = new HashSet<>(Arrays.asList(ProgrammingLanguage.values()));
//        return programmingLanguages;
//    }
//
////    @Transactional
////    public Set<IT_Specializations> getAll_it_specializations() {
////        // Tworzymy zbiór, do którego dodamy wszystkie wartości enumeracji ProgrammingLanguage
////        Set<ProgrammingLanguage> programmingLanguages = new HashSet<>(Arrays.asList(ProgrammingLanguage.values()));
////        return programmingLanguages;
////    }
//
//    @Transactional
//    public Set<ProgrammingLanguage> convertToProgrammingLanguages(Set<String> programmingLanguagesNames) {
//        Set<ProgrammingLanguage> programmingLanguages = new HashSet<>();
//        if (programmingLanguagesNames != null) {
//            for (String languageName : programmingLanguagesNames) {
//                ProgrammingLanguage language = ProgrammingLanguage.valueOf(languageName);
//                programmingLanguages.add(language);
//            }
//        }
//        return programmingLanguages;
//    }
//
////    @Transactional
////    public Set<IT_Specializations> convertToSpecializations(Set<String> specializationNames) {
////        Set<IT_Specializations> it_specializations = new HashSet<>();
////        if (specializationNames != null) {
////            for (String specializationName : specializationNames) {
////                IT_Specializations specialization = IT_Specializations.valueOf(specializationName);
////                it_specializations.add(specialization);
////            }
////        }
////        return it_specializations;
////    }
//
//}
