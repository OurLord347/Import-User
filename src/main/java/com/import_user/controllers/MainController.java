package com.import_user.controllers;


import com.import_user.services.ExcelService;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

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
    public String uploadExcel(@RequestParam("file") MultipartFile file) throws IOException {
        try (XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);

            StringBuilder contentBuilder = new StringBuilder();

            for (Row row : sheet) {
                for (Cell cell : row) {
                    contentBuilder.append(cell.toString()).append(" ");
                }
                contentBuilder.append("\n");
            }

            String content = contentBuilder.toString();
            return content;
        }
    }
}
