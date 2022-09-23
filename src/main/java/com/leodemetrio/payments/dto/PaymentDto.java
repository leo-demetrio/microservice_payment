package com.leodemetrio.payments.dto;

import com.leodemetrio.payments.model.Status;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class PaymentDto {

    private Long id;
    private BigDecimal value;
    private String name;
    private String number;
    private String expiration;
    private String code;
    private Status status;
    private Long orderId;
    private Long formOfPaymentId;
}
