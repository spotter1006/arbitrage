package com.eac.arbitrage.repository;

import com.eac.arbitrage.model.Result;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;


public interface ResultsRepository extends JpaRepository<Result, Long> {
    @Transactional void deleteByAnalysisId(Long analysisId);

    @Query(value = "SELECT DISTINCT pool FROM results",nativeQuery = true)
    List<String> getDistinctPools();

    @Query(value = "SELECT DISTINCT region FROM results WHERE pool = ?1",nativeQuery = true)
    List<String> getDistinctRegionByPool(String pool);

    @Query(value = "SELECT DISTINCT utc FROM results WHERE pool = ?1 ORDER BY 1 ASC",nativeQuery = true)
    List<Timestamp> getDistinctPoolTimes(String pool);

    @Query(value = "SELECT * FROM results WHERE pool = ?1 ORDER BY utc ASC", nativeQuery = true)
    List<Result> getByPool(String pool);

    @Query(value="SELECT * FROM results WHERE pool = ?1 and utc = ?2", nativeQuery = true)
    List<Result> getByPoolAndUtc(String Pool, Instant utc);


}
