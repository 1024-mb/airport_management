package com.example.airport_management.utilities;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;


public class flightStatus {
    public static String getStatus(String time) {
        LocalTime now = LocalTime.now(Clock.system(ZoneId.of("Asia/Singapore")));
        String out = "";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalTime time_formatted = LocalTime.parse(time, formatter);


        if(time_formatted.isBefore(now)) {out = "Departed";}
        else if(Math.abs(Duration.between(time_formatted, now).toMinutes()) < 20) {out = "Final Call";}
        else if(Math.abs(Duration.between(time_formatted, now).toMinutes()) <= 90) {out = "Boarding";}
        else if(Math.abs(Duration.between(time_formatted, now).toMinutes()) <= 120) {out = "Gates Open";}

        return out;
    }
}
