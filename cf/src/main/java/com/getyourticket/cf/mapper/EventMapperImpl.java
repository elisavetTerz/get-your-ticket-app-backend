package com.getyourticket.cf.mapper;

import com.getyourticket.cf.dto.EventDTO;
import com.getyourticket.cf.model.Event;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class EventMapperImpl implements EventMapper {
    @Override
    public EventDTO toDto(Event event) {
        if (event == null) {
            return null;
        }

        EventDTO eventDTO = new EventDTO();
        eventDTO.setId(event.getId());
        eventDTO.setTitle(event.getTitle());
        eventDTO.setDescription(event.getDescription());
        eventDTO.setDurationMinutes(event.getDurationMinutes());
        eventDTO.setDate(event.getDate());
        eventDTO.setStartTime(event.getStartTime());
        eventDTO.setAvailableSeats(event.getAvailableSeats());

        return eventDTO;
    }

    @Override
    public Event toEntity(EventDTO eventDTO) {
        if (eventDTO == null) {
            return null;
        }

        Event event = new Event();
        event.setId(eventDTO.getId());
        event.setTitle(eventDTO.getTitle());
        event.setDescription(eventDTO.getDescription());
        event.setDurationMinutes(eventDTO.getDurationMinutes());
        event.setDate(eventDTO.getDate());
        event.setStartTime(eventDTO.getStartTime());
        event.setAvailableSeats(eventDTO.getAvailableSeats());

        return event;
    }

    @Override
    public List<EventDTO> toDto(List<Event> events) {
        if (events == null) {
            return null;
        }

        return events.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<Event> toEntity(List<EventDTO> eventDTOs) {
        if (eventDTOs == null) {
            return null;
        }

        return eventDTOs.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

}
