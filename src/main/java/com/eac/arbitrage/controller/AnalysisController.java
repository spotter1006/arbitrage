package com.eac.arbitrage.controller;

import com.eac.arbitrage.model.Analysis;
import com.eac.arbitrage.model.Lmp;
import com.eac.arbitrage.service.AnalysisService;
import com.eac.arbitrage.service.BessAnalyzerService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/analysis")
public class AnalysisController {
    private final AnalysisService analysisService;
    private final BessAnalyzerService bessAnalyzerService;
    @Autowired
    AnalysisController(AnalysisService analysisService, BessAnalyzerService bessAnalyzerService){
        this.analysisService=analysisService;
        this.bessAnalyzerService = bessAnalyzerService;
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
    String startAnalysis(@PathVariable Long analysisId){
        String result;

        try {

            Analysis analysis = analysisService.getById(analysisId);
            result = "Starting analysis " + analysisId + ". check later for results ";
            bessAnalyzerService.startAnalysis(new AnalysisDTO(analysis));
        }catch(EntityNotFoundException e){
            result = "Analysis with ID " + analysisId + " not found";
        }
        return result;

    }
}
