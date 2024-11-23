package com.batherphilippa.pin_it_app_be.service;

import com.batherphilippa.pin_it_app_be.dto.GuestDTOIn;
import com.batherphilippa.pin_it_app_be.model.Guest;

public interface IGuestService {

    Guest saveGuest(GuestDTOIn guest);
}
