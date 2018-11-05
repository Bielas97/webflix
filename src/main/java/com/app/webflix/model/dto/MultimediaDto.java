package com.app.webflix.model.dto;

import com.app.webflix.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MultimediaDto {
    private Long id;
    private String name;
    private String genre;
    private String director;
    private LocalDateTime dateTime;
    private String description;
    private Integer episodeNumber;
    private Integer rating;
    private List<User> users;

}
