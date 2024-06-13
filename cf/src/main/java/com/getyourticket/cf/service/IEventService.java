package com.getyourticket.cf.service;

import com.getyourticket.cf.dto.EventDTO;
import jakarta.persistence.EntityNotFoundException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface IEventService {
    EventDTO addEvent(EventDTO eventDTO);
    void deleteEvent(Long id) throws EntityNotFoundException;
    List<EventDTO> getAllEvents();
    List<EventDTO> getEventsByDate(LocalDate date) throws EntityNotFoundException;
    List<EventDTO> getEventsByTime(LocalTime time) throws EntityNotFoundException;
    List<EventDTO> getEventByTitle(String title) throws EntityNotFoundException;
    EventDTO getEventById(Long id) throws EntityNotFoundException;
}
