package com.amitrei.couponsystemv2.utils;

import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;


@Component
public class DateUtil {


    public java.sql.Date expiredDateFromToday (int daysFromCurrentDate) {
        Date date = new java.util.Date();
        return new java.sql.Date(date.getTime() + (1000 * 60 * 60 * (daysFromCurrentDate * 24)));
    }


    public java.sql.Date setDate(int day, int month, int year) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month-1, day);
        java.util.Date date = cal.getTime();
        return new java.sql.Date(date.getTime());
    }


    public  java.sql.Date currentDate() {
        Date date = new java.util.Date();
        return new java.sql.Date(date.getTime());
    }

    public Boolean isDatePassed(java.sql.Date expiredDate) {
        return currentDate().after(expiredDate);
    }
}
