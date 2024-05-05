package com.eac.arbitrage.controller;

import com.eac.arbitrage.model.Analysis;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnalysisDTO {
    AnalysisDTO(Analysis analysis){
        this.setName(analysis.getName());
        this.setPool(analysis.getPool());
        this.setRegion(analysis.getRegion());
        this.setStartTime(analysis.getStartTime().toString());
        this.setEndTime(analysis.getEndTime().toString());
        this.setCapacity(analysis.getCapacity());
        this.setOutputInterval(analysis.getOutputInterval());
        this.setStatus(analysis.getStatus());
    }
    String name;
    String pool;
    String region;
    String startTime;
    String endTime;
    Integer capacity;
    String outputInterval;
    String status;
}
