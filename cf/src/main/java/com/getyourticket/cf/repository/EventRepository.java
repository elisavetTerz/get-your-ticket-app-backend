package com.getyourticket.cf.repository;

import com.getyourticket.cf.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

/**
 * Create repository for Event entity
 * to handle database operations using Spring Data JPA.
 */
@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    Optional<Event> findById(Long id);

    List<Event> findByTitle(String title); //Brings all the events with a specific title

    List<Event> findByDate(LocalDate date); //Brings all the events a specific day

    @Query("SELECT e FROM Event e WHERE LOWER(e.title) LIKE LOWER(CONCAT(:title, '%'))")
    List<Event> findByTitleStartsWithIgnoreCase(@Param("title") String title);

    List<Event> findByStartTime(LocalTime startTime); //Brings all the events a specific time
}
