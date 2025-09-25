package com.acme.transfers.backend.services;

import com.acme.transfers.backend.dto.TransferRequestDTO;
import com.acme.transfers.backend.dto.TransferResponseDTO;
import com.acme.transfers.backend.entities.Transfer;
import com.acme.transfers.backend.mapper.TransferMapper;
import com.acme.transfers.backend.repositories.TransferRepository;
import com.acme.transfers.backend.services.Exceptions.InvalidTransferDateException;
import com.acme.transfers.backend.services.Exceptions.TransferNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
@AllArgsConstructor
public class TransferService {

    private final TransferRepository repository;
    private final TransferMapper mapper;

    @Transactional(readOnly = true)
    public TransferResponseDTO findById(Long id) {
        Transfer transfer = repository.findById(id)
                .orElseThrow(() -> new TransferNotFoundException("Transfer not found"));
        return mapper.toResponseDTO(transfer);
    }

    @Transactional(readOnly = true)
    public Page<TransferResponseDTO> findAll(Pageable pageable) {
        Page<Transfer> result = repository.findAll(pageable);
        return result.map(mapper::toResponseDTO);
    }

    @Transactional
    public TransferResponseDTO create(TransferRequestDTO requestDTO) {
        Transfer transfer = mapper.toEntity(requestDTO);

        BigDecimal fee = calculateFee(transfer.getAmount(), transfer.getTransferDate());
        transfer.setFee(fee);

        return mapper.toResponseDTO(repository.save(transfer));
    }

    private BigDecimal calculateFee(BigDecimal amount, LocalDate transferDate) {
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

        BigDecimal totalFee = fixedFee.add(percentageFee);
        return totalFee.setScale(2, RoundingMode.HALF_UP);
    }

}
