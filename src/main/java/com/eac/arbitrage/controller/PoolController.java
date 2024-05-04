package com.eac.arbitrage.controller;

import com.eac.arbitrage.model.Pool;
import com.eac.arbitrage.service.PoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/pools")
public class PoolController {
    // UserService dependency
    private final PoolService userService;

    @Autowired
    public PoolController(PoolService poolService) {
        this.userService = poolService;
    }

    // Endpoint to get all pools
    @GetMapping
    public List<Pool> getAllPools() {
        return userService.getAllPools();
    }
}
