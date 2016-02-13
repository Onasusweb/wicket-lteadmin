package com.bustanil.myapp.infrastructure.reporting.model;

import ar.com.fdvs.dj.core.DynamicJasperHelper;
import ar.com.fdvs.dj.core.layout.ListLayoutManager;
import ar.com.fdvs.dj.domain.DynamicReport;
import ar.com.fdvs.dj.domain.Style;
import ar.com.fdvs.dj.domain.builders.ColumnBuilder;
import ar.com.fdvs.dj.domain.builders.DynamicReportBuilder;
import ar.com.fdvs.dj.domain.constants.Border;
import ar.com.fdvs.dj.domain.constants.Font;
import ar.com.fdvs.dj.domain.constants.HorizontalAlign;
import ar.com.fdvs.dj.domain.constants.VerticalAlign;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang.WordUtils;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.*;

public abstract class BaseReportTemplate implements ReportTemplate {

    protected String reportName;
    protected String templateName;
    private List<ColumnDefinition> columns = new ArrayList<>();

    public BaseReportTemplate() {
        templateDefinition(columns);
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public List<ColumnDefinition> getColumns() {
        return columns;
    }

    public void setColumns(List<ColumnDefinition> columns) {
        this.columns = columns;
    }

    @Override
    public void generateAndExport(Map<String, String> requestParams, OutputStream outputStream, String fileType) {
        String userName = requestParams.get("userName");
        String roleName = requestParams.get("roleName");

        Validate.notEmpty(fileType, "Missing parameter 'fileType'");

        Validate.notEmpty(reportName, "Missing report name. Have you set it in templateDefinition method?");
        Validate.notEmpty(templateName, "Missing template name. Have you set it in templateDefinition method?");

        Map<String, Object> reportParams = prepareReportParams(requestParams);
        Collection<?> data = retrieveData(requestParams);
        reportParams.put("size", String.valueOf(data.size()));
        DynamicReportBuilder builder = createBuilder(fileType);
        DynamicReport dynamicReport = builder.build();

        try {
            JRDataSource jrDataSource = new JRBeanCollectionDataSource(data);
            JasperPrint jasperPrint = DynamicJasperHelper.generateJasperPrint(dynamicReport, new ListLayoutManager(), jrDataSource, reportParams);

            export(jasperPrint, outputStream, fileType);
        } catch (JRException e) {
            throw new RuntimeException(e);
        }
    }

    private DynamicReportBuilder createBuilder(String fileType) {
        Style columnHeaderStyle = createColumnHeaderStyle();

        DynamicReportBuilder builder = new DynamicReportBuilder();
        builder.setTemplateFile("report/template/" + templateName + ".jrxml", false, false, true, false);
        builder.setUseFullPageWidth(true);
        builder.setWhenNoData("No Data Founds!", Style.createBlankStyle("blank"));
        builder.setDefaultStyles(Style.createBlankStyle("blank"), Style.createBlankStyle("blank"), columnHeaderStyle, null);
        if (fileType.equals("XLS")) {
            builder.setIgnorePagination(true);
        }
        prepareColumns(builder);
        return builder;
    }

    private void prepareColumns(DynamicReportBuilder builder) {
        for (ColumnDefinition columnDefinition : columns) {
            ColumnBuilder columnBuilder = ColumnBuilder.getNew();
            columnBuilder.setTitle(columnDefinition.getColumnName());
            if (!StringUtils.isBlank(columnDefinition.getSize())) {
                columnBuilder.setWidth(Integer.valueOf(columnDefinition.getSize()));
            }
            columnBuilder.setColumnProperty(columnDefinition.getField(), columnDefinition.getClazz());
            if (columnDefinition.getClazz() == BigDecimal.class) {
                columnBuilder.setTextFormatter(NumberFormat.getNumberInstance());
            }
            Style cellStyle = createCellStyle(columnDefinition);

            columnBuilder.setStyle(cellStyle);
            builder.addColumn(columnBuilder.build());
        }
    }

    private Style createCellStyle(ColumnDefinition columnDefinition) {
        Style cellStyle = new Style();
        cellStyle.setVerticalAlign(VerticalAlign.MIDDLE);
        cellStyle.setHorizontalAlign(columnDefinition.getAlignment() == ColumnDefinition.Alignment.LEFT ? HorizontalAlign.LEFT : HorizontalAlign.RIGHT);
        cellStyle.setBorder(Border.THIN());
        cellStyle.setPadding(6);
        cellStyle.setFont(new Font(7, "SansSerif", false));
        return cellStyle;
    }

    private Style createColumnHeaderStyle() {
        Style columnHeaderStyle = new Style();
        columnHeaderStyle.setVerticalAlign(VerticalAlign.MIDDLE);
        columnHeaderStyle.setHorizontalAlign(HorizontalAlign.LEFT);
        columnHeaderStyle.setPadding(6);
        columnHeaderStyle.setBorder(Border.THIN());
        columnHeaderStyle.setFont(new Font(8, "SansSerif", true));
        return columnHeaderStyle;
    }

    private Map<String, Object> prepareReportParams(Map<String, String> requestParams) {
        Map<String, Object> reportParams = new HashMap<>();
        reportParams.put("createdBy", requestParams.get("userName") + " (" + requestParams.get("roleName") + ")");
        reportParams.put("reportName", reportName);
        int filterCount = 1;
        for (final Map.Entry<String, String> entryFilter : requestParams.entrySet()) {
            if (entryFilter.getKey().startsWith("filter.")) {
                ColumnDefinition columnDefinition = (ColumnDefinition) CollectionUtils.find(columns, new Predicate() {
                    @Override
                    public boolean evaluate(Object o) {
                        ColumnDefinition columnDefinition = (ColumnDefinition) o;
                        return columnDefinition.getField().equals(entryFilter.getKey());
                    }
                });

                String label;
                if (columnDefinition == null) {
                    label = WordUtils.capitalizeFully(splitCamelCase(entryFilter.getKey().substring(7)));
                } else {
                    label = columnDefinition.getColumnName();
                }

                reportParams.put("FILTER_LABEL_" + filterCount, label);
                reportParams.put("FILTER_VALUE_" + filterCount, String.valueOf(entryFilter.getValue()));
                filterCount++;
            }
        }
        reportParams.put("logo", ReportTemplate.class.getResourceAsStream("/images/companylogo.png"));

        prepareAdditionalParams(requestParams, reportParams);

        return reportParams;
    }

    protected abstract void prepareAdditionalParams(Map<String, String> requestParams, Map<String, Object> reportParams);

    protected abstract Collection<?> retrieveData(Map<String, String> parameters);

    protected abstract void templateDefinition(List<ColumnDefinition> columnDefinitions);

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

    protected void export(JasperPrint jasperPrint, OutputStream outputStream, String fileType) {
        try {
            switch (fileType.toUpperCase()) {
                case "PDF":
                    JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
                    break;
                case "XLS":
                    JRXlsExporter exporter = new JRXlsExporter();
                    exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                    exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));
                    exporter.exportReport();
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported file type: " + fileType);
            }
        } catch (JRException e) {
            throw new RuntimeException("Error occurred while exporting report", e);
        }
    }
}
