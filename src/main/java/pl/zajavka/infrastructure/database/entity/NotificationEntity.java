//package pl.zajavka.infrastructure.database.entity;
//
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import pl.zajavka.infrastructure.security.UserEntity;
//
//import java.time.OffsetDateTime;
//import java.util.List;
//import java.util.Set;
//
//@Data
//@Builder
//@AllArgsConstructor
//@NoArgsConstructor
//@Entity
//@Table(name = "notification_table")
//public class NotificationEntity {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "notification_id")
//    private Integer id;
//
//    @Column(name = "notification_date_time")
//    private OffsetDateTime notificationDateTime;
//
//    @Column(name = "message")
//    private String message;
//
//    @Column(name = "interaction")
//    private String interaction;
//
//    @ManyToMany
//    @JoinTable(
//            name = "user_notification_table",
//            joinColumns = @JoinColumn(name = "notification_id"),
//            inverseJoinColumns = @JoinColumn(name = "user_id"))
//    private Set<UserEntity> users;
//
//    @ManyToMany(mappedBy = "notifications")
//    private Set<JobOfferEntity> jobOffers;
//
//
//
//}
