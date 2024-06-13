package com.getyourticket.cf.repository;

import com.getyourticket.cf.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Create repository for User entity
 * to handle database operations using Spring Data JPA.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
//    Custom queries
    List<User> findByLastnameStartingWith(String lastname);
    User findUserById(Integer id);
    Optional<User> findByUsername(String username);
    Optional<User> findByEmailAndPassword(String email, String password);

}
