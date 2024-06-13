package com.getyourticket.cf.repository;

import com.getyourticket.cf.model.Cart;
import com.getyourticket.cf.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Create repository for Cart Item entity
 * to handle database operations using Spring Data JPA.
 */
@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByCartId(Long cartId);
    List<CartItem> getAllByCart(Cart cart);
}
