package com.getyourticket.cf.service;

import com.getyourticket.cf.dto.TicketDTO;
import com.getyourticket.cf.service.exceptions.TicketNotFoundException;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;

public interface ITicketService {
    TicketDTO addTicket(TicketDTO ticketDTO) throws Exception;

    TicketDTO getTicketById(Long id) throws TicketNotFoundException;

    List<TicketDTO> getTicketsByUserEmail(String email) throws EntityNotFoundException;

    List<TicketDTO> getTicketsByUsername(String username) throws TicketNotFoundException;

    List<TicketDTO> getAllTickets() throws Exception;

    List<TicketDTO> getTicketsByEventId(Long eventId) throws Exception;

    void deleteTicket(Long id) throws TicketNotFoundException;
}
