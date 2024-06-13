package com.getyourticket.cf.service;

import com.getyourticket.cf.model.Cart;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CartServiceImpl implements ICartService {
    @Override
    public Cart updateCart(Cart cart) throws EntityNotFoundException {
        return null;
    }

    @Override
    public Cart deleteCart(Long id) throws EntityNotFoundException {
        return null;
    }
}
