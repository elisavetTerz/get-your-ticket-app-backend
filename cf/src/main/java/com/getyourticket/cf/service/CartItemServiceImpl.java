package com.getyourticket.cf.service;

import com.getyourticket.cf.model.CartItem;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@Slf4j
@RequiredArgsConstructor
public class CartItemServiceImpl implements ICartItemService{
    @Override
    public CartItem createCartItem(CartItem cartItem) {
        return null;
    }

    @Override
    public CartItem deleteCartItem(Long id) throws EntityNotFoundException {
        return null;
    }

    @Override
    public List<CartItem> getAllCartItems() throws EntityNotFoundException {
        return List.of();
    }

    @Override
    public CartItem getCartItemById(Long id) throws EntityNotFoundException {
        return null;
    }
}
