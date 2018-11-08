package com.app.webflix.service;


import com.app.webflix.model.dto.MultimediaDto;
import com.app.webflix.model.entity.Multimedia;
import com.app.webflix.repository.MultimediaRepository;
import org.apache.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MultimediaService {
    private static final Logger LOGGER = Logger.getLogger(MultimediaService.class);

    private MultimediaRepository multimediaRepository;
    private ModelMapper modelMapper;

    public MultimediaService(MultimediaRepository multimediaRepository, ModelMapper modelMapper) {
        this.multimediaRepository = multimediaRepository;
        this.modelMapper = modelMapper;
    }

    public void addOrUpdateMultimedia(MultimediaDto multimediaDto) {
        LOGGER.info("Adding or updating multimedia");
        multimediaRepository.save(modelMapper.map(multimediaDto, Multimedia.class));
    }

    public List<MultimediaDto> getAll() {
        LOGGER.info("Retrieving list of all movies");
        return multimediaRepository.findAll()
                .stream()
                .map(m -> modelMapper.map(m, MultimediaDto.class))
                .collect(Collectors.toList());
    }

    public Optional<MultimediaDto> getOneMultimedia(Long id){
        LOGGER.info("Getting movie by ID");
        return multimediaRepository.findById(id).map(m -> modelMapper.map(m, MultimediaDto.class));
    }

    public MultimediaDto getByGenre(String genre){
        LOGGER.info("Getting movies by genre");
        return modelMapper.map(multimediaRepository.findByGenre(genre), MultimediaDto.class);
    }

    public void deleteMultimedia(Long id){
        LOGGER.info("Deleting move with id = "+id);
        multimediaRepository.deleteById(id);
    }
}
