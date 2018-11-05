package com.app.webflix.service;

import com.app.webflix.model.dto.PaymentDto;
import com.app.webflix.model.entity.Payment;
import com.app.webflix.repository.PaymentRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

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
}
