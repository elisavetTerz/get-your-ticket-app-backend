package com.getyourticket.cf.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class EventDTO {
    private Long id;
    private String title;
    private String description;
    private Integer durationMinutes;
    private LocalDate date;
    private LocalTime startTime;
    private Integer availableSeats;

}
