package com.eac.arbitrage.controller;

import com.eac.arbitrage.model.Analysis;
import com.eac.arbitrage.service.AnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/analysis")
public class AnalysisController {
    private final AnalysisService analysisService;

    @Autowired
    AnalysisController(AnalysisService analysisService){
        this.analysisService=analysisService;
    }
    @GetMapping
    List<Analysis> getAllAnalyses(){
        return analysisService.getAllAnalyses();
    }
}
