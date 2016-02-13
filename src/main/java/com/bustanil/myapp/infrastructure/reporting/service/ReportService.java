package com.bustanil.myapp.infrastructure.reporting.service;


import javax.servlet.ServletOutputStream;
import java.util.Map;

public interface ReportService {

    void generateReport(Map<String, String> parameters, ServletOutputStream outputStream, String fileType);
}
