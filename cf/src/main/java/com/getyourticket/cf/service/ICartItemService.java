package com.getyourticket.cf.service;

import com.getyourticket.cf.model.CartItem;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;

public interface ICartItemService {
    CartItem createCartItem(CartItem cartItem);

    CartItem deleteCartItem(Long id) throws EntityNotFoundException;

    List<CartItem> getAllCartItems() throws EntityNotFoundException;

    CartItem getCartItemById(Long id) throws EntityNotFoundException;
}
