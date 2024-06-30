package com.getyourticket.cf.rest;

import com.getyourticket.cf.dto.EventDTO;
import com.getyourticket.cf.service.IEventService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


public class EventControllerTest {

    @Mock
    private IEventService eventService;

    @InjectMocks
    private EventController eventController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(eventController).build();
    }

    @Test
    public void testAddEventSuccess() throws Exception {
        EventDTO eventDTO = new EventDTO();
        eventDTO.setTitle("Concert");

        when(eventService.addEvent(any(EventDTO.class))).thenReturn(eventDTO);

        mockMvc.perform(post("/api/events/add-events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Concert\"}"))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").value("Concert"));
    }

    @Test
    public void testDeleteEventSuccess() throws Exception {
        mockMvc.perform(delete("/api/events/delete/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteEventNotFound() throws Exception {
        doThrow(new EntityNotFoundException()).when(eventService).deleteEvent(1L);

        mockMvc.perform(delete("/api/events/delete/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetAllEvents() throws Exception {
        EventDTO eventDTO1 = new EventDTO();
        eventDTO1.setTitle("Concert");
        EventDTO eventDTO2 = new EventDTO();
        eventDTO2.setTitle("Movie");

        List<EventDTO> eventDTOs = Arrays.asList(eventDTO1, eventDTO2);
        when(eventService.getAllEvents()).thenReturn(eventDTOs);

        mockMvc.perform(get("/api/events"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].title").value("Concert"))
                .andExpect(jsonPath("$[1].title").value("Movie"));
    }

    @Test
    public void testGetEventsByDateSuccess() throws Exception {
        EventDTO eventDTO = new EventDTO();
        eventDTO.setTitle("Concert");

        List<EventDTO> eventDTOs = Arrays.asList(eventDTO);
        when(eventService.getEventsByDate(any(LocalDate.class))).thenReturn(eventDTOs);

        mockMvc.perform(get("/api/date/2024-06-20"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].title").value("Concert"));
    }

    @Test
    public void testGetEventsByTimeSuccess() throws Exception {
        EventDTO eventDTO = new EventDTO();
        eventDTO.setTitle("Concert");

        List<EventDTO> eventDTOs = Arrays.asList(eventDTO);
        when(eventService.getEventsByTime(any(LocalTime.class))).thenReturn(eventDTOs);

        mockMvc.perform(get("/api/time/19:00:00"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].title").value("Concert"));
    }

    @Test
    public void testGetEventByTitleSuccess() throws Exception {
        EventDTO eventDTO = new EventDTO();
        eventDTO.setTitle("Concert");

        List<EventDTO> eventDTOs = Arrays.asList(eventDTO);
        when(eventService.getEventByTitle("Concert")).thenReturn(eventDTOs);

        mockMvc.perform(get("/api/title/Concert"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].title").value("Concert"));
    }

    @Test
    public void testGetEventByIdSuccess() throws Exception {
        EventDTO eventDTO = new EventDTO();
        eventDTO.setId(1L);
        eventDTO.setTitle("Concert");

        when(eventService.getEventById(1L)).thenReturn(eventDTO);

        mockMvc.perform(get("/api/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Concert"));
    }

    @Test
    public void testGetEventByIdNotFound() throws Exception {
        when(eventService.getEventById(1L)).thenThrow(new EntityNotFoundException());

        mockMvc.perform(get("/api/1"))
                .andExpect(status().isNotFound());
    }
}