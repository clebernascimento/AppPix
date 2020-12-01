package com.itbam.pixceler.util;

import java.text.NumberFormat;
import java.util.Locale;

public class Formatters {
    private static NumberFormat nf;
    private static String symbol;

    public static void prepare() {
        nf = NumberFormat.getCurrencyInstance(Locale.getDefault());
        nf.setMinimumFractionDigits(2);
        nf.setMaximumFractionDigits(2);
        String cur = nf.format(0);

        System.out.println(cur.split("0")[0]);
    }

    public static NumberFormat numFormat() {
        if (nf == null) {
            nf = NumberFormat.getInstance(Locale.getDefault());
            nf.setMinimumFractionDigits(2);
            nf.setMaximumFractionDigits(2);
        }
        return nf;
    }
}
