package com.example.airport_management;

public class airlineLogo {
    public static String get_logo(int AirlineID) {
        String logo = "";

        switch(AirlineID) {
            case 1:
                logo = "static/airasia.png";
                break;

            case 2:
                logo = "static/malaysia_airlines.png";
                break;
        }

        return logo;
    }
}
// 1--> airasia
// 2--> malaysian airlines