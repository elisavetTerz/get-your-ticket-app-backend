package com.getyourticket.cf.service;

import com.getyourticket.cf.dto.EventDTO;
import com.getyourticket.cf.mapper.EventMapper;
import com.getyourticket.cf.model.Event;
import com.getyourticket.cf.repository.EventRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventServiceImpl implements IEventService {
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;

    @Override
    public EventDTO addEvent(EventDTO eventDTO) {
        Event event = eventMapper.toEntity(eventDTO);
        event = eventRepository.save(event);
        return eventMapper.toDto(event);    }

    @Override
    public void deleteEvent(Long id) throws EntityNotFoundException {
        eventRepository.deleteById(id);
    }

    @Override
    public List<EventDTO> getAllEvents() {
        List<Event> events = eventRepository.findAll();
        log.debug("Fetched events: {}", events); // Logging for debugging
        return events.stream()
                .map(eventMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<EventDTO> getEventsByDate(LocalDate date) throws EntityNotFoundException {
        List<Event> events = eventRepository.findByDate(date);
        if (events.isEmpty()) {
            throw new EntityNotFoundException("No events found for the given date");
        }
        return events.stream().map(eventMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<EventDTO> getEventsByTime(LocalTime time) throws EntityNotFoundException {
        List<Event> events = eventRepository.findByStartTime(time);
        if (events.isEmpty()) {
            throw new EntityNotFoundException("No events found for the given time");
        }
        return events.stream()
                .map(eventMapper::toDto)
                .collect(Collectors.toList());    }

    @Override
    public List<EventDTO> getEventByTitle(String title) throws EntityNotFoundException {
        List<Event> events = eventRepository.findByTitle(title);
        if (events.isEmpty()) {
            throw new EntityNotFoundException("Found no events with the given title");
        }
        return events.stream().map(eventMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public EventDTO getEventById(Long id) throws EntityNotFoundException {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Event not found"));
        return eventMapper.toDto(event);
    }

//    @Override
//    public EventDTO addEvent(EventDTO eventDTO) {
//        Event event = EventMapper.toEntity(eventDTO);
//        event = eventRepository.save(event);
//        return EventMapper.toDto(event);
//    }
//
//    @Override
//    public void deleteEvent(Long id) throws EntityNotFoundException {
//        if (!eventRepository.existsById(id)) {
//            throw new EntityNotFoundException("Event not found");
//        }
//        eventRepository.deleteById(id);
//    }
//
//    @Override
//    public List<EventDTO> getAllEvents() {
//        return eventRepository.findAll().stream()
//                .map(EventMapper::toDto)
//                .collect(Collectors.toList());
//    }
//

//
//    @Override
//    public List<EventDTO> getEventsByTime(LocalTime time) throws EntityNotFoundException {
//        List<Event> events = eventRepository.findByStartTime(time);
//        if (events.isEmpty()) {
//            throw new EntityNotFoundException("No events found for the given time");
//        }
//        return events.stream().map(EventMapper::toDto).collect(Collectors.toList());
//    }
//
//    @Override
//    public EventDTO getEventByTitle(String title) throws EntityNotFoundException {
//        Event event = eventRepository.findByTitle(title).stream().findFirst()
//                .orElseThrow(() -> new EntityNotFoundException("Event not found with title: " + title));
//        return EventMapper.toDto(event);
//    }
//
//    @Override
//    public EventDTO getEventById(Long id) throws EntityNotFoundException {
//        Event event = eventRepository.findById(id)
//                .orElseThrow(() -> new EntityNotFoundException("Event not found with id: " + id));
//        return EventMapper.toDto(event);
//    }
}
