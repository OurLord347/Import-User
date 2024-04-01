package com.import_user.services;

import com.import_user.entity.Company;
import com.import_user.entity.Position;
import com.import_user.entity.User;
import com.import_user.repository.CompanyRepository;
import com.import_user.repository.PositionRepository;
import com.import_user.repository.UserRepository;
import com.monitorjbl.xlsx.StreamingReader;
import jakarta.servlet.http.HttpServletResponse;
import net.datafaker.Faker;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.apache.poi.ss.usermodel.Workbook;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Service
public class ExcelService {
    @Autowired
    private UserService userRepository;
    @Autowired
    private CompanyService companyRepository;
    @Autowired
    private PositionService positionRepository;

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

    public void saveUsers(MultipartFile file){
        Workbook workbook = null;

        try {
            workbook = StreamingReader.builder()
                    .rowCacheSize(100)    // number of rows to keep in memory (defaults to 10)
                    .bufferSize(4096)     // buffer size to use when reading InputStream to file (defaults to 1024)
                    .open(file.getInputStream());            // InputStream or File for XLSX file (required)
            ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
            long maxMemory = 500 * 1024 * 1024; // Ограничение на память в 500 МБ

            for (Sheet sheet : workbook) {

                for (Row r : sheet) {

                    //Ограничение по памяти на 500 мб
                    while (Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory() > maxMemory) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }

                    if (r.getRowNum() == 0) {
                        continue;
                    }
                    executor.execute(()->{
                        Company company = companyRepository.getByName(r.getCell(4).getStringCellValue());
                        Position position = positionRepository.getByName(r.getCell(5).getStringCellValue());
                        User user = userRepository.getById((long) r.getCell(0).getNumericCellValue());
                        user.setId((long) r.getCell(0).getNumericCellValue());
                        user.setName(r.getCell(1).getStringCellValue());
                        user.setLastName(r.getCell(2).getStringCellValue());
                        user.setBirthday(r.getCell(3).getDateCellValue());
                        user.setCompany(company);
                        user.setPosition(position);
                        user.setSalary((int) r.getCell(0).getNumericCellValue());
                        userRepository.getSave(user);
                    });
                }
            }
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
    }


}
