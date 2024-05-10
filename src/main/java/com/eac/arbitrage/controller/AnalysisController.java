package com.eac.arbitrage.controller;

import com.eac.arbitrage.model.Analysis;
import com.eac.arbitrage.model.Pool;
import com.eac.arbitrage.model.Region;
import com.eac.arbitrage.service.AnalysisService;
import com.eac.arbitrage.service.PoolService;
import com.eac.arbitrage.service.RegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/analysis")
public class AnalysisController {
    private final AnalysisService analysisService;
    private final PoolService poolService;
    private final RegionService regionService;

    @Autowired
    AnalysisController(AnalysisService analysisService,PoolService poolService,RegionService regionService){
        this.analysisService=analysisService;
        this.poolService = poolService;
        this.regionService = regionService;
    }

    @GetMapping
    List<Analysis> getAllAnalyses(){
        return analysisService.getAllAnalyses();
    }

    @PostMapping()
    String addAnalysis(@RequestBody AnalysisDTO analysisSpec){
        String result = "";
        Analysis analysis = new Analysis(analysisSpec);
        analysis.setStatus("CREATED");
        analysisService.addOrUpdate(analysis);
        return result;
    }
    // This method updates an existing analysis if a spec is found in the body spec and runs it
    @PutMapping("/{analysisId}")
    String startAnalysis(@PathVariable Long analysisId, @RequestBody AnalysisDTO analysisDTO){
        Analysis analysis = new Analysis(analysisDTO);
        analysis.setId(analysisId);
        analysisService.addOrUpdate(analysis);
        analysisService.deleteResults(analysis.getId());
        String result="";
        List<Pool> pools = (StringUtils.isEmpty(analysisDTO.getPool())? poolService.getAllPools() : Collections.singletonList(poolService.getByName(analysisDTO.getPool())));
        for(Pool pool : pools){
            List<Region> regions = (StringUtils.isEmpty(analysisDTO.getRegion())? regionService.getByPoolId(pool.getId()): Collections.singletonList(regionService.getByName(analysisDTO.getRegion())));
            for(Region region : regions){
                analysis.setRegion(region.getName());
                analysisService.startAnalysis(new Analysis(analysis));
            }
        }
        return result;
    }
}
