
package com.stemlink.tutor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Exercise2 {
    public static void main(String[] args) {
        // Test BookingValidator
        System.out.println("=== Testing BookingValidator ===");
        LocalDateTime futureTime = LocalDateTime.now().plusDays(2);
        LocalDateTime pastTime = LocalDateTime.now().minusDays(1);

        System.out.println("Future time valid? " +
                BookingValidator.isValidBookingTime(futureTime));
        System.out.println("Past time valid? " +
                BookingValidator.isValidBookingTime(pastTime));
        System.out.println("3 hours valid? " +
                BookingValidator.isValidDuration(3));
        System.out.println("5 hours valid? " +
                BookingValidator.isValidDuration(5));

        // Test slot availability
        LocalDateTime existingStart = LocalDateTime.of(2025, 11, 20, 14, 0);
        LocalDateTime existingEnd = existingStart.plusHours(2);
        LocalDateTime newStart = LocalDateTime.of(2025, 11, 20, 15, 0);
        LocalDateTime newEnd = newStart.plusHours(2);

        System.out.println("Slot available? " +
                BookingValidator.isSlotAvailable(newStart, newEnd, existingStart, existingEnd));

        // Test FeeCalculator
        System.out.println("\n=== Testing FeeCalculator ===");
        System.out.println("Standard 2h: LKR " +
                FeeCalculator.calculateTotal("STANDARD", 2));
        System.out.println("Urgent 3h: LKR " +
                FeeCalculator.calculateTotal("URGENT", 3));
        System.out.println("Group 4h: LKR " +
                FeeCalculator.calculateTotal("GROUP", 4));

        // Test DateTimeFormatter
        System.out.println("\n=== Testing DateTimeFormatter ===");
        LocalDateTime now = LocalDateTime.now();
        System.out.println("Standard: " +
                DateTimeFormatterUtil.formatDateTime(now));
        System.out.println("Long: " +
                DateTimeFormatterUtil.formatDateLong(now));
        System.out.println("Time: " +
                DateTimeFormatterUtil.formatTime(now));
    }
}

class BookingValidator {
    private BookingValidator() {

    }

    public static boolean isValidBookingTime(LocalDateTime bookingTime) {
        return bookingTime.isAfter(LocalDateTime.now());
    }

    public static boolean isValidDuration(int hours) {
        return hours >= 1 && hours <= 4;
    }

    public static boolean isSlotAvailable(
            LocalDateTime newStart,
            LocalDateTime newEnd,
            LocalDateTime existingStart,
            LocalDateTime existingEnd
    ) {
        return newEnd.isBefore(existingStart) || newStart.isAfter(existingEnd);
    }
}

class FeeCalculator {
    private static final double STANDARD_RATE = 1500.0;
    private static final double URGENT_RATE = 2500.0;
    private static final double GROUP_RATE = 1200.0;
    private static final double PLATFORM_FEE_PERCENTAGE = 0.15;

    private FeeCalculator() {

    }

    public static double calculateBaseFee(String bookingType, int hours) {
        double rate;
        switch (bookingType.toUpperCase()) {
            case "URGENT":
                rate = URGENT_RATE;
                break;
            case "GROUP":
                rate = GROUP_RATE;
                break;
            default:
                rate = STANDARD_RATE;
        }
        return rate * hours;
    }

    public static double calculatePlatformFee(double baseFee) {
        return baseFee * PLATFORM_FEE_PERCENTAGE;
    }

    public static double calculateTotal(String bookingType, int hours) {
        double baseFee = calculateBaseFee(bookingType, hours);
        double platformFee = calculatePlatformFee(baseFee);
        return baseFee + platformFee;
    }
}

class DateTimeFormatterUtil {
    private DateTimeFormatterUtil() {

    }

    public static String formatDateTime(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return dateTime.format(formatter);
    }

    public static String formatDateLong(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy");
        return dateTime.format(formatter);
    }

    public static String formatTime(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mm a");
        return dateTime.format(formatter);
    }
}