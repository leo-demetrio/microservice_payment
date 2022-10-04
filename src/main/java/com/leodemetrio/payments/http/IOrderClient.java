package com.leodemetrio.payments.http;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("msorder")
public interface IOrderClient {

    @RequestMapping(method = RequestMethod.PUT, value = "/orders/{id}/pago")
    void updatePayment(@PathVariable Long id);
}
