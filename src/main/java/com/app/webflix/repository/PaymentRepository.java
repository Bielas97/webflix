package com.app.webflix.repository;

import com.app.webflix.model.dto.PaymentDto;
import com.app.webflix.model.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    void addOrUpdatePayment(PaymentDto paymentDto);
    void deletePayment(Long id);
    Optional<PaymentDto> getOnePayment(Long id);
    List<PaymentDto> getAll();
}
