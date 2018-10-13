package com.annasedykh.loftcoin.utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class CurrencyFormatter {
    private static final String CRYPTO_PATTERN = "0.0000####";
    private static final String CONVERT_PATTERN = "0.########";
    private static final String FIAT_PATTERN = "###,##0.00##";

    private DecimalFormat decimalFormat;

    public CurrencyFormatter() {
        DecimalFormatSymbols unusualSymbols = new DecimalFormatSymbols();
        unusualSymbols.setDecimalSeparator('.');
        unusualSymbols.setGroupingSeparator(' ');

        decimalFormat = new DecimalFormat();
        decimalFormat.setDecimalFormatSymbols(unusualSymbols);
    }


    public String format(double value, boolean crypto) {
        if (crypto) {
            decimalFormat.applyPattern(CRYPTO_PATTERN);
        } else {
            decimalFormat.applyPattern(FIAT_PATTERN);
        }
        return decimalFormat.format(value);
    }

    public String formatForConverter(double value) {
        decimalFormat.applyPattern(CONVERT_PATTERN);
        return decimalFormat.format(value);
    }
}
