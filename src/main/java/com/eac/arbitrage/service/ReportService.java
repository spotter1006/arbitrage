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
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Service
public class ReportService {
    AnalysisRepository analysisRepository;
    ResultsRepository resultsRepository;

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

            Workbook workbook = new XSSFWorkbook();

            Sheet energySheet = workbook.createSheet("Energy");
            setupSheet(energySheet, regions);

            Sheet revSheet = workbook.createSheet("Revenue");
            setupSheet(revSheet, regions);

            Sheet totalSheet = workbook.createSheet("Total revenue");
            setupSheet(totalSheet, regions);

            List<Result> results = resultsRepository.getByPool(pool);
            for(Result result : results){
                addResult(result, workbook,regions);
            }

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
    private void setupSheet(Sheet sheet, List<String> regions){
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("UTC");
        int i = 1;
        for(String region : regions){
            header.createCell(i++).setCellValue(region);
        }
    }
    // Sheet order: 0 = energy, 1 = revenue, 2 = total revenue
    private void addResult(Result result, Workbook workbook,  List<String> regions){
        CreationHelper createHelper = workbook.getCreationHelper();
        for(int sheetNum = 0; sheetNum < workbook.getNumberOfSheets(); sheetNum++){
            Sheet sheet = workbook.getSheetAt(sheetNum);
            Row dataRow = sheet.createRow(sheet.getLastRowNum() + 1);

            // UTC time column
            Cell cell = dataRow.createCell(0);
            cell.setCellValue(LocalDateTime.ofInstant(result.getUtc(), ZoneOffset.UTC));
            CellStyle cellStyle = workbook.createCellStyle();
            cellStyle.setDataFormat( createHelper.createDataFormat().getFormat("m/d/yy h:mm"));
            cell.setCellStyle(cellStyle);

            // Data columns for each region across energy, revenue and total revenoue sheets
            for(int col = 0; col < regions.size(); col ++){
                cell = dataRow.createCell(col + 1);
                switch(sheetNum){
                    case 0:
                        cell.setCellValue(result.getEnergy()); break;
                    case 1:
                        cell.setCellValue(result.getRevenue()); break;
                    case 2:
                        cell.setCellValue(result.getTotalRevenue()); break;
                }
            }
        }
    }
}
