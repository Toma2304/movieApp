package org.example.movieapp.controller;

import jakarta.validation.Valid;
import org.example.movieapp.model.ExportData;
import org.example.movieapp.service.ExcelService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/excel")

public class ExcelController {

    private final ExcelService excelService;

    public ExcelController(ExcelService excelService) {
        this.excelService = excelService;
    }

    @PostMapping("/download")
    public ResponseEntity<byte[]> exportExcel(@Valid @RequestBody ExportData request) {

        String filename = request.getFilename();

        if (!filename.toLowerCase().endsWith(".xlsx")) {
            filename += ".xlsx";
        }

        byte[] excelData = excelService.generateExcel(request);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(excelData);
    }
}
