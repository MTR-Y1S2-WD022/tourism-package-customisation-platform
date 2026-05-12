package com.tourismplatform.util;

import java.time.LocalDate;

public class DateStatusUtil {

    public static String calculateTourStatus(String bookingStatus, LocalDate startDate, LocalDate endDate) {

        LocalDate currentDate = LocalDate.now();

        if (bookingStatus != null && bookingStatus.equalsIgnoreCase("CANCELLED")) {
            return "CANCELLED";
        }

        if (startDate == null || endDate == null) {
            return "UNKNOWN";
        }

        if (currentDate.isBefore(startDate)) {
            return "UPCOMING";
        }

        if ((currentDate.isEqual(startDate) || currentDate.isAfter(startDate))
                && (currentDate.isEqual(endDate) || currentDate.isBefore(endDate))) {
            return "ONGOING";
        }

        if (currentDate.isAfter(endDate)) {
            return "COMPLETED";
        }

        return "UNKNOWN";
    }
}
