package com.leodemetrio.payments.service;

import com.leodemetrio.payments.dto.PaymentDto;
import com.leodemetrio.payments.http.IOrderClient;
import com.leodemetrio.payments.model.Payment;
import com.leodemetrio.payments.model.Status;
import com.leodemetrio.payments.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PaymentService {

    public static final Class<PaymentDto> PAYMENT_DTO = PaymentDto.class;
    public static final Class<Payment> PAYMENT = Payment.class;
    private final PaymentRepository paymentRepository;
    private final ModelMapper modelMapper;
    private final IOrderClient iOrderClient;

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

    public void confirmPayment(Long id){
        Optional<Payment> payment = paymentRepository.findById(id);

        if (!payment.isPresent()) {
            throw new EntityNotFoundException();
        }

        payment.get().setStatus(Status.CONFIRMED);
        paymentRepository.save(payment.get());
        iOrderClient.updatePayment(payment.get().getOrderId());
    }

    public void updateStatus(Long id) {
        Optional<Payment> payment = paymentRepository.findById(id);

        if (!payment.isPresent()) {
            throw new EntityNotFoundException();
        }

        payment.get().setStatus(Status.CONFIRMED_LESS_INTEGRATION);
        paymentRepository.save(payment.get());
    }
}
