package com.eac.arbitrage.service;

import com.eac.arbitrage.model.Pool;
import com.eac.arbitrage.repository.PoolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PoolService {
    private final PoolRepository poolRepository;
    @Autowired
    public PoolService(PoolRepository poolRepository) {
        this.poolRepository = poolRepository;
    }


    public List<Pool> getAllPools() {
        return poolRepository.findAll();
    }
    public Pool getById(Long id){return poolRepository.getReferenceById(id);}
    public Pool getByName(String name){return poolRepository.getByName(name);};

}
