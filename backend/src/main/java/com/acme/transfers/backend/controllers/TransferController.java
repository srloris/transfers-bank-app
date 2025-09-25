package com.acme.transfers.backend.controllers;

import com.acme.transfers.backend.dto.TransferRequestDTO;
import com.acme.transfers.backend.dto.TransferResponseDTO;
import com.acme.transfers.backend.services.TransferService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping(value = "/transfer")
@AllArgsConstructor
public class TransferController {

    private final TransferService service;

    @GetMapping(value = "/{id}")
    public ResponseEntity<TransferResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping
    public ResponseEntity<Page<TransferResponseDTO>> findAll(Pageable pageable) {
        return ResponseEntity.ok(service.findAll(pageable));
    }

    @PostMapping
    public ResponseEntity<TransferResponseDTO> create(@Valid @RequestBody TransferRequestDTO requestDTO) {
        TransferResponseDTO responseDTO = service.create(requestDTO);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(responseDTO.getId()).toUri();
        return ResponseEntity.created(uri).body(responseDTO);
    }
}
