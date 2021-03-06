package com.vaadin.demo.stockdata.ui;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public class MoneyFormatter {

    public static String format(long cents) {
        return format(new BigDecimal(cents).divide(BigDecimal.TEN, BigDecimal.ROUND_HALF_UP));
    }

    public static String format(BigDecimal number) {
        return NumberFormat.getCurrencyInstance(Locale.US).format(number);
    }
}
