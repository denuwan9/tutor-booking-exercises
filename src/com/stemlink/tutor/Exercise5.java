// Exercise5.java - Main system integration
package com.stemlink.tutor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Exercise5 {
    public static void main(String[] args) {
        BookingSystem system = new BookingSystem();
        system.run();
    }
}

class BookingSystem {
    private static final List<Student> students = new ArrayList<>();
    private static final List<Mentor> mentors = new ArrayList<>();
    private static final List<Subject> subjects = new ArrayList<>();
    private static final List<Booking> bookings = new ArrayList<>();

    public void run() {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║   STEM Link Tutor Booking System     ║");
        System.out.println("╚════════════════════════════════════════╝\n");

        initializeSubjects();
        registerUsers();
        createBookings();
        displayStatistics();
    }

    private void initializeSubjects() {
        System.out.println("📚 Initializing subjects...");
        subjects.add(new Subject("JAVA-01", "Core Java OOP", 40, "PROGRAMMING"));
        subjects.add(new Subject("SPR-01", "Spring Boot Basics", 32, "WEB"));
        subjects.add(new Subject("DB-01", "Database Design", 24, "DATABASE"));
        subjects.add(new Subject("REACT-01", "React Fundamentals", 36, "WEB"));
        subjects.add(new Subject("DSA-01", "Data Structures", 48, "PROGRAMMING"));

        for (Subject subject : subjects) {
            System.out.println("  ✅ Added: " + subject.getSubjectCode() + " - " +
                    subject.getTitle() + " (" + subject.getCreditHours() + " hrs)");
        }
        System.out.println("\n");
    }

    private void registerUsers() {
        System.out.println("\n👥 Registering users...");

        // Create students
        Student student1 = new Student("Alice Johnson", "alice@stemlink.com");
        Student student2 = new Student("Bob Smith", "bob@stemlink.com");
        Student student3 = new Student("Carol White", "carol@stemlink.com");
        students.addAll(Arrays.asList(student1, student2, student3));


        // Create mentors
        Mentor mentor1 = new Mentor("Dr. Sarah Ahmed", "sarah@stemlink.com", "Java", 2000.0, 4.8);
        Mentor mentor2 = new Mentor("Prof. John Doe", "john@stemlink.com", "Spring Boot", 2500.0, 4.9);
        Mentor mentor3 = new Mentor("Ms. Emily Chen", "emily@stemlink.com", "React", 1800.0, 4.7);
        mentors.addAll(Arrays.asList(mentor1, mentor2, mentor3));

        for (Student student : students) {
            System.out.println("  👨‍🎓 Student registered: " + student.getDetails());
        }
        System.out.println("\n");
        for (Mentor mentor : mentors) {
            System.out.println("  👨‍🏫 Mentor registered: " + mentor.getDetails());
        }
    }

    private void createBookings() {
        System.out.println("\n📅 Creating bookings...");

        attemptBooking("STU-001", "MEN-001", "Core Java OOP",
                LocalDateTime.of(2025, 11, 20, 14, 0), 2);
        attemptBooking("STU-002", "MEN-002", "Spring Boot Basics",
                LocalDateTime.of(2025, 11, 18, 10, 0), 3);
        attemptBooking("STU-001", "MEN-003", "React Fundamentals",
                LocalDateTime.of(2025, 11, 15, 16, 0), 5); // Should fail
        attemptBooking("STU-003", "MEN-001", "Core Java OOP",
                LocalDateTime.of(2025, 11, 22, 9, 0), 1);
    }

    private static void attemptBooking(String studentId, String mentorId, String subject,
                                LocalDateTime scheduledTime, int duration) {
        System.out.println("\n  Attempting booking:");
        System.out.println("    Student: " + studentId);
        System.out.println("    Mentor: " + mentorId);
        System.out.println("    Subject: " + subject);
        System.out.println("    Time: " + DateTimeFormatterUtil.formatDateTime(scheduledTime));
        System.out.println("    Duration: " + duration + " hours");

        if (!BookingValidator.isValidBookingTime(scheduledTime)) {
            System.out.println("    ❌ Invalid booking time (must be in future)");
            return;
        }

        if (!BookingValidator.isValidDuration(duration)) {
            System.out.println("    ❌ Invalid duration (must be 1-4 hours)");
            return;
        }

        Booking booking = new Booking(studentId, mentorId, subject, scheduledTime, duration);
        bookings.add(booking);

        double baseFee = FeeCalculator.calculateBaseFee("STANDARD", duration);
        double platformFee = FeeCalculator.calculatePlatformFee(baseFee);
        double total = baseFee + platformFee;

        System.out.println("    ✅ Booking created: " + booking.getBookingId());
        System.out.println("    💰 Fees - Base: LKR " + baseFee +
                " | Platform: LKR " + platformFee +
                " | Total: LKR " + total);
    }

    private void displayStatistics() {
        System.out.println("\n📊 System Statistics");
        System.out.println("═══════════════════════════════════════");
        System.out.println("  Total Students: " + Student.getTotalStudents());
        System.out.println("  Total Mentors: " + Mentor.getTotalMentors());
        System.out.println("  Total Subjects: " + subjects.size());
        System.out.println("  Total Bookings: " + bookings.size());

        double totalRevenue = bookings.stream()
                .mapToDouble(booking -> FeeCalculator.calculateTotal("STANDARD", booking.getDurationHours()))
                .sum();
        System.out.println("  Total Revenue: LKR " + String.format("%.2f", totalRevenue));

        double avgDuration = bookings.stream()
                .mapToInt(Booking::getDurationHours)
                .average()
                .orElse(0.0);
        System.out.println("  Average Duration: " + avgDuration + " hours");
    }
}

// Additional classes for Exercise 5
class Mentor {
    private final String mentorId;
    private String name;
    private String email;
    private final String specialization;
    private final double hourlyRate;
    private final double rating;

    private static int totalMentors = 0;
    private static int mentorCounter = 1;

    public Mentor(String name, String email, String specialization, double hourlyRate, double rating) {
        this.mentorId = String.format("MEN-%03d", mentorCounter++);
        this.name = name;
        this.email = email;
        this.specialization = specialization;
        this.hourlyRate = hourlyRate;
        this.rating = Math.max(1.0, Math.min(5.0, rating)); // Clamp between 1-5

        totalMentors++;
    }

    public String getDetails() {
        return String.format("%s - %s (%s, LKR %.0f/hr, ⭐%.1f)",
                mentorId, name, specialization, hourlyRate, rating);
    }

    public static int getTotalMentors() {
        return totalMentors;
    }

    // Getters
    public String getMentorId() { return mentorId; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getSpecialization() { return specialization; }
    public double getHourlyRate() { return hourlyRate; }
    public double getRating() { return rating; }
}

final class Subject {
    private final String subjectCode;
    private final String title;
    private final int creditHours;
    private final String category;

    public Subject(String subjectCode, String title, int creditHours, String category) {
        this.subjectCode = subjectCode;
        this.title = title;
        this.creditHours = creditHours;
        this.category = category;
    }

    // Getters only
    public String getSubjectCode() { return subjectCode; }
    public String getTitle() { return title; }
    public int getCreditHours() { return creditHours; }
    public String getCategory() { return category; }

    @Override
    public String toString() {
        return String.format("%s - %s (%d hrs, %s)", subjectCode, title, creditHours, category);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Subject subject = (Subject) obj;
        return subjectCode.equals(subject.subjectCode);
    }

    @Override
    public int hashCode() {
        return subjectCode.hashCode();
    }
}

class IdGenerator {
    private static int studentCounter = 1;
    private static int mentorCounter = 1;
    private static int bookingCounter = 1;
    private static int subjectCounter = 1;

    private IdGenerator() {
        // Private constructor to prevent instantiation
    }

    public static String generateStudentId() {
        return String.format("STU-%03d", studentCounter++);
    }

    public static String generateMentorId() {
        return String.format("MEN-%03d", mentorCounter++);
    }

    public static String generateBookingId() {
        return String.format("BK-%03d", bookingCounter++);
    }

    public static String generateSubjectId(String category) {
        String prefix;
        switch (category.toUpperCase()) {
            case "PROGRAMMING": prefix = "JAVA"; break;
            case "WEB": prefix = "WEB"; break;
            case "DATABASE": prefix = "DB"; break;
            default: prefix = "GEN";
        }
        return String.format("%s-%02d", prefix, subjectCounter++);
    }
}

class PlatformConstants {
    // Booking rules
    public static final int MIN_BOOKING_HOURS = 1;
    public static final int MAX_BOOKING_HOURS = 4;
    public static final int MAX_ADVANCE_BOOKING_DAYS = 30;

    // Fee structure
    public static final double STANDARD_HOURLY_RATE = 1500.0;
    public static final double URGENT_MULTIPLIER = 1.5;
    public static final double GROUP_DISCOUNT = 0.8;
    public static final double PLATFORM_FEE_PERCENTAGE = 0.15;

    // Ratings
    public static final double MIN_MENTOR_RATING = 1.0;
    public static final double MAX_MENTOR_RATING = 5.0;
    public static final double MINIMUM_ACCEPTABLE_RATING = 3.0;

    private PlatformConstants() {
        throw new AssertionError("Cannot instantiate constants class");
    }
}