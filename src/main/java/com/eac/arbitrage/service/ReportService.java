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
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Date;
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
            Workbook workbook = new XSSFWorkbook();
            CreationHelper createHelper = workbook.getCreationHelper();
            List<String> regions = resultsRepository.getDistinctRegionByPool(pool);
            for (String region : regions) {

                String clean = region.replace("/", " ")
                        .replace("\\"," ")
                        .replace("/", " ")
                        .replace("*"," ")
                        .replace("?"," ")
                        .replace("["," ");
                Sheet sheet = workbook.createSheet(clean);

                Row header = sheet.createRow(0);
                Cell headerCell = header.createCell(0); headerCell.setCellValue("UTC");
                headerCell = header.createCell(1); headerCell.setCellValue("Energy");
                headerCell = header.createCell(2); headerCell.setCellValue("Revenue");
                headerCell = header.createCell(3);  headerCell.setCellValue("Total Revenue");

                List<Result> results = resultsRepository.getByRegion(region);
                int i = 1;
                for (Result result : results) {
                    Row dataRow = sheet.createRow(i++);
//                    OffsetDateTime odt = result.getUtc().atOffset( ZoneOffset.UTC );

                    Cell dataCell = dataRow.createCell(0); dataCell.setCellValue(Date.from(result.getUtc()));
                    CellStyle cellStyle = workbook.createCellStyle();
                    cellStyle.setDataFormat(                            createHelper.createDataFormat().getFormat("m/d/yy h:mm"));

                    dataCell.setCellStyle(cellStyle);

                    dataCell = dataRow.createCell(1); dataCell.setCellValue(result.getEnergy());
                    dataCell = dataRow.createCell(2); dataCell.setCellValue(result.getRevenue());
                    dataCell = dataRow.createCell(3); dataCell.setCellValue(result.getTotalRevenue());
                }
            }
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
}
