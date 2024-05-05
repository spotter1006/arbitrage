package com.eac.arbitrage.service;
import com.eac.arbitrage.controller.AnalysisDTO;
import com.eac.arbitrage.model.Lmp;
import com.eac.arbitrage.repository.LmpRepository;

import java.time.Instant;
import java.util.List;

public class AnalyzeRunnable implements Runnable{
    AnalysisDTO analysis;
    LmpRepository lmpRepository;
    double scratch;
    public AnalyzeRunnable(AnalysisDTO analysis, LmpRepository inputRepository){
        this.analysis = analysis;
        this.lmpRepository = inputRepository;
        scratch = 0.0;
    }
    public void run(){
        try {
            List<Lmp> lmps = lmpRepository.getByRegionLikeAndUtcBetween(analysis.getRegion(), Instant.parse(analysis.getStartTime()), Instant.parse(analysis.getEndTime()));
            for(Lmp lmp : lmps){
                processLmp(lmp);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void processLmp(Lmp lmp){
        scratch += lmp.getPrice();
    }
}
