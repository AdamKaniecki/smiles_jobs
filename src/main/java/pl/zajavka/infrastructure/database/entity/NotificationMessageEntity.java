//package pl.zajavka.infrastructure.database.entity;
//
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//@Entity
//@Data
//@Builder
//@AllArgsConstructor
//@NoArgsConstructor
//@Table(name = "notification_messages")
//public class NotificationMessageEntity {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "message_id")
//    private Integer id;
//
//    @ManyToOne
//    @JoinColumn(name = "notification_id")
//    private NotificationEntity notification;
//
//    @Column(name = "cv_id")
//    private Integer cvId;
//
//    @Column(name = "job_offer_id")
//    private Integer jobOfferId;
//
//    @Column(name = "message")
//    private String message;
//}
