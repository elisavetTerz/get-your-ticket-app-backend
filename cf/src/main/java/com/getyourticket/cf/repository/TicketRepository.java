package com.getyourticket.cf.repository;

import com.getyourticket.cf.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Create repository for Ticket entity
 * to handle database operations using Spring Data JPA.
 */
@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByUserId(Integer userId);
    List<Ticket> findByEventId(Long eventId);
    List<Ticket> findByUserEmail(String email);
    List<Ticket> findByUserLastname(String lastname);
}
