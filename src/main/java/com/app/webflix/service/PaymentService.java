package com.app.webflix.service;

import com.app.webflix.model.dto.PaymentDto;
import com.app.webflix.model.entity.Payment;
import com.app.webflix.repository.PaymentRepository;
import org.apache.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PaymentService {
    private PaymentRepository paymentRepository;
    private ModelMapper modelMapper;
    private static final Logger LOGGER = Logger.getLogger(MultimediaService.class);
    public PaymentService(PaymentRepository paymentRepository, ModelMapper modelMapper) {
        this.paymentRepository = paymentRepository;
        this.modelMapper = modelMapper;
    }

    public void addOrUpdatePayment(PaymentDto paymentDto){
        LOGGER.debug("Adding or updating payments");
        paymentRepository.save(modelMapper.map(paymentDto, Payment.class));
    }
    public void deletePayment(Long id){
        paymentRepository.deleteById(id);
    }
    Optional<PaymentDto> getOnePayment(Long id){
        LOGGER.debug("Deleting payment");
        return paymentRepository.findById(id).map(c -> modelMapper.map(c, PaymentDto.class));
    }
    List<PaymentDto> getAll(){
        LOGGER.debug("Getting all payments");
        return paymentRepository.findAll()
                .stream()
                .map(c -> modelMapper.map(c, PaymentDto.class))
                .collect(Collectors.toList());
}
}
