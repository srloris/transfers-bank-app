package com.acme.transfers.backend.mapper;

import com.acme.transfers.backend.dto.TransferRequestDTO;
import com.acme.transfers.backend.dto.TransferResponseDTO;
import com.acme.transfers.backend.entities.Transfer;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TransferMapper {

    public Transfer toEntity(TransferRequestDTO requestDTO) {
        if (requestDTO == null) {
            return null;
        }

        Transfer transfer = new Transfer();
        transfer.setSourceAccount(requestDTO.getSourceAccount());
        transfer.setDestinationAccount(requestDTO.getDestinationAccount());
        transfer.setAmount(requestDTO.getAmount());
        transfer.setTransferDate(requestDTO.getTransferDate());
        transfer.setScheduledDate(LocalDateTime.now());

        return transfer;
    }

    public TransferResponseDTO toResponseDTO(Transfer transfer) {
        if (transfer == null) {
            return null;
        }

        TransferResponseDTO responseDTO = new TransferResponseDTO();
        responseDTO.setId(transfer.getId());
        responseDTO.setSourceAccount(transfer.getSourceAccount());
        responseDTO.setDestinationAccount(transfer.getDestinationAccount());
        responseDTO.setAmount(transfer.getAmount());
        responseDTO.setFee(transfer.getFee());
        responseDTO.setTransferDate(transfer.getTransferDate());
        responseDTO.setScheduledDate(transfer.getScheduledDate());

        if (transfer.getAmount() != null && transfer.getFee() != null) {
            responseDTO.setTotalAmount(transfer.getAmount().add(transfer.getFee()));
        }

        return responseDTO;
    }

    public List<TransferResponseDTO> toResponseDTOList(List<Transfer> transfers) {
        if (transfers == null) {
            return null;
        }

        return transfers.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }
}
