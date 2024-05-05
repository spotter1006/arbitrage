package com.eac.arbitrage.repository;


import com.eac.arbitrage.model.Lmp;

import com.eac.arbitrage.model.Price;
import jakarta.persistence.QueryHint;
import org.hibernate.jpa.AvailableHints;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.stream.Stream;

@Repository
public interface LmpRepository extends JpaRepository<Lmp, Long> {
//    @QueryHints(
//            @QueryHint(name = AvailableHints.HINT_FETCH_SIZE, value = "1000")
//    )
//    @Query(value = "SELECT id, utc, price FROM lmps WHERE  region = ?1 AND utc BETWEEN ?2 AND ?3 ORDER BY utc ASC", nativeQuery = true)
//    Stream<Price> streamRegionPriceForTime( String region, String startTime, String endTime);

//    @Query(value = "SELECT * FROM lmps WHERE  region = ?1 AND utc BETWEEN ?2 AND ?3 ORDER BY utc ASC", nativeQuery = true)
//    List<Lmp> getForRegionBetween( String region, Instant startTime, Instant endTime);
    List<Lmp> getByRegionLikeAndUtcBetween( String region, Instant startTime, Instant endTime);
}
