package com.example.android.inventorystage2;

/**
 * Created by Emoke Hajdu on 8/14/2018.
 */

public class Utility {

    public static boolean isInteger(String text) {
        try {
            int i = Integer.parseInt(text);
        }
        catch(NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public static boolean isDouble(String text) {
        try {
            double d = Double.parseDouble(text);
        }
        catch(NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}
