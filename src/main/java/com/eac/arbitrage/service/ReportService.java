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
            CreationHelper createHelper = workbook.getCreationHelper();
            CellStyle timestampStyle = workbook.createCellStyle();
            timestampStyle.setDataFormat( createHelper.createDataFormat().getFormat("m/d/yy h:mm"));

            Sheet energySheet = workbook.createSheet("Energy");
            setupSheet(energySheet, regions);

            Sheet revSheet = workbook.createSheet("Revenue");
            setupSheet(revSheet, regions);

            Sheet totalSheet = workbook.createSheet("Total Revenue");
            setupSheet(totalSheet, regions);

            List<Result> results = resultsRepository.getByPool(pool);
            for(Result result : results){
                addResult(result, energySheet, timestampStyle);
                addResult(result, revSheet, timestampStyle);
                addResult(result, totalSheet, timestampStyle);
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
    private void addResult(Result result, Sheet sheet, CellStyle timestampStyle){

        Row headerRow = sheet.getRow(0);
        Row dataRow = sheet.createRow(sheet.getLastRowNum() + 1);

        for(Cell headerCell : headerRow){
            int columnIndex = headerCell.getColumnIndex();

            if(columnIndex == 0){// UTC time column
                Cell cell = dataRow.createCell(0);
                cell.setCellValue(LocalDateTime.ofInstant(result.getUtc(), ZoneOffset.UTC));
                cell.setCellStyle(timestampStyle);
            }if(headerCell.getStringCellValue().equals(result.getRegion())) {
                Cell dCell = dataRow.createCell(columnIndex);

                if (sheet.getSheetName().equals("Energy"))
                    dCell.setCellValue(result.getEnergy());
                else if (sheet.getSheetName().equals("Revenue") )
                    dCell.setCellValue(result.getRevenue());
                else if (sheet.getSheetName().equals( "Total Revenue"))
                    dCell.setCellValue(result.getTotalRevenue());

            }
        }

    }

}
