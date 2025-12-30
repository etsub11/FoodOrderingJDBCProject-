package com.example.foodorderingjdbcproject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EthiopianDateConverter {

    // Updated to use English month names as requested
    private static final String[] ETHIOPIAN_MONTHS = {
        "September", "October", "November", "December", "January", "February",
        "March", "April", "May", "June", "July", "August", "Pagumē"
    };

    public static String formatToEthiopian(LocalDateTime gregorianDateTime) {
        // Note: This is a simplified approximation and does not account for leap years
        // or the exact start of the Ethiopian new year.
        int gregorianYear = gregorianDateTime.getYear();
        int gregorianMonth = gregorianDateTime.getMonthValue();
        int gregorianDay = gregorianDateTime.getDayOfMonth();

        // Approximate the year (Ethiopian year is 7 or 8 years behind)
        int ethiopianYear = gregorianYear - 8;
        if (gregorianMonth > 9 || (gregorianMonth == 9 && gregorianDay >= 11)) {
            ethiopianYear = gregorianYear - 7;
        }

        // Approximate the month
        int ethiopianMonthIndex = (gregorianMonth + 3) % 12;
        String ethiopianMonth = ETHIOPIAN_MONTHS[ethiopianMonthIndex];
        
        // Format the time
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("h:mm a");
        String time = gregorianDateTime.format(timeFormatter);

        return String.format("%s %d, %d at %s", ethiopianMonth, gregorianDay, ethiopianYear, time);
    }
}