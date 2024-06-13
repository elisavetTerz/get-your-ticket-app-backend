package com.getyourticket.cf.service;

import com.getyourticket.cf.dto.TicketDTO;
import com.getyourticket.cf.mapper.TicketMapper;
import com.getyourticket.cf.model.Event;
import com.getyourticket.cf.model.Ticket;
import com.getyourticket.cf.model.User;
import com.getyourticket.cf.repository.EventRepository;
import com.getyourticket.cf.repository.TicketRepository;
import com.getyourticket.cf.repository.UserRepository;
import com.getyourticket.cf.service.exceptions.TicketNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class TicketServiceImpl implements ITicketService {
    @Autowired
    private final TicketRepository ticketRepository;
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final EventRepository eventRepository;
    @Autowired
    private TicketMapper ticketMapper;


    @Override
    public TicketDTO addTicket(TicketDTO ticketDTO) throws Exception {
        User user = userRepository.findById(ticketDTO.getUserId().getId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Event event = eventRepository.findById(ticketDTO.getEventId().getId())
                .orElseThrow(() -> new EntityNotFoundException("Event not found"));

        Ticket ticket = ticketMapper.toEntity(ticketDTO);
        ticket.setUser(user);
        ticket.setEvent(event);
        ticket.setCreatedAt(LocalDateTime.now());
        ticket.setUpdatedAt(LocalDateTime.now());

        Ticket savedTicket = ticketRepository.save(ticket);
        return ticketMapper.toDto(savedTicket);
    }

    @Override
    public TicketDTO getTicketById(Long id) throws TicketNotFoundException {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new TicketNotFoundException(id));
        return ticketMapper.toDto(ticket);
    }

    @Override
    public List<TicketDTO> getTicketsByUserEmail(String email) throws EntityNotFoundException {
        List<Ticket> tickets = ticketRepository.findByUserEmail(email);
        if (tickets.isEmpty()) {
            throw new EntityNotFoundException("No tickets found for the given email: " +email);
        }
        return tickets.stream().map(ticketMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<TicketDTO> getTicketsByUserLastname(String lastname) throws EntityNotFoundException {
        List<Ticket> tickets = ticketRepository.findByUserLastname(lastname);
        if (tickets.isEmpty()) {
            throw new EntityNotFoundException("No tickets found for the given lastname: " + lastname);
        }
        return tickets.stream().map(ticketMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<TicketDTO> getAllTickets() throws Exception {
        List<Ticket> tickets = ticketRepository.findAll();
        return tickets.stream().map(ticketMapper::toDto).collect(Collectors.toList());
    }


    @Override
    public List<TicketDTO> getTicketsByEventId(Long eventId) throws Exception {
        List<Ticket> tickets = ticketRepository.findByEventId(eventId);
        if (tickets.isEmpty()) {
            throw new EntityNotFoundException("No tickets found for event with id: " + eventId);
        }
        return tickets.stream().map(ticketMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public void deleteTicket(Long id) throws TicketNotFoundException {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new TicketNotFoundException(id));
        ticketRepository.delete(ticket);
    }
}
