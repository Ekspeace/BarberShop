package com.ekspeace.barbershop.Constants;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Utils {
    public static final int TIME_SLOT_TOTAL = 9;
    public static final String service = "SERVICE";
    public static final String timeDate ="TIME_DATE";
    public static final String barber ="BARBER";
    public static final String connection = "CONNECTION";

    public static String convertTimeToString(int slot) {
        switch (slot) {
            case 0:
                return "08:00 AM";
            case 1:
                return "09:00 AM";
            case 2:
                return "10:00 AM";
            case 3:
                return "11:00 AM";
            case 4:
                return "12:00 PM";
            case 5:
                return "13:00 PM";
            case 6:
                return "14:00 PM";
            case 7:
                return "15:00 PM";
            case 8:
                return "16:00 PM";
            default:
                return "Closed";

        }
    }
    public static Boolean isOnline(Context context)	{
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && ni.isConnected();
    }
}
