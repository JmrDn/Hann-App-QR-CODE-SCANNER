package com.example.hannappqrcodescanner;

import android.annotation.SuppressLint;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
@SuppressLint("SimpleDateFormat")
public class DateAndTimeUtils {

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String calculateMinutesAgo(LocalDateTime yourDate) {
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(yourDate, now);

        long minutes = duration.toMinutes();
        long hours = minutes / 60;
        long day = hours / 24;
        long week = day / 7;


        if (minutes == 0) {
            return "just now";
        } else if (minutes == 1) {
            return "1 minute ago";
        } else if (minutes < 60) {
            return minutes + " minutes ago";
        } else if (minutes < 120) {
            return "1 hour ago";
        } else if (hours > 1 && hours < 25) {
            return hours + " hours ago";
        }
        else if (day == 1){
            return "1 day ago";
        }
        else if (day > 1 && day <= 7){
            return day + " days ago";
        }
        else if (week == 1){
            return "1 week ago";
        }
        else{
            return  week + "weeks ago";
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static LocalDateTime parseDateString(String dateString, String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return LocalDateTime.parse(dateString, formatter);
    }
    public static String getDate(){
        return new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(new Date());
    }

    public static String convertTimeStampFormatToDateAndTime(String dateAndTime){

        String formattedTime = "";
        // Create a SimpleDateFormat for parsing 12-hour time
        SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy H:m:s");

        try {
            // Parse the 12-hour time string to Date
            Date date = inputFormat.parse(dateAndTime);

            // Create a SimpleDateFormat for formatting to 24-hour time
            SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy/MM/dd h:mm a");

            // Format the Date to 24-hour format
            formattedTime = outputFormat.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formattedTime;
    }
}
