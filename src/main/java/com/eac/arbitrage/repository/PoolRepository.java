package com.eac.arbitrage.repository;

import com.eac.arbitrage.model.Pool;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PoolRepository extends JpaRepository<Pool, Long> {
    public Pool getByName(String name);
}
