package com.getyourticket.cf.mapper;

import com.getyourticket.cf.dto.TicketDTO;
import com.getyourticket.cf.model.Ticket;

public interface TicketMapper {
    TicketDTO toDto(Ticket ticket);

    Ticket toEntity(TicketDTO ticketDTO);
}
