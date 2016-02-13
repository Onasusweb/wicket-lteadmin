package com.bustanil.myapp.shared.wicket.converter;

import org.apache.wicket.util.convert.converter.BigDecimalConverter;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Locale;

public class IDBigDecimalConverter extends BigDecimalConverter {

    private DecimalFormat fmt;

    public IDBigDecimalConverter(){
        Locale id_ID = new Locale("id", "ID");
        fmt = (DecimalFormat) DecimalFormat.getInstance(id_ID);
        fmt.setParseBigDecimal(true);
    }

    @Override
    public BigDecimal convertToObject(String value, Locale locale) {
        try {
            if (value != null) {
                fmt.setGroupingUsed(true);
                return (BigDecimal) fmt.parse(value);
            }
            else {
                return null;
            }
        } catch (ParseException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }

    @Override
    public String convertToString(BigDecimal value, Locale locale) {
        if (value != null) {
            fmt.setGroupingUsed(false);
            return fmt.format(value);
        } else {
            return null;
        }
    }
}
