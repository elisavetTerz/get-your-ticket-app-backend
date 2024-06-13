package com.getyourticket.cf.model;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Event Entity:
 * this table will store information about the theatre shows.
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "EVENTS")
public class Event {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="title")
    private String title;

    @Column(name = "description", length = 200)
    private String description;

    @Column(name="duration_minutes")
    private Integer durationMinutes;

    @Column(name="date")
    private LocalDate date;

    @Column(name="start_time")
    private LocalTime startTime;

    @Column(name="available_Seats")
    private Integer availableSeats;

    @OneToMany(mappedBy = "event")
    private List<Ticket> tickets = new ArrayList<>();

}
