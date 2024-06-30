package com.getyourticket.cf.service;

import com.getyourticket.cf.authentication.jwt.JwtUtils;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class TicketServiceImpl implements ITicketService {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final TicketMapper ticketMapper;

    @Override
    public TicketDTO addTicket(TicketDTO ticketDTO) throws Exception {
        logger.info("Received ticketDTO: {}", ticketDTO);

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
            throw new EntityNotFoundException("No tickets found for the given email: " + email);
        }
        return tickets.stream().map(ticketMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<TicketDTO> getTicketsByUsername(String username) throws EntityNotFoundException {
        Optional<Ticket> tickets = ticketRepository.findByUserUsername(username);
        if (tickets.isEmpty()) {
            throw new EntityNotFoundException("No tickets found for the given lastname: " + username);
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
