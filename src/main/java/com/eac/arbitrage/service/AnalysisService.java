package com.eac.arbitrage.service;

import com.eac.arbitrage.model.Analysis;
import com.eac.arbitrage.controller.AnalysisDTO;
import com.eac.arbitrage.repository.AnalysisRepository;
import com.eac.arbitrage.repository.LmpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class AnalysisService {
    private final AnalysisRepository analysisRepository;
    private final ExecutorService executorService;
    private final LmpRepository lmpRepository;

    @Autowired
    AnalysisService(AnalysisRepository analysisRepository, LmpRepository lmpRepository){
        this.analysisRepository=analysisRepository;
        this.executorService = Executors.newSingleThreadExecutor();
        this.lmpRepository = lmpRepository;
    }
    public Analysis getById(Long id){
        return analysisRepository.getReferenceById(id);
    }
    public List<Analysis> getAllAnalyses(){
       return analysisRepository.findAll();
    }
    public Analysis addAnalysis(Analysis analysis){
        analysisRepository.save(analysis);
        return(analysis);
    }
    public void startAnalysis(AnalysisDTO analysis){
        executorService.submit(new AnalyzeRunnable(analysis, lmpRepository));
    }
}
