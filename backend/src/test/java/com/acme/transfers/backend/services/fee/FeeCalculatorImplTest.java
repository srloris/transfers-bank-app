package com.acme.transfers.backend.services.fee;

import com.acme.transfers.backend.services.Exceptions.InvalidTransferDateException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FeeCalculatorImplTest {

    private FeeCalculatorImpl feeCalculator;

    @BeforeEach
    void setUp() {
        feeCalculator = new FeeCalculatorImpl();
    }

    @Test
    void testCalculate_FeeForImmediateTransfer() {
        BigDecimal amount = new BigDecimal("1000.00");
        LocalDate transferDate = LocalDate.now(); // 0 dias
        BigDecimal expectedFee = new BigDecimal("28.00"); // 3 + 1000*0.025

        BigDecimal fee = feeCalculator.calculate(amount, transferDate);

        assertEquals(0, fee.compareTo(expectedFee));
    }

    @Test
    void testCalculate_FeeFor1To10Days() {
        BigDecimal amount = new BigDecimal("500.00");
        for (int days = 1; days <= 10; days++) {
            LocalDate transferDate = LocalDate.now().plusDays(days);
            BigDecimal fee = feeCalculator.calculate(amount, transferDate);
            assertEquals(0, fee.compareTo(new BigDecimal("12.00")), "Failed for days: " + days);
        }
    }

    @Test
    void testCalculate_FeeFor11To20Days() {
        BigDecimal amount = new BigDecimal("1000.00");
        for (int days = 11; days <= 20; days++) {
            LocalDate transferDate = LocalDate.now().plusDays(days);
            BigDecimal expectedFee = amount.multiply(new BigDecimal("0.082")).setScale(2, RoundingMode.HALF_UP);
            BigDecimal fee = feeCalculator.calculate(amount, transferDate);
            assertEquals(0, fee.compareTo(expectedFee), "Failed for days: " + days);
        }
    }

    @Test
    void testCalculate_FeeFor21To30Days() {
        BigDecimal amount = new BigDecimal("1000.00");
        for (int days = 21; days <= 30; days++) {
            LocalDate transferDate = LocalDate.now().plusDays(days);
            BigDecimal expectedFee = amount.multiply(new BigDecimal("0.069")).setScale(2, RoundingMode.HALF_UP);
            BigDecimal fee = feeCalculator.calculate(amount, transferDate);
            assertEquals(0, fee.compareTo(expectedFee), "Failed for days: " + days);
        }
    }

    @Test
    void testCalculate_FeeFor31To40Days() {
        BigDecimal amount = new BigDecimal("1000.00");
        for (int days = 31; days <= 40; days++) {
            LocalDate transferDate = LocalDate.now().plusDays(days);
            BigDecimal expectedFee = amount.multiply(new BigDecimal("0.047")).setScale(2, RoundingMode.HALF_UP);
            BigDecimal fee = feeCalculator.calculate(amount, transferDate);
            assertEquals(0, fee.compareTo(expectedFee), "Failed for days: " + days);
        }
    }

    @Test
    void testCalculate_FeeFor41To50Days() {
        BigDecimal amount = new BigDecimal("1000.00");
        for (int days = 41; days <= 50; days++) {
            LocalDate transferDate = LocalDate.now().plusDays(days);
            BigDecimal expectedFee = amount.multiply(new BigDecimal("0.017")).setScale(2, RoundingMode.HALF_UP);
            BigDecimal fee = feeCalculator.calculate(amount, transferDate);
            assertEquals(0, fee.compareTo(expectedFee), "Failed for days: " + days);
        }
    }

    @Test
    void testCalculate_InvalidTransferDate_ThrowsException() {
        BigDecimal amount = new BigDecimal("1000.00");
        LocalDate transferDate = LocalDate.now().plusDays(51);

        assertThrows(InvalidTransferDateException.class, () ->
                feeCalculator.calculate(amount, transferDate));
    }
}
