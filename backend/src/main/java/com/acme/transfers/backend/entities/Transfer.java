package com.acme.transfers.backend.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_transfers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transfer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 10)
    private String sourceAccount;

    @Column(nullable = false, length = 10)
    private String destinationAccount;

    @Column(nullable = false, scale = 2, precision = 19)
    private BigDecimal amount;

    @Column(nullable = false, scale = 2, precision = 19)
    private BigDecimal fee;

    @Column(nullable = false)
    private LocalDate transferDate;

    @Column(nullable = false)
    private LocalDateTime scheduledDate;

}
