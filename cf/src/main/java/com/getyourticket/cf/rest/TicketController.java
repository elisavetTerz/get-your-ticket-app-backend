package com.getyourticket.cf.rest;

import com.getyourticket.cf.dto.TicketDTO;
import com.getyourticket.cf.service.ITicketService;
import com.getyourticket.cf.service.exceptions.TicketNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {
    @Autowired
    private ITicketService ticketService;

    @PostMapping("/add-tickets")
    public ResponseEntity<TicketDTO> addTicket(@Validated @RequestBody TicketDTO ticketDTO) throws Exception {
        TicketDTO ticketAdded = ticketService.addTicket(ticketDTO);
        return ResponseEntity.status(201).body(ticketAdded);
    }

    @GetMapping("/get-ticket/{id}")
    public ResponseEntity<TicketDTO> getTicket(@PathVariable("id") Long ticketId){
        try {
            TicketDTO ticketDTO = this.ticketService.getTicketById(ticketId);
            return ResponseEntity.status(200).body(ticketDTO);
            //        return new ResponseEntity<TicketDTO>(ticketDTO, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body(null);
        }
    }
    @GetMapping("/get-ticket-by-lastname/{lastname}")
    public ResponseEntity<List<TicketDTO>> getTicketsByUserLastname(@PathVariable("lastname") String lastname){
        try {
            List<TicketDTO> ticketDTOs = this.ticketService.getTicketsByUserLastname(lastname);
            return ResponseEntity.status(200).body(ticketDTOs);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body(null);
        }
    }
    @GetMapping("/get-ticket-by-email/{email}")
    public ResponseEntity<List<TicketDTO>> getTicketsByUserEmail(@PathVariable("email") String email){
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

}