package com.getyourticket.cf.repository;

import com.getyourticket.cf.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Create repository for Cart entity
 * to handle database operations using Spring Data JPA.
 */
@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findById(Long id);
    Cart findByUserId(Integer userId);
}
