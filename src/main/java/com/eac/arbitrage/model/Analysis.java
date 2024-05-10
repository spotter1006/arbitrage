package com.eac.arbitrage.model;

import com.eac.arbitrage.controller.AnalysisDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;


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
    Double capacity;
    @Temporal(TemporalType.TIMESTAMP)
    private Instant startTime;
    @Temporal(TemporalType.TIMESTAMP)
    private Instant endTime;
    String status;
    String outputInterval;
    Integer averageWindow;
    Double chargeRate;
    Double efficiency;

    public Analysis(AnalysisDTO dto){
        this.setId(dto.getId());
        this.setName(dto.getName());
        this.setPool(dto.getPool());
        this.setRegion(dto.getRegion());
        this.setStartTime(parse(dto.getStartTime()));
        this.setEndTime(parse(dto.getEndTime()));
        this.setCapacity(dto.getCapacity());
        this.setOutputInterval(dto.getOutputInterval());
        this.setStatus(dto.getStatus());
        this.setAverageWindow(dto.getAverageWindow());
        this.setChargeRate(dto.getChargeRate());
        this.setEfficiency(dto.getEfficiency());
    }
    public Analysis(Analysis other){
        this.setId(other.getId());
        this.setName(other.getName());
        this.setPool(other.getPool());
        this.setRegion(other.getRegion());
        this.setStartTime(other.getStartTime());
        this.setEndTime(other.getEndTime());
        this.setCapacity(other.getCapacity());
        this.setOutputInterval(other.getOutputInterval());
        this.setStatus(other.getStatus());
        this.setAverageWindow(other.getAverageWindow());
        this.setChargeRate(other.getChargeRate());
        this.setEfficiency(other.getEfficiency());
    }

    private Instant parse(String input){
        DateTimeFormatter f = DateTimeFormatter.ISO_DATE_TIME;
        LocalDateTime ldt = LocalDateTime.parse(input, f);
        ZoneId z = ZoneId.of("UTC");
        ZonedDateTime zdt = ldt.atZone(z);
        return zdt.toInstant();
    }
}