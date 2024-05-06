package com.eac.arbitrage.service;
import com.eac.arbitrage.controller.AnalysisDTO;
import com.eac.arbitrage.model.Lmp;
import com.eac.arbitrage.repository.LmpRepository;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.apache.commons.collections4.queue.CircularFifoQueue;

public class AnalyzeRunnable implements Runnable{

    // TODO: convert these constants to input parameters in the analysis spec
    private final static Integer AVERAGE_WINDOW = 100;
    private final static double C_RATE = 0.5;
    private final static double EFFICIENCY = 0.90;

    AnalysisDTO analysis;
    LmpRepository lmpRepository;
    double averagePrice;
    double stateOfCharge;
    double runningRevenue;
    CircularFifoQueue<Lmp> latestRecords;

    public AnalyzeRunnable(AnalysisDTO analysis, LmpRepository inputRepository){
        this.analysis = analysis;
        this.lmpRepository = inputRepository;
        averagePrice = 0.0;
        stateOfCharge = analysis.getCapacity() / 2.0;     // Start half-full
        latestRecords = new CircularFifoQueue<Lmp>(AVERAGE_WINDOW);
        runningRevenue=0.0;

    }
    public void run(){
        try {
            List<Lmp> lmps = lmpRepository.getByRegionLikeAndUtcBetweenOrderByUtc(analysis.getRegion(), Instant.parse(analysis.getStartTime()), Instant.parse(analysis.getEndTime()));
            for(Lmp lmp : lmps){
                processLmp(lmp);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void processLmp(Lmp lmp){
        latestRecords.add(lmp);
        if(latestRecords.size() < AVERAGE_WINDOW)   return; // Wait for average window number of records

        updateAverage();

        Instant priorInstant = (latestRecords.get(latestRecords.size()-2)).getUtc();
        Duration duration = Duration.between( priorInstant, lmp.getUtc());
        double hours = duration.get(ChronoUnit.SECONDS) / 3600.0;

        double MWs = analysis.getCapacity() * C_RATE * EFFICIENCY;  // TODO: replace efficiency with real losses based on SoC and power
        double MWHs = MWs * hours;
        double rev = MWHs * lmp.getPrice();

        if(lmp.getPrice() > averagePrice && stateOfCharge >= MWHs){              // Sell
            stateOfCharge -= MWHs;
            runningRevenue += rev;
        } else if (lmp.getPrice() < averagePrice && stateOfCharge < analysis.getCapacity()) {     // buy
            stateOfCharge += MWHs;
            runningRevenue -= rev;
        }

    }
    void updateAverage(){
        Double total = 0.0;
        for(Lmp lmp : latestRecords){
            total += lmp.getPrice();
        }
        averagePrice=total/latestRecords.size();
    }
}
