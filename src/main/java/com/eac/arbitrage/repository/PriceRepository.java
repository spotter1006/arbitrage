package com.eac.arbitrage.repository;

import com.eac.arbitrage.model.Price;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.time.Instant;
import java.util.List;

public interface PriceRepository extends JpaRepository<Price, Long> {
    @Query(value = "SELECT id, utc, price, AVG(sum_price) OVER (ORDER BY utc ROWS BETWEEN ?1 PRECEDING AND ?2 FOLLOWING) AS average_price " +
            "FROM " +
            "(SELECT  id, utc, price, sum(price) as sum_price FROM lmps where utc between ?3 and ?4 and region = ?5 GROUP BY 1,2) a " +
            "ORDER BY utc ASC;", nativeQuery = true)
    List<Price> getPriceHistoryForRegion(Integer averagePreceding, Integer averageFollowing, Instant startTime, Instant endTime, String region);
}
