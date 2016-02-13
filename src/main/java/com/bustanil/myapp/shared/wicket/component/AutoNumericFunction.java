package com.bustanil.myapp.shared.wicket.component;

import de.agilecoders.wicket.jquery.function.IFunction;

public class AutoNumericFunction implements IFunction {

    private char thousandSeparator;
    private char decimalSeparator;
    private int scale;


    public AutoNumericFunction(char thousandSeparator, char decimalSeparator, int scale) {
        this.thousandSeparator = thousandSeparator;
        this.decimalSeparator = decimalSeparator;
        this.scale = scale;
    }

    public String build() {
        return "autoNumeric('init', " + "{aSep : '" + thousandSeparator + "', aDec : '" + decimalSeparator + "'})";
    }

}
