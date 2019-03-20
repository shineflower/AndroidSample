package com.jackie.sample.utils;

import android.content.Context;
import android.content.res.Resources;

import com.jackie.sample.R;

import java.util.Calendar;
import java.util.Date;
 
public class FuzzyDateTimeFormatter {
    private final static int SECONDS = 1;
    private final static int MINUTES = 60 * SECONDS;
    private final static int HOURS   = 60 * MINUTES;
    private final static int DAYS    = 24 * HOURS;
    private final static int WEEKS   =  7 * DAYS;
    private final static int MONTHS  =  4 * WEEKS;
    private final static int YEARS   = 12 * MONTHS;
 
    /**
     * Returns a properly formatted fuzzy string representing time ago
     * @param context   Context
     * @param date      Absolute date of the event
     * @return          Formatted string
     */
    public static String getTimeAgo(Context context, Date date) {
        int beforeSeconds = (int) (date.getTime() / 1000);
        int nowSeconds = (int) (Calendar.getInstance().getTimeInMillis() / 1000);
        int diffSeconds = nowSeconds - beforeSeconds;
 
        Resources resources = context.getResources();
 
        if (diffSeconds < MINUTES) {
            return resources.getQuantityString(R.plurals.fuzzydatetime__seconds_ago,
                    diffSeconds, diffSeconds);
        } else if (diffSeconds < HOURS) {
            return resources.getQuantityString(R.plurals.fuzzydatetime__minutes_ago,
                    diffSeconds / MINUTES, diffSeconds / MINUTES);
        } else if (diffSeconds < DAYS) {
            return resources.getQuantityString(R.plurals.fuzzydatetime__hours_ago,
                    diffSeconds / HOURS, diffSeconds / HOURS);
        } else if (diffSeconds < WEEKS) {
            return resources.getQuantityString(R.plurals.fuzzydatetime__days_ago,
                    diffSeconds / DAYS, diffSeconds / DAYS);
        } else if (diffSeconds < MONTHS) {
            return resources.getQuantityString(R.plurals.fuzzydatetime__weeks_ago,
                    diffSeconds / WEEKS, diffSeconds / WEEKS);
        } else if (diffSeconds < YEARS) {
            return resources.getQuantityString(R.plurals.fuzzydatetime__months_ago,
                    diffSeconds / MONTHS, diffSeconds / MONTHS);
        } else {
            return resources.getQuantityString(R.plurals.fuzzydatetime__years_ago,
                    diffSeconds / YEARS, diffSeconds / YEARS);
        }
    }
}