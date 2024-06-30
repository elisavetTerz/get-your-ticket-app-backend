package com.getyourticket.cf.rest;

import com.getyourticket.cf.authentication.jwt.JwtUtils;
import com.getyourticket.cf.dto.TicketDTO;
import com.getyourticket.cf.service.ITicketService;
import com.getyourticket.cf.service.exceptions.TicketNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class TicketControllerTest {

    @Mock
    private ITicketService ticketService;

    @Mock
    private JwtUtils jwtUtils;

    @InjectMocks
    private TicketController ticketController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(ticketController).build();
    }

    @Test
    public void testAddTicketSuccess() throws Exception {
        TicketDTO ticketDTO = new TicketDTO();
        ticketDTO.setPrice(BigDecimal.valueOf(100.0));

        when(jwtUtils.validateJwtToken(anyString())).thenReturn(true);
        when(ticketService.addTicket(any(TicketDTO.class))).thenReturn(ticketDTO);

        mockMvc.perform(post("/api/tickets/add-tickets")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer valid-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Concert\",\"price\":100.0}"))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.price").value(100.0));
    }

    @Test
    public void testAddTicketUnauthorized() throws Exception {
        when(jwtUtils.validateJwtToken(anyString())).thenReturn(false);

        mockMvc.perform(post("/api/tickets/add-tickets")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer invalid-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Concert\",\"price\":100.0}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testGetTicketByIdSuccess() throws Exception {
        TicketDTO ticketDTO = new TicketDTO();
        ticketDTO.setId(1L);

        when(ticketService.getTicketById(1L)).thenReturn(ticketDTO);

        mockMvc.perform(get("/api/tickets/get-ticket/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    public void testGetTicketByIdNotFound() throws Exception {
        when(ticketService.getTicketById(1L)).thenThrow(new EntityNotFoundException());

        mockMvc.perform(get("/api/tickets/get-ticket/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetTicketsByUsernameSuccess() throws Exception {
        TicketDTO ticketDTO1 = new TicketDTO();
        TicketDTO ticketDTO2 = new TicketDTO();

        List<TicketDTO> ticketDTOs = Arrays.asList(ticketDTO1, ticketDTO2);
        when(ticketService.getTicketsByUsername("user")).thenReturn(ticketDTOs);

        mockMvc.perform(get("/api/tickets/get-ticket-by-username/user"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testGetTicketsByUserEmailSuccess() throws Exception {
        TicketDTO ticketDTO1 = new TicketDTO();
        TicketDTO ticketDTO2 = new TicketDTO();

        List<TicketDTO> ticketDTOs = Arrays.asList(ticketDTO1, ticketDTO2);
        when(ticketService.getTicketsByUserEmail("user@example.com")).thenReturn(ticketDTOs);

        mockMvc.perform(get("/api/tickets/get-ticket-by-email/user@example.com"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testGetAllTickets() throws Exception {
        TicketDTO ticketDTO1 = new TicketDTO();
        TicketDTO ticketDTO2 = new TicketDTO();

        List<TicketDTO> ticketDTOs = Arrays.asList(ticketDTO1, ticketDTO2);
        when(ticketService.getAllTickets()).thenReturn(ticketDTOs);

        mockMvc.perform(get("/api/tickets/get-all-tickets"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testGetTicketsByEventIdSuccess() throws Exception {
        TicketDTO ticketDTO1 = new TicketDTO();
        TicketDTO ticketDTO2 = new TicketDTO();

        List<TicketDTO> ticketDTOs = Arrays.asList(ticketDTO1, ticketDTO2);
        when(ticketService.getTicketsByEventId(1L)).thenReturn(ticketDTOs);

        mockMvc.perform(get("/api/tickets/get-tickets-by-event/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testDeleteTicketSuccess() throws Exception {
        mockMvc.perform(delete("/api/tickets/delete-ticket/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteTicketNotFound() throws Exception {
        doThrow(new TicketNotFoundException("")).when(ticketService).deleteTicket(1L);

        mockMvc.perform(delete("/api/tickets/delete-ticket/1"))
                .andExpect(status().isNotFound());
    }
}
