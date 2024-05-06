package com.eac.arbitrage.service;

import com.eac.arbitrage.repository.ResultsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ResultService {
    private ResultsRepository resultsRepository;
    @Autowired
    ResultService(ResultsRepository resultsRepository){
        this.resultsRepository = resultsRepository;
    }
}
