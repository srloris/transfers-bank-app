package com.acme.transfers.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferRequestDTO {

    @NotBlank(message = "Source account is required")
    @Size(min = 10, max = 10, message = "Source account must have exactly 10 characters")
    private String sourceAccount;

    @NotBlank(message = "Destination account is required")
    @Size(min = 10, max = 10, message = "Destination account must have exactly 10 characters")
    private String destinationAccount;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    @Digits(integer = 17, fraction = 2, message = "Amount format is invalid")
    private BigDecimal amount;

    @NotNull(message = "Transfer date is required")
    @FutureOrPresent(message = "Transfer date must be today or in the future")
    private LocalDate transferDate;

}
