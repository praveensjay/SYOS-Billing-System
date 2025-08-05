package com.syos.utils;

import java.math.BigDecimal;

public class DiscountCalculator {

    public BigDecimal calculateDiscount(BigDecimal totalPrice, double discountRate) {
        if (totalPrice == null || discountRate < 0) {
            throw new IllegalArgumentException("Invalid discount parameters");
        }
        return totalPrice.multiply(BigDecimal.valueOf(discountRate / 100));
    }
}
