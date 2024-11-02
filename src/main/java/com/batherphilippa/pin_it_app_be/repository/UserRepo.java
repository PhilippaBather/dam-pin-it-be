package com.batherphilippa.pin_it_app_be.repository;

import com.batherphilippa.pin_it_app_be.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

/**
 * UserRepo - the User Repository containing additional helper methods.
 */
@Repository
public interface UserRepo extends CrudRepository<User, Long> {

    Set<User> findAll();
    Optional<User> findByEmail(String email);
    Optional<User> findById(long userId);

    @Query(value = "SELECT * FROM users u WHERE u.email = :email AND u.password = :password", nativeQuery = true)
    Optional<User> findByEmailAndPasswordNativeSQL(String email, String password);
}
