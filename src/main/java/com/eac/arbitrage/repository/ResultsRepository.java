package com.eac.arbitrage.repository;

import com.eac.arbitrage.model.Result;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ResultsRepository extends JpaRepository<Result, Long> {
}
