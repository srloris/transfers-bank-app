package com.acme.transfers.backend.services;

import com.acme.transfers.backend.dto.TransferRequestDTO;
import com.acme.transfers.backend.dto.TransferResponseDTO;
import com.acme.transfers.backend.entities.Transfer;
import com.acme.transfers.backend.mapper.TransferMapper;
import com.acme.transfers.backend.repositories.TransferRepository;
import com.acme.transfers.backend.services.Exceptions.InvalidTransferDateException;
import com.acme.transfers.backend.services.Exceptions.TransferNotFoundException;
import com.acme.transfers.backend.services.fee.FeeCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransferServiceTest {

    @Mock
    private TransferRepository repository;

    @Mock
    private TransferMapper mapper;

    @Mock
    private FeeCalculator feeCalculator;

    @InjectMocks
    private TransferService service;

    private Transfer transfer;
    private TransferRequestDTO requestDTO;
    private TransferResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        transfer = new Transfer();
        transfer.setId(1L);
        transfer.setAmount(new BigDecimal("1000.00"));
        transfer.setTransferDate(LocalDate.now());

        requestDTO = new TransferRequestDTO();
        requestDTO.setAmount(new BigDecimal("1000.00"));
        requestDTO.setTransferDate(LocalDate.now());

        responseDTO = new TransferResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setAmount(new BigDecimal("1000.00"));
    }

    @Test
    void testFindById_Success() {
        when(repository.findById(1L)).thenReturn(Optional.of(transfer));
        when(mapper.toResponseDTO(transfer)).thenReturn(responseDTO);

        TransferResponseDTO result = service.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(repository).findById(1L);
        verify(mapper).toResponseDTO(transfer);
    }

    @Test
    void testFindById_NotFound() {
        when(repository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(TransferNotFoundException.class, () -> service.findById(2L));
        verify(repository).findById(2L);
        verifyNoInteractions(mapper);
    }

    @Test
    void testCreate_ValidTransfer() {
        BigDecimal mockedFee = new BigDecimal("28.00");

        when(mapper.toEntity(requestDTO)).thenReturn(transfer);
        when(feeCalculator.calculate(transfer.getAmount(), transfer.getTransferDate())).thenReturn(mockedFee);
        when(repository.save(transfer)).thenReturn(transfer);
        when(mapper.toResponseDTO(transfer)).thenReturn(responseDTO);

        TransferResponseDTO result = service.create(requestDTO);

        assertNotNull(result);
        assertEquals(responseDTO.getId(), result.getId());

        // Verifica se a fee foi definida corretamente pelo mock
        assertEquals(0, transfer.getFee().compareTo(mockedFee));

        verify(feeCalculator).calculate(transfer.getAmount(), transfer.getTransferDate());
        verify(repository).save(transfer);
        verify(mapper).toResponseDTO(transfer);
    }

    @Test
    void testCreate_InvalidTransferDate_ThrowsException() {
        requestDTO.setTransferDate(LocalDate.now().plusDays(60));
        when(mapper.toEntity(requestDTO)).thenReturn(transfer);
        transfer.setTransferDate(requestDTO.getTransferDate());

        when(feeCalculator.calculate(transfer.getAmount(), transfer.getTransferDate()))
                .thenThrow(new InvalidTransferDateException("Transfer date not allowed"));

        assertThrows(InvalidTransferDateException.class, () -> service.create(requestDTO));

        verify(feeCalculator).calculate(transfer.getAmount(), transfer.getTransferDate());
        verifyNoInteractions(repository);
    }
}
