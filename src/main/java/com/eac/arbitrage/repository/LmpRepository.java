package com.eac.arbitrage.repository;

import com.eac.arbitrage.model.Lmp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;


@Repository
public interface LmpRepository extends JpaRepository<Lmp, Long> {
    List<Lmp> getByRegionLikeAndUtcBetweenOrderByUtc( String region, Instant startTime, Instant endTime);
}
