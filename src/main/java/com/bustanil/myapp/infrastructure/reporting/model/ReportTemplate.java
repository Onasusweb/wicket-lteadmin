package com.bustanil.myapp.infrastructure.reporting.model;

import java.io.OutputStream;
import java.util.Map;

public interface ReportTemplate {
    void generateAndExport(Map<String, String> requestParams, OutputStream outputStream, String fileType);
}
