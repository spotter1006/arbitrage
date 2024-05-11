package com.eac.arbitrage.repository;

import com.eac.arbitrage.model.Result;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public interface ResultsRepository extends JpaRepository<Result, Long> {
    @Transactional void deleteByAnalysisId(Long analysisId);
    @Query(value = "SELECT DISTINCT pool FROM results",nativeQuery = true)
    List<String> getDistinctPools();
    @Query(value = "SELECT DISTINCT region FROM results WHERE pool = ?1",nativeQuery = true)
    List<String> getDistinctRegionByPool(String pool);
    List<Result> getByRegion(String region);
}
