package com.bustanil.myapp.infrastructure.reporting.model;

public class ColumnDefinition {
    private Class clazz;
    private String field;
    private String columnName;
    private String size;
    private Alignment alignment = Alignment.LEFT;

    public enum Alignment {
        LEFT, RIGHT
    }

    public ColumnDefinition(String field, String columnName, String size, String alignment, Class clazz) {
        this(field, columnName, size);
        this.alignment = Alignment.valueOf(alignment.toUpperCase());
        this.clazz = clazz;
    }

    public ColumnDefinition(String field, String columnName, String size) {
        this(field, columnName);
        this.size = size;
    }

    public ColumnDefinition(String field, String columnName) {
        this.field = field;
        this.columnName = columnName;
        this.clazz = Object.class;
    }

    public String getField() {
        return field;
    }

    public String getColumnName() {
        return columnName;
    }

    public String getSize() {
        return size;
    }

    public Alignment getAlignment() {
        return alignment;
    }

    public Class getClazz() {
        return clazz;
    }
}
