package com.getyourticket.cf.rest;

import com.getyourticket.cf.dto.EventDTO;
import com.getyourticket.cf.service.IEventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class EventController {
    private final IEventService eventService;

    @Operation(summary = "Add an Event")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Event created",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = EventDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid input was supplied",
                    content = @Content),
            @ApiResponse(responseCode = "503", description = "Service Unavailable",
                    content = @Content)})
    @PostMapping("/events/add-events")
    public ResponseEntity<EventDTO> addEvent(@Validated @RequestBody EventDTO eventDTO) {
        EventDTO createdEvent = eventService.addEvent(eventDTO);
        return ResponseEntity.status(201).body(createdEvent);
    }

    @Operation(summary = "Delete an Event by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Event Deleted",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = EventDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "Event not found",
                    content = @Content)})
    @DeleteMapping("/events/delete/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        try {
            eventService.deleteEvent(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/events")
    public ResponseEntity<List<EventDTO>> getAllEvents() {
        List<EventDTO> events = eventService.getAllEvents();
        return ResponseEntity.ok(events);
    }

    @GetMapping("/date/{date}")
    public ResponseEntity<List<EventDTO>> getEventsByDate(@PathVariable LocalDate date) {
        List<EventDTO> events = eventService.getEventsByDate(date);
        return ResponseEntity.ok(events);
    }

    @GetMapping("/time/{time}")
    public ResponseEntity<List<EventDTO>> getEventsByTime(@PathVariable LocalTime time) {
        List<EventDTO> events = eventService.getEventsByTime(time);
        return ResponseEntity.ok(events);
    }

    @GetMapping("/title/{title}")
    public ResponseEntity<List<EventDTO>> getEventByTitle(@PathVariable String title) {
        try {
            List<EventDTO> event = eventService.getEventByTitle(title);
            return ResponseEntity.ok(event);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventDTO> getEventById(@PathVariable Long id) {
        try {
            EventDTO event = eventService.getEventById(id);
            return ResponseEntity.ok(event);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
