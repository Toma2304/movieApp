package org.example.movieapp.model;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ExportData {
    @NotEmpty(message = "Filename input is compulsory")
    private String filename;

    @NotEmpty(message = "Choose fields to export")
    private List<Field> fields;

    @NotEmpty(message = "Choose which data to export")
    private List<Map<String, Object>> data;
}