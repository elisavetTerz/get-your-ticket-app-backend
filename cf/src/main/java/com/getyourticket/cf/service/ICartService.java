package com.getyourticket.cf.service;

import com.getyourticket.cf.model.Cart;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;

public interface ICartService {
    Cart updateCart(Cart cart) throws EntityNotFoundException;
    Cart deleteCart(Long id) throws EntityNotFoundException;
}
