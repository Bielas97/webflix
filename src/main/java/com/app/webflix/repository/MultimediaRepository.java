package com.app.webflix.repository;

import com.app.webflix.model.entity.Multimedia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MultimediaRepository extends JpaRepository<Multimedia, Long> {
    Multimedia findByGenre(String genre);

    List<Multimedia> findAllOrOrderByName(String name);
    List<Multimedia> findAllOrOrderByGenre(String genre);
    List<Multimedia> findAllOrOrderByDirector(String director);
}
