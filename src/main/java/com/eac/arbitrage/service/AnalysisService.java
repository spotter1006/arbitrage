package com.eac.arbitrage.service;

import com.eac.arbitrage.model.Analysis;
import com.eac.arbitrage.repository.AnalysisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class AnalysisService {
    private final AnalysisRepository analysisRepository;
    @Autowired
    AnalysisService(AnalysisRepository analysisRepository){
        this.analysisRepository=analysisRepository;
    }
    public List<Analysis> getAllAnalyses(){
       return analysisRepository.findAll();
    }
}
