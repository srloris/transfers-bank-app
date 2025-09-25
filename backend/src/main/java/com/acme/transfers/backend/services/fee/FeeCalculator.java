package com.acme.transfers.backend.services.fee;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface FeeCalculator {

    BigDecimal calculate(BigDecimal amount, LocalDate transferDate);
}
