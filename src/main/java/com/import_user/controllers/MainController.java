package com.import_user.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;


@Controller
public class MainController {

    @GetMapping("/")
    @ResponseBody
    public String hello() {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("MySheet");
            Row row = sheet.createRow(0);
            Cell cell = row.createCell(0);
            cell.setCellValue("Hello, World!");

            try (FileOutputStream fileOut = new FileOutputStream("sample.xlsx")) {
                workbook.write(fileOut);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Hello, World!";
    }


}
