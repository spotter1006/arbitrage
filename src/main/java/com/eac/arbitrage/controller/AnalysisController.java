package com.eac.arbitrage.controller;

import com.eac.arbitrage.model.Analysis;
import com.eac.arbitrage.service.AnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping()
    String addAnalysis(@RequestBody AnalysisDTO analysisSpec){
        String result = "";
        Analysis analysis = new Analysis(analysisSpec);
        analysis.setStatus("CREATED");
        analysisService.addAnalysis(analysis);
        return result;
    }
    @PutMapping("/{analysisId}")
    String startAnalysis(@PathVariable Integer analysisId){
        String result;
        result = "Starting analysis " + analysisId;
        return result;

    }
}
