package com.eac.arbitrage.service;

import com.eac.arbitrage.model.Analysis;
import com.eac.arbitrage.model.Result;
import com.eac.arbitrage.repository.AnalysisRepository;
import com.eac.arbitrage.repository.ResultsRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static java.time.ZoneOffset.UTC;

@Service
public class ReportService {
    AnalysisRepository analysisRepository;
    ResultsRepository resultsRepository;
    Map<String, Integer> columnNameMap = new HashMap<String, Integer>();

    @Autowired
    public ReportService(AnalysisRepository analysisRepository,ResultsRepository resultsRepository){
        this.analysisRepository = analysisRepository;
        this.resultsRepository = resultsRepository;
    }

    public void createReport(Long analysisId, String directory) {
        Analysis analysis = analysisRepository.getReferenceById(analysisId);
        List<String> pools = resultsRepository.getDistinctPools();
        for (String pool : pools) {
            List<String> regions = resultsRepository.getDistinctRegionByPool(pool);
            List<Result> results = resultsRepository.getByPool(pool);

            Workbook workbook = new XSSFWorkbook();
            CellStyle timestampStyle = workbook.createCellStyle();
            timestampStyle.setDataFormat(workbook.getCreationHelper().createDataFormat().getFormat("m/d/yy h:mm"));

            Sheet energySheet = workbook.createSheet("Energy");
            Sheet revSheet = workbook.createSheet("Revenue");
            Sheet totalSheet = workbook.createSheet("Total Revenue");

            setupSheet(energySheet, regions, timestampStyle);
            setupSheet(revSheet, regions,  timestampStyle);
            setupSheet(totalSheet, regions, timestampStyle);

            processResults(results, workbook);

            // Save to filesystem
            try{
                String filename = directory + "/" + analysis.getName() + "-" + pool + ".xlsx";
                FileOutputStream outputStream = new FileOutputStream(filename);
                workbook.write(outputStream);
                workbook.close();
            }catch(IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void setupSheet(Sheet sheet, List<String> regions,  CellStyle timestampStyle){
        Row headerRow = sheet.createRow(0);
        Cell utcCell = headerRow.createCell(0);
        utcCell.setCellValue("UTC");
        columnNameMap.put("UTC", 0);
        int i = 1;
        for(String region : regions){
            Cell headerCell = headerRow.createCell(i);
            headerCell.setCellValue(region);
            columnNameMap.put(region, i);
            i++;
        }

    }

    private void processResults(List<Result> results, Workbook workbook){
        Sheet energySheet = workbook.getSheet("Energy");
        Sheet revSheet = workbook.getSheet("Revenue");
        Sheet totalSheet = workbook.getSheet("Total Revenue");
        Instant currentInterval = results.get(0).getUtc();

        Row energyRow = energySheet.createRow(energySheet.getPhysicalNumberOfRows());
        Row revRow = revSheet.createRow(revSheet.getPhysicalNumberOfRows());
        Row totalRow = totalSheet.createRow(totalSheet.getPhysicalNumberOfRows());

        CellStyle dateStyle = workbook.createCellStyle();
        dateStyle.setDataFormat((short) 22);

        CellStyle moneyStyle = workbook.createCellStyle();
        moneyStyle.setDataFormat((short) 39);

        for(Result result : results){
            if(!result.getUtc().equals(currentInterval)){
                energyRow = energySheet.createRow(energySheet.getPhysicalNumberOfRows());
                revRow = revSheet.createRow(revSheet.getPhysicalNumberOfRows());
                totalRow = totalSheet.createRow(totalSheet.getPhysicalNumberOfRows());
                currentInterval = result.getUtc();
            }
            LocalDateTime ldt = LocalDateTime.ofInstant(result.getUtc(),UTC);
            Cell eneryUtcCell =  energyRow.createCell(0);
            eneryUtcCell.setCellValue(ldt);
            eneryUtcCell.setCellStyle(dateStyle);

            Cell revUtcCell =  revRow.createCell(0);
            revUtcCell.setCellValue(ldt);
            revUtcCell.setCellStyle(dateStyle);

            Cell totalUtcCell = totalRow.createCell(0);
            totalUtcCell.setCellValue(ldt);
            totalUtcCell.setCellStyle(dateStyle);

            energySheet.setColumnWidth(0,5000);
            revSheet.setColumnWidth(0,5000);
            totalSheet.setColumnWidth(0,5000);

            int column = columnNameMap.get(result.getRegion());

            Cell energyCell = energyRow.createCell(column);
            energyCell.setCellValue(result.getEnergy());


            Cell revCell = revRow.createCell(column);
            revCell.setCellValue(result.getRevenue());
            revCell.setCellStyle(moneyStyle);

            Cell totalCell = totalRow.createCell(column);
            totalCell.setCellValue(result.getTotalRevenue());
            totalCell.setCellStyle(moneyStyle);

        }
    }
}