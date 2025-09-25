package com.acme.transfers.backend.services;

import com.acme.transfers.backend.dto.TransferRequestDTO;
import com.acme.transfers.backend.dto.TransferResponseDTO;
import com.acme.transfers.backend.entities.Transfer;
import com.acme.transfers.backend.mapper.TransferMapper;
import com.acme.transfers.backend.repositories.TransferRepository;
import com.acme.transfers.backend.services.Exceptions.InvalidTransferDateException;
import com.acme.transfers.backend.services.Exceptions.TransferNotFoundException;
import com.acme.transfers.backend.services.fee.FeeCalculator;
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
    private final FeeCalculator feeCalculator;

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

        BigDecimal fee = feeCalculator.calculate(transfer.getAmount(), transfer.getTransferDate());
        transfer.setFee(fee);

        return mapper.toResponseDTO(repository.save(transfer));
    }

}
