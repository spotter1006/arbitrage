package com.eac.arbitrage.controller;

import com.eac.arbitrage.model.Region;
import com.eac.arbitrage.service.RegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/regions")
public class RegionController {
    private final RegionService regionService;
    @Autowired
    RegionController(RegionService regionService){
        this.regionService = regionService;
    }
    @GetMapping
    List<Region> getAllRegions(){
        return regionService.getAllregions();
    }
}
