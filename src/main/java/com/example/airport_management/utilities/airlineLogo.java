package com.example.airport_management.utilities;

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

            case 79:  //BRITISH
                logo = "static/british_airways.png";
                break;

            case 80: //KOREAN
                logo = "static/korean_airlines.png";
                break;

            case 81: //DELTA
                logo = "static/delta_airlines.png";
                break;

            case 82:
                logo = "static/china_southern.png";
                break;
        }

        return logo;
    }
}
// 1--> airasia
// 2--> malaysian airlines