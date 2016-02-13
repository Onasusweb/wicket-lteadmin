package com.bustanil.myapp.infrastructure.reporting.service.impl;


import com.bustanil.myapp.infrastructure.reporting.model.ReportTemplate;
import com.bustanil.myapp.infrastructure.reporting.service.ReportService;
import org.apache.commons.lang.Validate;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import java.util.Map;

@Service
public class ReportServiceImpl implements ReportService, ApplicationContextAware {
    private ApplicationContext applicationContext;

    static String splitCamelCase(String s) {
        return s.replaceAll(
                String.format("%s|%s|%s",
                        "(?<=[A-Z])(?=[A-Z][a-z])",
                        "(?<=[^A-Z])(?=[A-Z])",
                        "(?<=[A-Za-z])(?=[^A-Za-z])"
                ),
                " "
        );
    }

    @Override
    public void generateReport(Map<String, String> parameters, ServletOutputStream outputStream, String fileType) {
        String reportName = parameters.get("reportName");
        Validate.notEmpty(reportName, "Missing parameter 'reportName'");

        ReportTemplate template = loadTemplate(reportName);

        template.generateAndExport(parameters, outputStream, fileType);

    }

    private ReportTemplate loadTemplate(String reportName) {
        return (ReportTemplate) applicationContext.getBean(reportName);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
