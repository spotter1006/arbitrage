package com.eac.arbitrage.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "lmps")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Lmp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    Long regionId;
    Instant utc;
    Double price;
    Double congestion;
    Double loss;
    Double energy;
    String pool;
    String region;
}
