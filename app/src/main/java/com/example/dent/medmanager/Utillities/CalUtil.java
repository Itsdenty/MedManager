package com.example.dent.medmanager.Utillities;

/**
 * Created by dent4 on 4/11/2018.
 */

public class CalUtil {
    public static String getMonth(int month){
        String monthString = "";
        switch(month){
            case 0:
                monthString ="January";
                break;
            case 1:
                monthString = "February";
                break;
            case 2:
                monthString = "March";
                break;
            case 3:
                monthString = "April";
                break;
            case 4:
                monthString = "May";
                break;
            case 5:
                monthString = "June";
                break;
            case 6:
                monthString = "July";
                break;
            case 7:
                monthString = "August";
                break;
            case 8:
                monthString = "September";
                break;
            case 9:
                monthString = "October";
                break;
            case 10:
                monthString = "November";
                break;
            case 11:
                monthString = "November";
                break;
        }
        return monthString;
    }

    public static String convertDay(int day){
        String stringDay = "";
        switch(day){
            case 0:
                stringDay = String.valueOf(day) + "st";
                break;
            case 1:
                stringDay = String.valueOf(day) + "nd";
                break;
            case 2:
                stringDay = String.valueOf(day) + "rd";
                break;
            case 20:
                stringDay = String.valueOf(day) + "st";
                break;
            case 21:
                stringDay = String.valueOf(day) + "nd";
                break;
            case 22:
                stringDay = String.valueOf(day) + "rd";
                break;
            case 30:
                stringDay = String.valueOf(day) + "rd";
                break;
            default:
                stringDay = String.valueOf(day) + "th";
                break;
        }
        return stringDay;
    }
}

