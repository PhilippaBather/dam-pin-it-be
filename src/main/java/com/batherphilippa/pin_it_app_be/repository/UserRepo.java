package com.batherphilippa.pin_it_app_be.repository;

import com.batherphilippa.pin_it_app_be.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

/**
 * UserRepo - the User Repository containing additional helper methods.
 */
@Repository
public interface UserRepo extends CrudRepository<User, Long> {

    @NonNull
    @Override
    Set<User> findAll();
    Optional<User> findByEmail(String email);
    Optional<User> findById(long userId);

}
