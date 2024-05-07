package com.eac.arbitrage.controller;

import com.eac.arbitrage.model.Analysis;
import com.eac.arbitrage.service.AnalysisService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
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
        analysisService.addOrUpdate(analysis);
        return result;
    }
    // This method updates an existing analysis if a spec is found in the body spec and runs it
    @PutMapping("/{analysisId}")
    String startAnalysis(@PathVariable Long analysisId, @RequestBody AnalysisDTO analysisSpec){
        String result;
        try {
            Analysis analysis = analysisService.getById(analysisId);
            if(analysisSpec != null){
                Analysis analysisUpdate = new Analysis(analysisSpec);
                analysisUpdate.setId(analysisId);
                analysisService.addOrUpdate(analysisUpdate);
            }
            result = "Starting analysis " + analysisId + ". check later for results ";
            analysisService.startAnalysis(new AnalysisDTO(analysis));
        }catch(EntityNotFoundException e){
            result = "Analysis with ID " + analysisId + " not found";
        }
        return result;
    }
}
