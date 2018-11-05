package com.app.webflix.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Multimedia {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String genre;
    private String director;
    private LocalDateTime dateTime;

}
