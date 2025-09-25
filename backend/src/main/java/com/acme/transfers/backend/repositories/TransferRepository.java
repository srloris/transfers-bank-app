package com.acme.transfers.backend.repositories;

import com.acme.transfers.backend.entities.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransferRepository extends JpaRepository<Transfer, Long> {
}
