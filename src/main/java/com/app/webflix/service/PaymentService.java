package com.app.webflix.service;

import com.app.webflix.model.dto.PaymentDto;
import com.app.webflix.model.entity.Payment;
import com.app.webflix.repository.PaymentRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PaymentService {
    private PaymentRepository paymentRepository;
    private ModelMapper modelMapper;

    public PaymentService(PaymentRepository paymentRepository, ModelMapper modelMapper) {
        this.paymentRepository = paymentRepository;
        this.modelMapper = modelMapper;
    }

    public void addOrUpdatePayment(PaymentDto paymentDto){
        paymentRepository.save(modelMapper.map(paymentDto, Payment.class));
    }
    public void deletePayment(Long id){
        paymentRepository.deleteById(id);
    }
    Optional<PaymentDto> getOnePayment(Long id){
        return paymentRepository.findById(id).map(c -> modelMapper.map(c, PaymentDto.class));
    }
    List<PaymentDto> getAll(){
        return paymentRepository.findAll()
                .stream()
                .map(c -> modelMapper.map(c, PaymentDto.class))
                .collect(Collectors.toList());
}
}
