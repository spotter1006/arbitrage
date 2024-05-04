package com.eac.arbitrage.service;

import com.eac.arbitrage.model.Region;
import com.eac.arbitrage.repository.RegionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RegionService {
    private final RegionRepository regionRepository;
    @Autowired
    RegionService(RegionRepository regionRepository){
        this.regionRepository = regionRepository;
    }
    public List<Region> getAllregions(){
        return regionRepository.findAll();
    }
}
