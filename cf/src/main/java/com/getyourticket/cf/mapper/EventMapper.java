package com.getyourticket.cf.mapper;

import com.getyourticket.cf.dto.EventDTO;
import com.getyourticket.cf.model.Event;

import java.util.List;

public interface EventMapper {
    EventDTO toDto(Event event);
    Event toEntity(EventDTO eventDTO);

    List<EventDTO> toDto(List<Event> events);

    List<Event> toEntity(List<EventDTO> eventDTOs);
}
