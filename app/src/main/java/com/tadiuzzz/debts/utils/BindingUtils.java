package com.tadiuzzz.debts.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Simonov.vv on 23.07.2019.
 */
public class BindingUtils {

    public static String getFormattedDateFromMillis(Long millis) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
        return simpleDateFormat.format(new Date(millis));
    }

    public static Long getDateNow() {
        Date date = new Date();
        return date.getTime();
    }
}
