package com.leodemetrio.payments.service;

import com.leodemetrio.payments.dto.PaymentDto;
import com.leodemetrio.payments.model.Payment;
import com.leodemetrio.payments.model.Status;
import com.leodemetrio.payments.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@RequiredArgsConstructor
@Service
public class PaymentService {

    public static final Class<PaymentDto> PAYMENT_DTO = PaymentDto.class;
    public static final Class<Payment> PAYMENT = Payment.class;
    private final PaymentRepository paymentRepository;
    private final ModelMapper modelMapper;

    public Page<PaymentDto> findAll(Pageable pageable){
        return paymentRepository
                .findAll(pageable)
                .map(p -> modelMapper.map(p, PAYMENT_DTO));
    }

    public PaymentDto findById(Long id){
        Payment payment = paymentRepository.findById(id).orElseThrow(() -> new EntityNotFoundException());

        return modelMapper.map(payment, PAYMENT_DTO);
    }

    public PaymentDto create(PaymentDto paymentDto){
        Payment payment = modelMapper.map(paymentDto, PAYMENT);
        payment.setStatus(Status.CREATED);
        paymentRepository.save(payment);

        return modelMapper.map(payment, PAYMENT_DTO);
    }

    public PaymentDto update(Long id, PaymentDto paymentDto){
        findById(id);
        Payment payment = modelMapper.map(paymentDto, PAYMENT);
        payment.setId(id);
        paymentRepository.save(payment);

        return modelMapper.map(payment, PAYMENT_DTO);
    }

    public void delete(Long id){
        paymentRepository.deleteById(id);
    }

}