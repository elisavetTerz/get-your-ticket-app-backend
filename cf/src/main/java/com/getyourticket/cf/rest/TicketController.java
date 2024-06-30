package com.getyourticket.cf.rest;

import com.getyourticket.cf.authentication.jwt.AuthTokenFilter;
import com.getyourticket.cf.authentication.jwt.JwtUtils;
import com.getyourticket.cf.dto.TicketDTO;
import com.getyourticket.cf.service.ITicketService;
import com.getyourticket.cf.service.exceptions.TicketNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {
    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

    private final ITicketService ticketService;
    private final JwtUtils jwtUtils;

    @Operation(summary = "Add a Ticket")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Ticket added",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = TicketDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid input was supplied",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content),
            @ApiResponse(responseCode = "503", description = "Service Unavailable",
                    content = @Content)})
    @PostMapping("/add-tickets")
    public ResponseEntity<TicketDTO> addTicket(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @Validated @RequestBody TicketDTO ticketDTO) throws Exception {

        String token = extractToken(authorizationHeader);
        if (token == null || !jwtUtils.validateJwtToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        logger.info("Received request to add ticket: {}", ticketDTO);
        TicketDTO ticketAdded = ticketService.addTicket(ticketDTO);
        logger.info("Ticket added successfully: {}", ticketAdded);
        return ResponseEntity.status(201).body(ticketAdded);
    }

    @Operation(summary = "Get a Ticket by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ticket retrieved",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = TicketDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Ticket not found",
                    content = @Content)})
    @GetMapping("/get-ticket/{id}")
    public ResponseEntity<TicketDTO> getTicket(@PathVariable("id") Long ticketId) {
        try {
            TicketDTO ticketDTO = this.ticketService.getTicketById(ticketId);
            return ResponseEntity.status(200).body(ticketDTO);
            //        return new ResponseEntity<TicketDTO>(ticketDTO, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body(null);
        }
    }

    @GetMapping("/get-ticket-by-username/{username}")
    public ResponseEntity<List<TicketDTO>> getTicketsByUsername(@PathVariable("username") String username) {
        try {
            List<TicketDTO> ticketDTOs = this.ticketService.getTicketsByUsername(username);
            return ResponseEntity.status(200).body(ticketDTOs);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body(null);
        }
    }

    @GetMapping("/get-ticket-by-email/{email}")
    public ResponseEntity<List<TicketDTO>> getTicketsByUserEmail(@PathVariable("email") String email) {
        try {
            List<TicketDTO> ticketDTOs = this.ticketService.getTicketsByUserEmail(email);
            return ResponseEntity.status(200).body(ticketDTOs);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body(null);
        }
    }

    @GetMapping("/get-all-tickets")
    public ResponseEntity<List<TicketDTO>> getAllTickets() {
        try {
            List<TicketDTO> ticketDTOs = ticketService.getAllTickets();
            return ResponseEntity.status(200).body(ticketDTOs);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/get-tickets-by-event/{eventId}")
    public ResponseEntity<List<TicketDTO>> getTicketsByEventId(@PathVariable("eventId") Long eventId) {
        try {
            List<TicketDTO> ticketDTOs = ticketService.getTicketsByEventId(eventId);
            return ResponseEntity.status(200).body(ticketDTOs);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body(null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @DeleteMapping("/delete-ticket/{id}")
    public ResponseEntity<Void> deleteTicket(@PathVariable("id") Long id) {
        try {
            ticketService.deleteTicket(id);
            return ResponseEntity.status(204).build();
        } catch (TicketNotFoundException e) {
            return ResponseEntity.status(404).body(null);
        }
    }

    private String extractToken(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        return null;
    }
}