package com.itbam.pixceler.service.model;

import com.itbam.pixceler.BuildConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class OrderCloseDTO {

    private int orderId;
    private int userCashierId;
    private List<PaymentDTO> payments = new ArrayList<>();
    private String serial;

    public OrderCloseDTO() {
        this.serial = BuildConfig.SERIAL_NUMBER;
        this.payments.add(setPayment());
        this.orderId = Order.id;
        this.userCashierId = 1;
    }

    @AllArgsConstructor
    private class PaymentDTO {
        private Integer cardBrandId;
        private int id;
        private String tid;
        private String transactionStatus;
        private double value;
    }

    public PaymentDTO setPayment() {
        Random random = new Random();
        int randomNum = random.nextInt(100000);
        randomNum++;

        return new PaymentDTO(
                null,
                13,
                String.valueOf(randomNum),
                "APPROVED",
                Order.total
        );
    }

}
