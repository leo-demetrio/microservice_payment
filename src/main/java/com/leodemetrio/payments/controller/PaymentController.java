package com.leodemetrio.payments.controller;

import com.leodemetrio.payments.dto.PaymentDto;
import com.leodemetrio.payments.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;


@RequiredArgsConstructor
@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping
    public Page<PaymentDto> findAll(@PageableDefault(size = 10) Pageable pageable) {
        return paymentService.findAll(pageable);
    }


    @GetMapping("/{id}")
    public ResponseEntity<PaymentDto> findById(@PathVariable @NotNull Long id) {
        PaymentDto dto = paymentService.findById(id);

        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<PaymentDto> create(@RequestBody @Valid PaymentDto dto, UriComponentsBuilder uriBuilder) {
        PaymentDto paymentDto = paymentService.create(dto);
        URI address = uriBuilder.path("/payments/{id}").buildAndExpand(paymentDto.getId()).toUri();

        return ResponseEntity.created(address).body(paymentDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PaymentDto> update(@PathVariable @NotNull Long id, @RequestBody @Valid PaymentDto dto) {
        PaymentDto paymentDto = paymentService.update(id, dto);

        return ResponseEntity.ok(paymentDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<PaymentDto> remover(@PathVariable @NotNull Long id) {
        paymentService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/confirmar")
    public void confirmPayment(@PathVariable @NotNull Long id){
        paymentService.confirmPayment(id);
    }

}
