package com.eac.arbitrage.service;

import com.eac.arbitrage.controller.AnalysisDTO;
import com.eac.arbitrage.repository.LmpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class BessAnalyzerService {
    private final ExecutorService executorService;
    private final LmpRepository lmpRepository;

    @Autowired
    BessAnalyzerService(LmpRepository lmpRepository){
        this.executorService = Executors.newSingleThreadExecutor();
        this.lmpRepository = lmpRepository;
    }

    public void startAnalysis(AnalysisDTO analysis){
        executorService.submit(new AnalyzeRunnable(analysis, lmpRepository));
    }
}


