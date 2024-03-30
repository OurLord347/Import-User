package com.import_user.services;

import jakarta.servlet.http.HttpServletResponse;
import net.datafaker.Faker;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;

import java.io.IOException;



public class ExcelService {
    public static void generateExcelUsers(HttpServletResponse response, int countRow) {
        try {
            Faker faker = new Faker();

            try (SXSSFWorkbook workbook = new SXSSFWorkbook()) {
                Sheet sheet = workbook.createSheet("Sheet1");
                XSSFCellStyle dateStyle = (XSSFCellStyle) workbook.createCellStyle();
                dateStyle.setDataFormat(workbook.createDataFormat().getFormat("dd.MM.yyyy"));
                Row firstRow = sheet.createRow(0);
                //id, name, last name, birthday, company, position at work, salary
                firstRow.createCell(0).setCellValue("id");
                firstRow.createCell(1).setCellValue("name");
                firstRow.createCell(2).setCellValue("last name");
                firstRow.createCell(3).setCellValue("birthday");
                firstRow.createCell(4).setCellValue("company");
                firstRow.createCell(5).setCellValue("position at work");
                firstRow.createCell(6).setCellValue("salary");

                for (int rowNumber = 1; rowNumber < countRow+1; rowNumber++) {
                    Row row = sheet.createRow(rowNumber);
                    row.createCell(0).setCellValue(rowNumber);
                    row.createCell(1).setCellValue(faker.name().name());
                    row.createCell(2).setCellValue(faker.name().lastName());
                    Cell cell3 = row.createCell(3);
                    cell3.setCellValue(faker.date().birthday(14,100));
                    cell3.setCellStyle(dateStyle);
                    row.createCell(4).setCellValue(faker.company().name());
                    row.createCell(5).setCellValue(faker.company().profession());
                    row.createCell(6).setCellValue(faker.number().numberBetween(1000,800000));

                }

                workbook.write(response.getOutputStream());
            }
        } catch (IOException ignored) {
        }
    }


}
