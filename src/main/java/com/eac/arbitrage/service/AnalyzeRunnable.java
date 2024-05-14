package com.eac.arbitrage.service;

import com.eac.arbitrage.model.Analysis;
import com.eac.arbitrage.model.Price;
import com.eac.arbitrage.model.Result;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import com.eac.arbitrage.repository.PriceRepository;
import com.eac.arbitrage.repository.ResultsRepository;

public class AnalyzeRunnable implements Runnable{

    Analysis analysis;
    PriceRepository priceRepository;
    ResultsRepository resultsRepository;

    double stateOfCharge;
    double runningRevenue;
    double totalRevenue;
    double dataIntervalHours;
    Instant reportInstant;

    public AnalyzeRunnable(Analysis analysis, PriceRepository priceRepository, ResultsRepository resultsRepository){
        this.analysis = analysis;
        this.priceRepository = priceRepository;
        this.resultsRepository = resultsRepository;

        stateOfCharge = analysis.getCapacity() / 2.0;     // Start half-full
        runningRevenue=0.0;
    }
    public void run(){
        try {

            List<Price> prices = priceRepository.getPriceHistoryForRegion(analysis.getAverageWindow()/2, analysis.getAverageWindow()/2, analysis.getStartTime(), analysis.getEndTime(), analysis.getRegion());
            reportInstant = prices.get(0).getUtc();
            dataIntervalHours = Duration.between( prices.get(0).getUtc(),prices.get(1).getUtc()).get(ChronoUnit.SECONDS) / 3600.0;
            for(Price price : prices){
                double MWs = analysis.getCapacity() * analysis.getChargeRate();
                double MWHs = MWs * dataIntervalHours;
                double rev = MWHs * price.getPrice();

                // TODO: replace efficiency with real losses based on SoC and power
                if(price.getPrice() > price.getAveragePrice() && stateOfCharge >= MWHs){     // Sell
                    stateOfCharge -= MWHs;
                    runningRevenue += rev * analysis.getEfficiency();           // Losses don't contribute to revenue
                    totalRevenue += rev * analysis.getEfficiency();
                } else if (price.getPrice() < price.getAveragePrice() && stateOfCharge < analysis.getCapacity() - MWHs) {     // buy
                    stateOfCharge += MWHs * analysis.getEfficiency();       // Losses detract from charging
                    runningRevenue -= rev;                                  // But the energy still has to be paid for
                    totalRevenue -= rev;
                }

                if(Duration.between(reportInstant, price.getUtc()).compareTo(Duration.ofHours(24)) >= 0){
                    Result result = new Result();
                    result.setAnalysisId(analysis.getId());
                    result.setPool(analysis.getPool());
                    result.setRegion(analysis.getRegion());
                    result.setUtc(price.getUtc());
                    result.setEnergy(stateOfCharge);
                    result.setRevenue(runningRevenue);
                    result.setTotalRevenue(totalRevenue);
                    resultsRepository.saveAndFlush(result);

                    reportInstant = price.getUtc();
                    runningRevenue = 0.0;
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }



}