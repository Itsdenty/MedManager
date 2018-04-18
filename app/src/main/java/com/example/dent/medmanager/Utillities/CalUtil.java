package com.example.dent.medmanager.Utillities;

/**
 * Created by dent4 on 4/11/2018.
 */
/* Custome helper methods for calender conversion
      they were used in screens that converted the calender object
      to millisecond long for storing to database and vice versa
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
    public static int getMonthNumber(String month){
        int monthNumber = 0;
        switch(month){
            case "January":
                monthNumber = 0;
                break;
            case "February":
                monthNumber = 1;
                break;
            case "March":
                monthNumber = 2;
                break;
            case "April":
                monthNumber = 3;
                break;
            case "May":
                monthNumber = 4;
                break;
            case "June":
                monthNumber = 5;
                break;
            case "July":
                monthNumber = 6;
                break;
            case "August":
                monthNumber = 7;
                break;
            case "Semptember":
                monthNumber = 8;
                break;
            case "October":
                monthNumber = 9;
                break;
            case "November":
                monthNumber = 10;
                break;
            case "December":
                monthNumber = 11;
                break;
        }
        return monthNumber;
    }
    public static int getNoDays(int month){
        int days = 0;
        switch(month){
            case 0:
                days = 31;
                break;
            case 1:
                days = 28;
                break;
            case 2:
                days = 31;
                break;
            case 3:
                days = 30;
                break;
            case 4:
                days = 31;
                break;
            case 5:
                days = 30;
                break;
            case 6:
                days = 31;
                break;
            case 7:
                days = 31;
                break;
            case 8:
                days = 30;
                break;
            case 9:
                days = 31;
                break;
            case 10:
                days = 30;
                break;
            case 11:
                days = 31;
                break;
        }
        return days;
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

