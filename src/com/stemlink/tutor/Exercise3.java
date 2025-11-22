// Exercise3.java
package com.stemlink.tutor;

import java.time.LocalDateTime;
import java.util.Objects;

public class Exercise3 {
    public static void main(String[] args) {
        // Create a booking
        Booking booking1 = new Booking(
                "STU-001",
                "MEN-005",
                "Core Java OOP",
                LocalDateTime.of(2025, 11, 20, 14, 0),
                2
        );

        System.out.println("Original Booking:");
        System.out.println(booking1.getBookingDetails());

        // Try to "modify" - must create new object
        Booking booking2 = new Booking(
                booking1.getStudentId(),
                booking1.getMentorId(),
                booking1.getSubject(),
                LocalDateTime.of(2025, 11, 21, 14, 0), // Changed date
                3 // Changed duration
        );

        System.out.println("\n'Modified' Booking (new object):");
        System.out.println(booking2.getBookingDetails());

        // Demonstrate immutability
        System.out.println("\nAre they the same object? " + (booking1 == booking2));
        System.out.println("Original booking unchanged? " +
                (booking1.getDurationHours() == 2));

        // Test equals and hashCode
        System.out.println("Are they equal? " + booking1.equals(booking2));

        // Create same booking ID to test equals
        Booking booking3 = new Booking("STU-001", "MEN-005", "Test",
                LocalDateTime.now(), 1);
        System.out.println("Same ID bookings equal? " + booking1.equals(booking3));
    }
}

class Booking {
    private final String bookingId;
    private final String studentId;
    private final String mentorId;
    private final String subject;
    private final LocalDateTime scheduledTime;
    private final int durationHours;
    private final LocalDateTime createdAt;
    private final String status;

    private static int bookingCounter = 1;

    public Booking(String studentId, String mentorId, String subject,
                   LocalDateTime scheduledTime, int durationHours) {
        this.bookingId = String.format("BK-%03d", bookingCounter++);
        this.studentId = studentId;
        this.mentorId = mentorId;
        this.subject = subject;
        this.scheduledTime = scheduledTime;
        this.durationHours = durationHours;
        this.createdAt = LocalDateTime.now();
        this.status = "PENDING";
    }

    // Getters only - no setters!
    public String getBookingId() { return bookingId; }
    public String getStudentId() { return studentId; }
    public String getMentorId() { return mentorId; }
    public String getSubject() { return subject; }
    public LocalDateTime getScheduledTime() { return scheduledTime; }
    public int getDurationHours() { return durationHours; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public String getStatus() { return status; }

    public String getBookingDetails() {
        return String.format(
                "Booking ID: %s\nStudent: %s | Mentor: %s\nSubject: %s\nScheduled: %s\nDuration: %d hours\nStatus: %s\nCreated: %s",
                bookingId, studentId, mentorId, subject,
                DateTimeFormatterUtil.formatDateTime(scheduledTime),
                durationHours, status,
                DateTimeFormatterUtil.formatDateTime(createdAt)
        );
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Booking booking = (Booking) obj;
        return Objects.equals(bookingId, booking.bookingId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookingId);
    }
}