package com.import_user.controllers;


import com.import_user.services.ExcelService;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import com.monitorjbl.xlsx.StreamingReader;

@Controller
public class MainController {

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("title", "Главная страница");
        return "home";
    }

    @PostMapping("/download-excel")
    public void downloadExcel(HttpServletResponse response, @RequestParam("number") int number) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=large-example.xlsx");
        ExcelService.generateExcelUsers(response, number);
    }

    @PostMapping("/upload-excel")
    public String readExcelFileInChunks(@RequestParam("file") MultipartFile file) {
        Workbook workbook = null;
        try {
            long usedMB = (Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory())/(1024*1024);
            System.err.println("Использовано памяти МБ: " + usedMB);

            workbook = StreamingReader.builder()
                    .rowCacheSize(100)    // number of rows to keep in memory (defaults to 10)
                    .bufferSize(4096)     // buffer size to use when reading InputStream to file (defaults to 1024)
                    .open(file.getInputStream());            // InputStream or File for XLSX file (required)


            usedMB = (Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory())/(1024*1024);
            System.err.println("Использовано памяти МБ: " + usedMB);

            for (Sheet sheet : workbook){
                System.out.println(sheet.getSheetName());
                for (Row r : sheet) {
                    if(r.getRowNum() == 0){
                        continue;
                    }
                    for (Cell c : r) {
                        System.out.println(c.getStringCellValue());
                    }
                }
            }

            usedMB = (Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory())/(1024*1024);
            System.err.println("Использовано памяти МБ: " + usedMB);
        } catch (Exception e) {
            System.err.println("Ошибка при чтении Excel файла: " + e.getMessage());
        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (IOException e) {
                    System.err.println("Ошибка при закрытии книги Excel: " + e.getMessage());
                }
            }
        }
        return "home";
    }
}
