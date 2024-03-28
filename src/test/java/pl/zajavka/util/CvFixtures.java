//package pl.zajavka.util;
//
//import lombok.experimental.UtilityClass;
//import pl.zajavka.infrastructure.domain.Address;
//import pl.zajavka.infrastructure.domain.CV;
//import pl.zajavka.infrastructure.domain.User;
//import pl.zajavka.infrastructure.database.entity.CvEntity;
//
//@UtilityClass
//public class CvFixtures {
//
//
//
//    public CV createSampleCv() {
//
//        Address address = Address.builder()
//                .city("Sample City")
//                .country("Sample Country")
//                .streetAndNumber("Sample Street 125")
//                .build();
//
//        User user = User.builder()
//                .userName("sample_user")
//                .email("sample@example.com")
//                .password("password123")
//                .active(true)
//                .build();
//
//
//
//        CV cv = CV.builder()
//                .name("Sample Name")
//                .surname("Sample Surname")
//                .dateOfBirth("01-01-1990")
//                .sex("Male")
//                .maritalStatus("Single")
//                .contactEmail("sample@example.com")
//                .phoneNumber("+1234567890")
//                .education("Sample Education")
//                .workExperience("Sample Work Experience")
//                .skills("Sample Skills")
//                .language("English")
//                .languageLevel("B2")
//                .hobby("Sample Hobby")
//                .address(address)
//                .user(user)
//                .build();
//        return cv;
//    }
//
//    public static CvEntity sampleCvEntity(){
//
//        CvEntity cv = CvEntity.builder()
//                .name("John")
//                .surname("Doe")
//                .dateOfBirth("1990-01-01")
//                .sex("Male")
//                .maritalStatus("Single")
//                .phoneNumber("123456789")
//                .contactEmail("john@example.com")
//                .workExperience("5 years")
//                .education("Master's Degree")
//                .skills("Java, Python")
//                .tools("IntelliJ IDEA, Git")
//                .yearsOfExperience(5)
//                .language("English")
//                .languageLevel("Fluent")
//                .hobby("Reading, Travelling")
//                .build();
//
//        return cv;
//
//    }
//
//    public static CV sampleCV(){
//
//        CV cv = CV.builder()
//                .name("John")
//                .surname("Doe")
//                .dateOfBirth("1990-01-01")
//                .sex("Male")
//                .maritalStatus("Single")
//                .phoneNumber("123456789")
//                .contactEmail("john@example.com")
//                .workExperience("5 years")
//                .education("Master's Degree")
//                .skills("Java, Python")
//                .tools("IntelliJ IDEA, Git")
//                .yearsOfExperience(5)
//                .language("English")
//                .languageLevel("Fluent")
//                .hobby("Reading, Travelling")
//                .build();
//
//        return cv;
//
//    }
//}
