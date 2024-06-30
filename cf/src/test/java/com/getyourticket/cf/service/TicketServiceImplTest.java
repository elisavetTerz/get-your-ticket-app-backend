package com.getyourticket.cf.service;

import com.getyourticket.cf.dto.EventDTO;
import com.getyourticket.cf.dto.TicketDTO;
import com.getyourticket.cf.dto.UserRegisterDTO;
import com.getyourticket.cf.mapper.TicketMapper;
import com.getyourticket.cf.model.Event;
import com.getyourticket.cf.model.Status;
import com.getyourticket.cf.model.Ticket;
import com.getyourticket.cf.model.User;
import com.getyourticket.cf.repository.EventRepository;
import com.getyourticket.cf.repository.TicketRepository;
import com.getyourticket.cf.repository.UserRepository;
import com.getyourticket.cf.service.exceptions.TicketNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TicketServiceImplTest {

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private TicketMapper ticketMapper;

    @InjectMocks
    private TicketServiceImpl ticketService;

    private TicketDTO ticketDTO;

    @BeforeEach
    public void setup() {
        ticketDTO = new TicketDTO();
        ticketDTO.setId(1L);
        ticketDTO.setUserId(new UserRegisterDTO());
        ticketDTO.setEventId(new EventDTO());
        ticketDTO.setSeatNumber("A1");
        ticketDTO.setPrice(BigDecimal.valueOf(100.00));
        ticketDTO.setStatus(Status.RESERVED);
        ticketDTO.setCreatedAt(LocalDateTime.now());
        ticketDTO.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    public void testAddTicket_Success() throws Exception {
        // Setup User and Event for TicketDTO
        User user = new User(); // Create a user instance
        user.setId(1); // Set the ID explicitly for stubbing

        Event event = new Event(); // Create an event instance
        event.setId(Long.valueOf(1)); // Set the ID explicitly for stubbing

        // Mock userRepository.findById() to return the user object when called with ID 1
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        // Mock eventRepository.findById() to return the event object when called with ID 1
        when(eventRepository.findById(Long.valueOf(1))).thenReturn(Optional.of(event));

        // Prepare a TicketDTO object
        TicketDTO ticketDTO = new TicketDTO();
        UserRegisterDTO userRegisterDTO = new UserRegisterDTO();
        userRegisterDTO.setId(1); // Set the user ID in the UserRegisterDTO
        ticketDTO.setUserId(userRegisterDTO);

        EventDTO eventDTO = new EventDTO();
        eventDTO.setId(Long.valueOf(1)); // Set the event ID in the EventDTO
        ticketDTO.setEventId(eventDTO);

        // Stubbing the mapper to return a specific Ticket object when mapping TicketDTO
        Ticket mappedTicket = new Ticket();
        mappedTicket.setUser(user);
        mappedTicket.setEvent(event);
        when(ticketMapper.toEntity(any(TicketDTO.class))).thenReturn(mappedTicket);

        // Mock ticketRepository.save() to return the mapped Ticket object
        Ticket savedTicket = new Ticket();
        savedTicket.setId(1L);
        savedTicket.setStatus(Status.RESERVED);
        when(ticketRepository.save(any(Ticket.class))).thenReturn(savedTicket);

        // Stubbing the mapper to return a specific TicketDTO object when mapping Ticket
        TicketDTO savedTicketDTO = new TicketDTO();
        savedTicketDTO.setStatus(Status.RESERVED);
        when(ticketMapper.toDto(any(Ticket.class))).thenReturn(savedTicketDTO);

        // Call the method under test
        TicketDTO resultDTO = ticketService.addTicket(ticketDTO);

        // Assert the result or perform further verifications
        assertNotNull(resultDTO);
        assertEquals(Status.RESERVED, resultDTO.getStatus());

        // Verify that userRepository.findById() was called exactly once with ID 1
        verify(userRepository, times(1)).findById(1);

        // Verify that eventRepository.findById() was called exactly once with ID 1
        verify(eventRepository, times(1)).findById(Long.valueOf(1));

        // Verify that ticketRepository.save() was called exactly once with the mapped Ticket
        verify(ticketRepository, times(1)).save(mappedTicket);
    }





    @Test
    public void testGetTicketById_Success() throws TicketNotFoundException {
        Ticket ticket = new Ticket();
        ticket.setId(1L);

        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));
        when(ticketMapper.toDto(any(Ticket.class))).thenReturn(ticketDTO);

        TicketDTO foundTicket = ticketService.getTicketById(1L);

        assertNotNull(foundTicket);
        assertEquals(1L, foundTicket.getId());

        verify(ticketRepository, times(1)).findById(1L);
        verify(ticketMapper, times(1)).toDto(ticket);
    }

    @Test
    public void testGetTicketById_TicketNotFound() {
        when(ticketRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(TicketNotFoundException.class, () -> {
            ticketService.getTicketById(1L);
        });

        verify(ticketRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(ticketRepository, ticketMapper);
    }

    @Test
    public void testGetTicketsByUserEmail_Success() throws EntityNotFoundException {
        List<Ticket> tickets = new ArrayList<>();
        tickets.add(new Ticket());
        when(ticketRepository.findByUserEmail(any(String.class))).thenReturn(tickets);
        when(ticketMapper.toDto(any(Ticket.class))).thenReturn(ticketDTO);

        List<TicketDTO> foundTickets = ticketService.getTicketsByUserEmail("test@example.com");

        assertNotNull(foundTickets);
        assertFalse(foundTickets.isEmpty());

        verify(ticketRepository, times(1)).findByUserEmail("test@example.com");
        verify(ticketMapper, times(1)).toDto(any(Ticket.class));
    }

    @Test
    public void testGetTicketsByUserEmail_NoTicketsFound() {
        when(ticketRepository.findByUserEmail(any(String.class))).thenReturn(new ArrayList<>());

        assertThrows(EntityNotFoundException.class, () -> {
            ticketService.getTicketsByUserEmail("test@example.com");
        });

        verify(ticketRepository, times(1)).findByUserEmail("test@example.com");
        verifyNoMoreInteractions(ticketRepository, ticketMapper);
    }

    @Test
    public void testGetTicketsByUsername_Success() throws EntityNotFoundException {
        Optional<Ticket> ticketOptional = Optional.of(new Ticket());
        when(ticketRepository.findByUserUsername(any(String.class))).thenReturn(ticketOptional);
        when(ticketMapper.toDto(any(Ticket.class))).thenReturn(ticketDTO);

        List<TicketDTO> foundTickets = ticketService.getTicketsByUsername("testuser");

        assertNotNull(foundTickets);
        assertFalse(foundTickets.isEmpty());

        verify(ticketRepository, times(1)).findByUserUsername("testuser");
        verify(ticketMapper, times(1)).toDto(any(Ticket.class));
    }

    @Test
    public void testGetTicketsByUsername_NoTicketsFound() {
        when(ticketRepository.findByUserUsername(any(String.class))).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            ticketService.getTicketsByUsername("testuser");
        });

        verify(ticketRepository, times(1)).findByUserUsername("testuser");
        verifyNoMoreInteractions(ticketRepository, ticketMapper);
    }

    @Test
    public void testGetAllTickets_Success() throws Exception {
        List<Ticket> tickets = new ArrayList<>();
        tickets.add(new Ticket());
        when(ticketRepository.findAll()).thenReturn(tickets);
        when(ticketMapper.toDto(any(Ticket.class))).thenReturn(ticketDTO);

        List<TicketDTO> foundTickets = ticketService.getAllTickets();

        assertNotNull(foundTickets);
        assertFalse(foundTickets.isEmpty());

        verify(ticketRepository, times(1)).findAll();
        verify(ticketMapper, times(1)).toDto(any(Ticket.class));
    }

    @Test
    public void testGetTicketsByEventId_Success() throws Exception {
        List<Ticket> tickets = new ArrayList<>();
        tickets.add(new Ticket());
        when(ticketRepository.findByEventId(any(Long.class))).thenReturn(tickets);
        when(ticketMapper.toDto(any(Ticket.class))).thenReturn(ticketDTO);

        List<TicketDTO> foundTickets = ticketService.getTicketsByEventId(1L);

        assertNotNull(foundTickets);
        assertFalse(foundTickets.isEmpty());

        verify(ticketRepository, times(1)).findByEventId(1L);
        verify(ticketMapper, times(1)).toDto(any(Ticket.class));
    }

    @Test
    public void testGetTicketsByEventId_NoTicketsFound() {
        when(ticketRepository.findByEventId(any(Long.class))).thenReturn(new ArrayList<>());

        assertThrows(EntityNotFoundException.class, () -> {
            ticketService.getTicketsByEventId(1L);
        });

        verify(ticketRepository, times(1)).findByEventId(1L);
        verifyNoMoreInteractions(ticketRepository, ticketMapper);
    }

    @Test
    public void testDeleteTicket_Success() throws TicketNotFoundException {
        Ticket ticket = new Ticket();
        ticket.setId(1L);

        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));

        assertDoesNotThrow(() -> {
            ticketService.deleteTicket(1L);
        });

        verify(ticketRepository, times(1)).findById(1L);
        verify(ticketRepository, times(1)).delete(ticket);
    }

    @Test
    public void testDeleteTicket_TicketNotFound() {
        when(ticketRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(TicketNotFoundException.class, () -> {
            ticketService.deleteTicket(1L);
        });

        verify(ticketRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(ticketRepository);
    }
}
