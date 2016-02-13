package com.bustanil.myapp.infrastructure.reporting.model;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.Date;

public class Period {

    private Date from;
    private Date to;

    public Period(Date from, Date to) {
        Validate.notNull(from);
        Validate.notNull(to);
        this.from = from;
        this.to = to;
    }

    public Date getFrom() {
        return from;
    }

    public Date getTo() {
        return to;
    }

    @Override
    public String toString() {
        String datePattern = "dd/MM/yyyy";
        return new StringBuilder().append(DateFormatUtils.format(from, datePattern)).append(" - ").append(DateFormatUtils.format(to, datePattern)).toString();
    }
}
