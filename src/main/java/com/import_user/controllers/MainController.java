package com.import_user.controllers;


import com.import_user.services.ExcelService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;


@Controller
public class MainController {

    @Autowired
    ExcelService excelService;

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
        try {
            long usedMB = (Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory())/(1024*1024);
            System.err.println("Используется памяти МБ: " + usedMB);
            excelService.saveUsers(file);

            usedMB = (Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory())/(1024*1024);
            System.err.println("Используется памяти МБ: " + usedMB);
        } catch (Exception e) {
            System.err.println("Ошибка при чтении Excel файла: " + e.getMessage());
        }
        return "home";
    }
}
