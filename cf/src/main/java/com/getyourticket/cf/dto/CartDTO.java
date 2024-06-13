package com.getyourticket.cf.dto;

import lombok.Data;

import java.util.Set;

@Data
public class CartDTO {
    private Long id;
    private Long userId;
    private Set<CartItemDTO> items;
}
