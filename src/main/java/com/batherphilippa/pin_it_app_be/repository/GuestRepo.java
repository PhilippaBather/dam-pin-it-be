package com.batherphilippa.pin_it_app_be.repository;

import com.batherphilippa.pin_it_app_be.model.Guest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GuestRepo  extends CrudRepository<Guest, Long> {
}
