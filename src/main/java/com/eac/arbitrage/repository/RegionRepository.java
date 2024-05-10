package com.eac.arbitrage.repository;

import com.eac.arbitrage.model.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegionRepository extends JpaRepository<Region, Long> {
    public List<Region> getByPoolid(Long poolId);
    public Region getByName(String name);
}
