package com.getyourticket.cf.service;

import com.getyourticket.cf.dto.EventDTO;
import com.getyourticket.cf.mapper.EventMapper;
import com.getyourticket.cf.model.Event;
import com.getyourticket.cf.repository.EventRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EventServiceImplTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private EventMapper eventMapper;

    @InjectMocks
    private EventServiceImpl eventService;

    private EventDTO eventDTO;

    @BeforeEach
    public void setup() {
        eventDTO = new EventDTO();
        eventDTO.setId(1L);
        eventDTO.setTitle("Test Event");
    }

    @Test
    public void testAddEvent_Success() {
        Event event = new Event();
        event.setId(1L);

        when(eventMapper.toEntity(any(EventDTO.class))).thenReturn(event);
        when(eventRepository.save(any(Event.class))).thenReturn(event);
        when(eventMapper.toDto(any(Event.class))).thenReturn(eventDTO);

        EventDTO savedEvent = eventService.addEvent(eventDTO);

        assertNotNull(savedEvent);
        assertEquals(1L, savedEvent.getId());

        verify(eventMapper, times(1)).toEntity(eventDTO);
        verify(eventRepository, times(1)).save(event);
        verify(eventMapper, times(1)).toDto(event);
    }

    @Test
    public void testDeleteEvent_Success() throws EntityNotFoundException {
        Event event = new Event();
        event.setId(1L);

        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));

        assertDoesNotThrow(() -> {
            eventService.deleteEvent(1L);
        });

        verify(eventRepository, times(1)).findById(1L);
        verify(eventRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testGetAllEvents_Success() {
        List<Event> events = new ArrayList<>();
        events.add(new Event());

        when(eventRepository.findAll()).thenReturn(events);
        when(eventMapper.toDto(any(Event.class))).thenReturn(eventDTO);

        List<EventDTO> foundEvents = eventService.getAllEvents();

        assertNotNull(foundEvents);
        assertFalse(foundEvents.isEmpty());

        verify(eventRepository, times(1)).findAll();
        verify(eventMapper, times(1)).toDto(any(Event.class));
    }

    @Test
    public void testGetEventsByDate_Success() throws EntityNotFoundException {
        List<Event> events = new ArrayList<>();
        events.add(new Event());

        when(eventRepository.findByDate(any(LocalDate.class))).thenReturn(events);
        when(eventMapper.toDto(any(Event.class))).thenReturn(eventDTO);

        List<EventDTO> foundEvents = eventService.getEventsByDate(LocalDate.now());

        assertNotNull(foundEvents);
        assertFalse(foundEvents.isEmpty());

        verify(eventRepository, times(1)).findByDate(any(LocalDate.class));
        verify(eventMapper, times(1)).toDto(any(Event.class));
    }

    @Test
    public void testGetEventsByDate_NoEventsFound() {
        when(eventRepository.findByDate(any(LocalDate.class))).thenReturn(new ArrayList<>());

        assertThrows(EntityNotFoundException.class, () -> {
            eventService.getEventsByDate(LocalDate.now());
        });

        verify(eventRepository, times(1)).findByDate(any(LocalDate.class));
        verifyNoMoreInteractions(eventRepository, eventMapper);
    }

    @Test
    public void testGetEventsByTime_Success() throws EntityNotFoundException {
        List<Event> events = new ArrayList<>();
        events.add(new Event());

        when(eventRepository.findByStartTime(any(LocalTime.class))).thenReturn(events);
        when(eventMapper.toDto(any(Event.class))).thenReturn(eventDTO);

        List<EventDTO> foundEvents = eventService.getEventsByTime(LocalTime.now());

        assertNotNull(foundEvents);
        assertFalse(foundEvents.isEmpty());

        verify(eventRepository, times(1)).findByStartTime(any(LocalTime.class));
        verify(eventMapper, times(1)).toDto(any(Event.class));
    }

    @Test
    public void testGetEventsByTime_NoEventsFound() {
        when(eventRepository.findByStartTime(any(LocalTime.class))).thenReturn(new ArrayList<>());

        assertThrows(EntityNotFoundException.class, () -> {
            eventService.getEventsByTime(LocalTime.now());
        });

        verify(eventRepository, times(1)).findByStartTime(any(LocalTime.class));
        verifyNoMoreInteractions(eventRepository, eventMapper);
    }

    @Test
    public void testGetEventByTitle_Success() throws EntityNotFoundException {
        List<Event> events = new ArrayList<>();
        events.add(new Event());

        when(eventRepository.findByTitleStartsWithIgnoreCase(any(String.class))).thenReturn(events);
        when(eventMapper.toDto(any(Event.class))).thenReturn(eventDTO);

        List<EventDTO> foundEvents = eventService.getEventByTitle("Test Event");

        assertNotNull(foundEvents);
        assertFalse(foundEvents.isEmpty());

        verify(eventRepository, times(1)).findByTitleStartsWithIgnoreCase("Test Event");
        verify(eventMapper, times(1)).toDto(any(Event.class));
    }

    @Test
    public void testGetEventByTitle_NoEventsFound() {
        when(eventRepository.findByTitleStartsWithIgnoreCase(any(String.class))).thenReturn(new ArrayList<>());

        assertThrows(EntityNotFoundException.class, () -> {
            eventService.getEventByTitle("Test Event");
        });

        verify(eventRepository, times(1)).findByTitleStartsWithIgnoreCase("Test Event");
        verifyNoMoreInteractions(eventRepository, eventMapper);
    }

    @Test
    public void testGetEventById_Success() throws EntityNotFoundException {
        Event event = new Event();
        event.setId(1L);

        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
        when(eventMapper.toDto(any(Event.class))).thenReturn(eventDTO);

        EventDTO foundEvent = eventService.getEventById(1L);

        assertNotNull(foundEvent);
        assertEquals(1L, foundEvent.getId());

        verify(eventRepository, times(1)).findById(1L);
        verify(eventMapper, times(1)).toDto(event);
    }

    @Test
    public void testGetEventById_EventNotFound() {
        when(eventRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            eventService.getEventById(1L);
        });

        verify(eventRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(eventRepository, eventMapper);
    }
}
