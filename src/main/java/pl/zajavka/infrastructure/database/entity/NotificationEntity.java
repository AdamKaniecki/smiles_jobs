package pl.zajavka.infrastructure.database.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.zajavka.infrastructure.security.UserEntity;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "notification_table")
public class NotificationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Integer id;

    @Column(name = "candidate_message")
    private String candidateMessage;

    @Column(name = "company_message")
    private String companyMessage;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Column(name = "date_time")
    private LocalDateTime dateTime;

    @ManyToOne
    @JoinColumn(name = "cv_id")
    private CvEntity cv;

    @ManyToOne
    @JoinColumn(name = "job_offer_id")
    private JobOfferEntity jobOffer;

    @ManyToOne
    @JoinColumn(name = "sender_user_id")
    private UserEntity senderUser;

    @ManyToOne
    @JoinColumn(name = "receiver_user_id")
    private UserEntity receiverUser;
}

