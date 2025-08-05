package com.syos.utils;

import java.math.BigDecimal;

public class TaxCalculator {
    private static final BigDecimal TAX_RATE = BigDecimal.valueOf(0.02); // 2% tax

    public BigDecimal calculateTax(BigDecimal totalPrice) {
        if (totalPrice == null || totalPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Invalid total price for tax calculation");
        }
        return totalPrice.multiply(TAX_RATE);
    }
}
