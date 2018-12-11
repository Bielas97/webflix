package com.app.webflix.model.dto;

import com.app.webflix.model.entity.User;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

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
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<User> users;
    private MultipartFile multipartFile;
    private String filename;

}
