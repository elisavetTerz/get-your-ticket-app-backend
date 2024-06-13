package com.getyourticket.cf.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


/**
 * Cart Items Entity:
 * This table will store the relationship between the cart
 * and the tickets the user wants to purchase.
 */


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "CART_ITEMS")
public class CartItem {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @Column(nullable = true)
    private Integer quantity;

//    @Column(name = "created_at", columnDefinition = "TIMESTAMP")
//    private LocalDateTime createdAt;
//
//    @Column(name = "updated_at", columnDefinition = "TIMESTAMP")
//    private LocalDateTime updatedAt;

}
