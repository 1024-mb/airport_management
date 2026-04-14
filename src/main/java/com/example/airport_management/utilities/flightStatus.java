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

        long minutes = Duration.between(now, time_formatted).toMinutes();

        if (minutes < 0) {
            out = "Departed";
        } else if (minutes <= 20) {
            out = "Final Call";
        } else if (minutes <= 90) {
            out = "Boarding";
        } else if (minutes <= 120) {
            out = "Gates Open";
        }
        return out;
    }
}
