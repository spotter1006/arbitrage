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
        this.setId(analysis.getId());
        this.setName(analysis.getName());
        this.setPool(analysis.getPool());
        this.setRegion(analysis.getRegion());
        this.setStartTime(analysis.getStartTime().toString());
        this.setEndTime(analysis.getEndTime().toString());
        this.setCapacity(analysis.getCapacity());
        this.setOutputInterval(analysis.getOutputInterval());
        this.setStatus(analysis.getStatus());
    }
    Long id;
    String name;
    String pool;
    String region;
    String startTime;
    String endTime;
    Double capacity;
    String outputInterval;
    String status;
}
