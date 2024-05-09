package pl.zajavka.util;

import lombok.experimental.UtilityClass;
import pl.zajavka.controller.dto.UserDTO;
import pl.zajavka.infrastructure.domain.Notification;
import pl.zajavka.infrastructure.domain.User;
import pl.zajavka.infrastructure.database.entity.Status;
import pl.zajavka.infrastructure.security.RoleEntity;
import pl.zajavka.infrastructure.security.UserEntity;

import java.time.LocalDateTime;
import java.util.Set;

@UtilityClass
public class UserFixtures {



    public static UserEntity someUserEntity1() {
        RoleEntity candidateRole = RoleEntity.builder().id(1).role("ROLE_CANDIDATE").build();
        UserEntity userEntity = UserEntity.builder()
//                .id(1)
                .userName("adam12")
                .roles(Set.of(candidateRole))
                .password("adam112")
                .email("adam2113@poczta.onet.pl")
                .active(true)
                .build();
        return userEntity;
    }

    public static UserEntity someUserEntity2() {
        RoleEntity companyRole = RoleEntity.builder().role("ROLE_COMPANY").build();
        UserEntity userEntity = UserEntity.builder()
                .userName("john34")
                .roles(Set.of(companyRole))
                .password("john34")
                .email("john34@example.com")
                .active(true)
                .build();

        return userEntity;
    }

    public static UserEntity someUserEntity3() {
        RoleEntity candidateRole = RoleEntity.builder().role("ROLE_CANDIDATE").build();
        return UserEntity.builder()
                .userName("john35")
                .roles(Set.of(candidateRole))
                .password("john35")
                .email("john35@example.com")
                .active(true)
                .build();
    }

    public static User someUser1() {
        RoleEntity candidateRole = RoleEntity.builder().id(1).role("ROLE_CANDIDATE").build();
        User user = User.builder()
//                .id(1)
                .userName("adam12")
                .roles(Set.of(candidateRole))
                .password("adam112")
                .email("adam2113@poczta.onet.pl")
                .active(true)
                .build();
        return user;
    }

    public static User someUser2() {
        RoleEntity companyRole = RoleEntity.builder().id(2).role("ROLE_COMPANY").build();
        User user = User.builder()
//                .id(2)
                .userName("adam122222")
                .roles(Set.of(companyRole))
                .password("adam122212")
                .email("adam2112223@poczta.onet.pl")
                .active(true)
                .build();
        return user;
    }
    public static UserDTO someUserDTO1() {
        RoleEntity candidateRole = RoleEntity.builder().role("ROLE_COMPANY").build();
        LocalDateTime notificationSendDateTime = LocalDateTime.now();
        LocalDateTime notificationReceiveDateTime = LocalDateTime.now();
        Notification notificationSend = Notification.builder()
                .companyMessage("randomMessage1")
                .candidateMessage("randomMessage2")
                .status(Status.WAITING_FOR_INTERVIEW)
                .dateTime(notificationSendDateTime)
                .build();
        Notification notificationsReceive = Notification.builder()
                .companyMessage("randomMessage3")
                .candidateMessage("randomMessage4")
                .status(Status.WAITING_FOR_INTERVIEW)
                .dateTime(notificationReceiveDateTime)
                .build();

        UserDTO user = UserDTO.builder()
                .userName("adam10120")
                .password("adam101120")
                .email("adam2011130@poczta.onet.pl")
                .active(true)
                .notificationsSend(Set.of(notificationSend))
                .notificationsReceive(Set.of(notificationsReceive))
                .build();
        return user;
    }

    public static UserDTO someUserDTO2() {
        RoleEntity candidateRole = RoleEntity.builder().role("ROLE_COMPANY").build();
        LocalDateTime notificationSendDateTime = LocalDateTime.now();
        LocalDateTime notificationReceiveDateTime = LocalDateTime.now();
        Notification notificationSend = Notification.builder()
                .companyMessage("randomMessage1")
                .candidateMessage("randomMessage2")
                .status(Status.WAITING_FOR_INTERVIEW)
                .dateTime(notificationSendDateTime)
                .build();
        Notification notificationsReceive = Notification.builder()
                .companyMessage("randomMessage3")
                .candidateMessage("randomMessage4")
                .status(Status.WAITING_FOR_INTERVIEW)
                .dateTime(notificationReceiveDateTime)
                .build();

        UserDTO user = UserDTO.builder()
                .userName("adam10120")
                .password("adam101120")
                .email("adam2011130@poczta.onet.pl")
                .active(true)
                .notificationsSend(Set.of(notificationSend))
                .notificationsReceive(Set.of(notificationsReceive))
                .build();
        return user;
    }
}