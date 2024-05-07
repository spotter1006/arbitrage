package com.eac.arbitrage.repository;

import com.eac.arbitrage.model.Result;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;


public interface ResultsRepository extends JpaRepository<Result, Long> {
    @Transactional void deleteByAnalysisId(Long analysisId);
}
