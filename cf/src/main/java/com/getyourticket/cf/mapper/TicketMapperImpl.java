package com.getyourticket.cf.mapper;

import com.getyourticket.cf.dto.EventDTO;
import com.getyourticket.cf.dto.TicketDTO;
import com.getyourticket.cf.dto.UserRegisterDTO;
import com.getyourticket.cf.model.Event;
import com.getyourticket.cf.model.Ticket;
import com.getyourticket.cf.model.User;
import org.springframework.stereotype.Component;

@Component
public class TicketMapperImpl implements TicketMapper {

    private final UserMapper userMapper;
    private final EventMapper eventMapper;
    public TicketMapperImpl(UserMapper userMapper, EventMapper eventMapper) {
        this.userMapper = userMapper;
        this.eventMapper = eventMapper;

    }
    @Override
    public TicketDTO toDto(Ticket ticket) {
        if (ticket == null) return null;

        UserRegisterDTO userDTO = userMapper.toDTO(ticket.getUser());
        EventDTO eventDTO = eventMapper.toDto(ticket.getEvent());

        return new TicketDTO(
                ticket.getId(),
                userDTO,
                eventDTO,
                ticket.getSeatNumber(),
                ticket.getPrice(),
                ticket.getStatus(),
                ticket.getCreatedAt(),
                ticket.getUpdatedAt()
        );
    }

    @Override
    public Ticket toEntity(TicketDTO ticketDTO) {
        if (ticketDTO ==  null) return null;

        User user = userMapper.toEntity(ticketDTO.getUserId());
        Event event = eventMapper.toEntity(ticketDTO.getEventId());

        Ticket ticket = new Ticket();
        ticket.setId(ticketDTO.getId());
        ticket.setUser(user);
        ticket.setEvent(event);
        ticket.setSeatNumber(ticketDTO.getSeatNumber());
        ticket.setPrice(ticketDTO.getPrice());
        ticket.setStatus(ticketDTO.getStatus());
        ticket.setCreatedAt(ticketDTO.getCreatedAt());
        ticket.setUpdatedAt(ticketDTO.getUpdatedAt());

        return ticket;
    }
}
