package com.batherphilippa.pin_it_app_be.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTOIn {
    private String comment;
    private LocalDate creationDate;

}
