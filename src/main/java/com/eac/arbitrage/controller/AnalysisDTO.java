package com.eac.arbitrage.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnalysisDTO {
    String name;
    String pool;
    String region;
    String startTime;
    String endTime;
    Integer capacity;
    String outputInterval;
    String status;
}
