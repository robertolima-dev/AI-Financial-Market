package com.mali.crypfy.indexmanager.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;

public class StringUtils {

    public static String toMoneyFormat(BigDecimal value) {
        if (value == null)
            return "0,00";
        NumberFormat df = NumberFormat.getCurrencyInstance();
        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        dfs.setCurrencySymbol("");
        dfs.setGroupingSeparator('.');
        dfs.setMonetaryDecimalSeparator(',');
        dfs.setMinusSign('-');
        ((DecimalFormat) df).setDecimalFormatSymbols(dfs);
        ((DecimalFormat) df).setNegativePrefix("-");
        ((DecimalFormat) df).setNegativeSuffix("");
        return df.format((BigDecimal) value);
    }
}
