package com.app.webflix.model.entity;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Multimedia {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String genre;
    private String director;
    private LocalDateTime dateTime;
    private String description;
    private Integer episodeNumber;
    private Integer rating;
    @ManyToMany(mappedBy = "watchList", fetch = FetchType.EAGER)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<User> users;
    @Transient
    private MultipartFile multipartFile;
    private String filename;

}
