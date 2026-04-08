package com.example.airport_management;

public class calculateTotal {
    public static double befTaxTot(String type, int quantity) {
        double total = 0;

        if(type.equals("Economy Class")) {
            total = 100 * quantity;
        }
        else if(type.equals("Premium Economy")) {
            total = 110 * quantity;
        }
        else if(type.equals("Business Class")) {
            total = 120 * quantity;
        }
        else if(type.equals("First Class")) {
            total = 140 * quantity;
        }

        return total;

    }

}
