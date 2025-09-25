package com.acme.transfers.backend.services.fee;

import com.acme.transfers.backend.services.Exceptions.InvalidTransferDateException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
@Component
public class FeeCalculatorImpl implements FeeCalculator {

    @Override
    public BigDecimal calculate(BigDecimal amount, LocalDate transferDate) {
        long daysBetween = ChronoUnit.DAYS.between(LocalDate.now(), transferDate);

        BigDecimal fixedFee;
        BigDecimal percentageFee;

        if (daysBetween == 0) {
            fixedFee = new BigDecimal("3.00");
            percentageFee = amount.multiply(new BigDecimal("0.025"));
        } else if (daysBetween >= 1 && daysBetween <= 10) {
            fixedFee = new BigDecimal("12.00");
            percentageFee = BigDecimal.ZERO;
        } else if (daysBetween >= 11 && daysBetween <= 20) {
            fixedFee = BigDecimal.ZERO;
            percentageFee = amount.multiply(new BigDecimal("0.082"));
        } else if (daysBetween >= 21 && daysBetween <= 30) {
            fixedFee = BigDecimal.ZERO;
            percentageFee = amount.multiply(new BigDecimal("0.069"));
        } else if (daysBetween >= 31 && daysBetween <= 40) {
            fixedFee = BigDecimal.ZERO;
            percentageFee = amount.multiply(new BigDecimal("0.047"));
        } else if (daysBetween >= 41 && daysBetween <= 50) {
            fixedFee = BigDecimal.ZERO;
            percentageFee = amount.multiply(new BigDecimal("0.017"));
        } else {
            throw new InvalidTransferDateException(
                    String.format("Transfer date not allowed. Days between today and transfer: %d. " +
                            "Allowed range: 0-50 days from today.", daysBetween));
        }

        return fixedFee.add(percentageFee).setScale(2, RoundingMode.HALF_UP);
    }
}
