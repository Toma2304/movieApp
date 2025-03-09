package org.example.movieapp.service;

import org.example.movieapp.model.ExportData;
import org.example.movieapp.model.Field;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ExcelService {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    public byte[] generateExcel(ExportData request) {
        List<Field> fields = request.getFields();
        List<Map<String, Object>> data = request.getData();

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Export");

            CellStyle dateStyle = workbook.createCellStyle();
            CreationHelper createHelper = workbook.getCreationHelper();
            dateStyle.setDataFormat(createHelper.createDataFormat().getFormat("yyyy-mm-dd"));


            Row header = sheet.createRow(0);
            for (int colIndex = 0; colIndex < fields.size(); colIndex++) {
                String headerName = fields.get(colIndex).getName();
                header.createCell(colIndex).setCellValue(headerName);
            }

            int rowNum = 1;
            for (Map<String, Object> rowMap : data) {
                Row row = sheet.createRow(rowNum++);

                for (int colIndex = 0; colIndex < fields.size(); colIndex++) {
                    Field definition = fields.get(colIndex);
                    String fieldName = definition.getName();
                    String fieldType = definition.getType();

                    Object value = rowMap.get(fieldName);
                    Cell cell = row.createCell(colIndex);

                    if (value == null) {
                        continue;
                    }

                    switch (fieldType.toLowerCase()) {
                        case "number":
                            try {
                                double d = Double.parseDouble(value.toString());
                                cell.setCellValue(d);
                            } catch (NumberFormatException e) {
                                cell.setCellValue(value.toString());
                            }
                            break;

                        case "date":
                            // "2024-12-18"
                            try {
                                Date parsedDate = DATE_FORMAT.parse(value.toString());
                                cell.setCellValue(parsedDate);
                                cell.setCellStyle(dateStyle);
                            } catch (ParseException e) {
                                cell.setCellValue(value.toString());
                            }
                            break;

                        default:
                            cell.setCellValue(value.toString());
                            break;
                    }
                }
            }

            for (int i = 0; i < fields.size(); i++) {
                sheet.autoSizeColumn(i);
            }

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            workbook.write(bos);
            return bos.toByteArray();

        } catch (IOException e) {
            e.printStackTrace();
            return new byte[0];
        }
    }
}
