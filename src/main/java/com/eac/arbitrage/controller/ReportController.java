package com.eac.arbitrage.controller;

import com.eac.arbitrage.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/report")

public class ReportController {
    ReportService reportService;
    @Autowired
    ReportController(ReportService reportService){
        this.reportService = reportService;
    }
    @PutMapping
    String runReport(@RequestBody ReportDTO reportDTO){
        String result="";
        reportService.createReport(reportDTO.getAnalysisId(),reportDTO.getOutputDirectory());
        return result;
    }
}
