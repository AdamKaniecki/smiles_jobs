package pl.zajavka.util;

import pl.zajavka.infrastructure.database.entity.CandidateEntity;

public class EntityFixtures {

    public static CandidateEntity someCandidate1(){
        return CandidateEntity.builder()
                .name("Tobi")
                .surname("Ohpuhu")
                .email("cTtyu@ERT.pl")
                .phoneNumber("567456234")
                .build();
    }

    public static CandidateEntity someCandidate2(){
        return CandidateEntity.builder()
                .name("iko")
                .surname("ujuk")
                .email("uiop@ERT.pl")
                .phoneNumber("568123456")
                .build();
    }
}
