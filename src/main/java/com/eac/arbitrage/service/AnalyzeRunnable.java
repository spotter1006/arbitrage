package com.eac.arbitrage.service;
import com.eac.arbitrage.controller.AnalysisDTO;
import com.eac.arbitrage.model.Lmp;
import com.eac.arbitrage.model.Result;
import com.eac.arbitrage.repository.LmpRepository;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import com.eac.arbitrage.repository.ResultsRepository;
import org.apache.commons.collections4.queue.CircularFifoQueue;

public class AnalyzeRunnable implements Runnable{

    AnalysisDTO analysis;
    LmpRepository lmpRepository;
    ResultsRepository resultsRepository;
    double averagePrice;
    double stateOfCharge;
    double runningRevenue;
    double totalRevenue;
    CircularFifoQueue<Lmp> latestRecords;
    Instant reportInstant;

    public AnalyzeRunnable(AnalysisDTO analysis, LmpRepository lmpRepository, ResultsRepository resultsRepository){
        this.analysis = analysis;
        this.lmpRepository = lmpRepository;
        this.resultsRepository = resultsRepository;
        averagePrice = 0.0;
        stateOfCharge = analysis.getCapacity() / 2.0;     // Start half-full
        latestRecords = new CircularFifoQueue<Lmp>(analysis.getAverageWindow());
        runningRevenue=0.0;
    }
    public void run(){
        try {
            List<Lmp> lmps = lmpRepository.getByRegionLikeAndUtcBetweenOrderByUtc(analysis.getRegion(), Instant.parse(analysis.getStartTime()), Instant.parse(analysis.getEndTime()));
            reportInstant = lmps.get(0).getUtc();
            for(Lmp lmp : lmps){
                processLmp(lmp, analysis.getId());
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void processLmp(Lmp lmp, Long analysisId){
        latestRecords.add(lmp);
        if(latestRecords.size() < analysis.getAverageWindow())   return; // Wait for average window number of records

        updateAverage();
        Instant priorInstant = (latestRecords.get(latestRecords.size()-2)).getUtc();
        double hours = Duration.between( priorInstant, lmp.getUtc()).get(ChronoUnit.SECONDS) / 3600.0;
        double MWs = analysis.getCapacity() * analysis.getChargeRate();
        double MWHs = MWs * hours;
        double rev = MWHs * lmp.getPrice();

        // TODO: replace efficiency with real losses based on SoC and power
        if(lmp.getPrice() > averagePrice && stateOfCharge >= MWHs){     // Sell
            stateOfCharge -= MWHs;
            runningRevenue += rev * analysis.getEfficiency();           // Losses don't contribute to revenue
            totalRevenue += rev * analysis.getEfficiency();
        } else if (lmp.getPrice() < averagePrice && stateOfCharge < analysis.getCapacity() - MWHs) {     // buy
            stateOfCharge += MWHs * analysis.getEfficiency();       // Losses detract from charging
            runningRevenue -= rev;                                  // But the energy still has to be paid for
            totalRevenue -= rev;
        }

        if(Duration.between(reportInstant, lmp.getUtc()).compareTo(Duration.ofHours(24)) >= 0){
            Result result = new Result();
            result.setAnalysisId(analysisId);
            result.setUtc(lmp.getUtc());
            result.setEnergy(stateOfCharge);
            result.setRevenue(runningRevenue);
            result.setTotalRevenue(totalRevenue);
            resultsRepository.save(result);

            reportInstant = lmp.getUtc();
            runningRevenue = 0.0;
        }

    }

    private void updateAverage(){
        Double total = 0.0;
        for(Lmp lmp : latestRecords){
            total += lmp.getPrice();
        }
        averagePrice=total/latestRecords.size();
    }
}
