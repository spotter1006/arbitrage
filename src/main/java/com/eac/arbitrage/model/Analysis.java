package com.eac.arbitrage.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;


@Entity
@Table(name = "Analysis")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Analysis {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String pool;
    private String region;
    Integer capacity;
    @Temporal(TemporalType.TIMESTAMP)
    private Instant startTime;
    @Temporal(TemporalType.TIMESTAMP)
    private Instant endTime;
    String status;
}