package com.app.webflix.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MultimediaDto {
    private Long id;
    private String name;
    private String genre;
    private String director;
    private LocalDateTime dateTime;

}
