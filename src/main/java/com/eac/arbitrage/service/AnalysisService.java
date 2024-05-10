package com.eac.arbitrage.service;

import com.eac.arbitrage.model.Analysis;
import com.eac.arbitrage.repository.AnalysisRepository;
import com.eac.arbitrage.repository.PriceRepository;
import com.eac.arbitrage.repository.ResultsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class AnalysisService {
    private final AnalysisRepository analysisRepository;
    private final ExecutorService executorService;
    private final PriceRepository priceRepository;
    private final ResultsRepository resultsRepository;

    @Autowired
    AnalysisService(AnalysisRepository analysisRepository, PriceRepository priceRepository, ResultsRepository resultsRepository){
        this.analysisRepository=analysisRepository;
        this.executorService = Executors.newFixedThreadPool(1);
        this.priceRepository = priceRepository;
        this.resultsRepository = resultsRepository;
    }
    public Analysis getById(Long id){
        return analysisRepository.getReferenceById(id);
    }
    public List<Analysis> getAllAnalyses(){
       return analysisRepository.findAll();
    }
    public Analysis addOrUpdate(Analysis analysis){
        analysisRepository.save(analysis);
        return(analysis);
    }
    public void deleteResults(Long analysisId){
        resultsRepository.deleteByAnalysisId(analysisId);
    }
    public void startAnalysis(Analysis analysis){
        executorService.submit(new AnalyzeRunnable(analysis, priceRepository, resultsRepository));
    }


}
