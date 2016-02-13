package com.bustanil.myapp.shared.wicket.converter;

import org.apache.wicket.util.convert.converter.DateConverter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class MetricDateConverter extends DateConverter {

    @Override
    public DateFormat getDateFormat(Locale locale) {
        if (locale == null)
        {
            locale = Locale.getDefault();
        }

        return new SimpleDateFormat("dd/MM/yyyy", locale);
    }
}
