package com.app.webflix.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private List<User> users;

}
