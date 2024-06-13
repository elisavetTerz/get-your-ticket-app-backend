package com.getyourticket.cf.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Ticket Entity:
 * This table will store information about the tickets
 * that users purchase.
 * Optional: the user can buy tickets for persons without account.
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "TICKETS")
public class Ticket {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "event_id", referencedColumnName = "id")
    private Event event;

    // In case the user can buy tickets for other persons with no account
    // private String purchaserName;
    // private String purchaserEmail;
    @Column(name="seat_number", nullable = true)
    private String seatNumber;

    @Column(name="price", nullable = true)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    private Status status; //reserved-purchased-cancelled

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;


    public LocalDateTime getEventStartTime() {
        return LocalDateTime.of(event.getDate(), event.getStartTime());
    }
}
