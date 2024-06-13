package com.getyourticket.cf.dto;

import lombok.Data;

@Data
public class CartItemDTO {
    private Long id;
    private Long cartId;
    private Long eventId;
    private Integer quantity;
}
