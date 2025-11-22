// Exercise4.java
package com.stemlink.tutor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Exercise4 {
    public static void main(String[] args) {
        System.out.println("=== Creating Different Session Requests ===\n");

        // 1. Simple session (only required fields)
        SessionRequest simple = new SessionRequest.Builder(
                "STU-001",
                "MEN-003",
                "Java Basics"
        ).build();

        System.out.println("1. Simple Session:");
        System.out.println(simple);
        System.out.println();

        // 2. Detailed session with notes and materials
        SessionRequest detailed = new SessionRequest.Builder(
                "STU-002",
                "MEN-005",
                "Spring Boot REST APIs"
        )
                .sessionNotes("Focus on exception handling and validation")
                .materialsNeeded(Arrays.asList("Laptop", "Spring Boot docs", "Postman"))
                .preferredTime(LocalDateTime.now().plusDays(3))
                .build();

        System.out.println("2. Detailed Session:");
        System.out.println(detailed);
        System.out.println();

        // 3. Urgent session
        SessionRequest urgent = new SessionRequest.Builder(
                "STU-003",
                "MEN-002",
                "Database Design"
        )
                .isUrgent(true)
                .preferredTime(LocalDateTime.now().plusDays(2))
                .sessionNotes("Need help before exam!")
                .build();

        System.out.println("3. Urgent Session:");
        System.out.println(urgent);
        System.out.println();

        // 4. Group session
        SessionRequest group = new SessionRequest.Builder(
                "STU-004",
                "MEN-001",
                "React Fundamentals"
        )
                .maxStudents(4)
                .materialsNeeded(Arrays.asList("Laptop", "VS Code", "Node.js installed"))
                .build();

        System.out.println("4. Group Session:");
        System.out.println(group);
        System.out.println();

        // 5. Test validation - try invalid group size
        try {
            SessionRequest invalid = new SessionRequest.Builder(
                    "STU-005",
                    "MEN-002",
                    "Advanced Java"
            )
                    .maxStudents(1) // Should be at least 2 for group
                    .build();
        } catch (IllegalArgumentException e) {
            System.out.println("5. Validation Test:");
            System.out.println("✅ Caught error: " + e.getMessage());
        }

        // 6. Test urgent booking too far in advance
        try {
            SessionRequest invalidUrgent = new SessionRequest.Builder(
                    "STU-006",
                    "MEN-003",
                    "Machine Learning"
            )
                    .isUrgent(true)
                    .preferredTime(LocalDateTime.now().plusDays(10)) // Too far for urgent
                    .build();
        } catch (IllegalArgumentException e) {
            System.out.println("6. Urgent Validation Test:");
            System.out.println("✅ Caught error: " + e.getMessage());
        }
    }
}

class SessionRequest {
    private final String studentId;
    private final String mentorId;
    private final String subject;
    private final String sessionNotes;
    private final List<String> materialsNeeded;
    private final boolean isUrgent;
    private final LocalDateTime preferredTime;
    private final int maxStudents;

    private SessionRequest(Builder builder) {
        this.studentId = builder.studentId;
        this.mentorId = builder.mentorId;
        this.subject = builder.subject;
        this.sessionNotes = builder.sessionNotes;
        this.materialsNeeded = new ArrayList<>(builder.materialsNeeded); // Defensive copy
        this.isUrgent = builder.isUrgent;
        this.preferredTime = builder.preferredTime;
        this.maxStudents = builder.maxStudents;
    }

    // Getters only
    public String getStudentId() { return studentId; }
    public String getMentorId() { return mentorId; }
    public String getSubject() { return subject; }
    public String getSessionNotes() { return sessionNotes; }
    public List<String> getMaterialsNeeded() { return new ArrayList<>(materialsNeeded); }
    public boolean isUrgent() { return isUrgent; }
    public LocalDateTime getPreferredTime() { return preferredTime; }
    public int getMaxStudents() { return maxStudents; }

    @Override
    public String toString() {
        String sessionType = maxStudents > 1 ?
                String.format("Group (max %d students)", maxStudents) : "Individual";

        String timeInfo = preferredTime != null ?
                DateTimeFormatterUtil.formatDateTime(preferredTime) : "Not specified";

        String notesInfo = sessionNotes.isEmpty() ? "(none)" : sessionNotes;
        String materialsInfo = materialsNeeded.isEmpty() ? "(none)" : materialsNeeded.toString();

        return String.format(
                "SessionRequest {\n" +
                        "  Student: %s | Mentor: %s\n" +
                        "  Subject: %s\n" +
                        "  Type: %s | Urgent: %s\n" +
                        "  Notes: %s\n" +
                        "  Materials: %s\n" +
                        "  Preferred Time: %s\n" +
                        "}",
                studentId, mentorId, subject, sessionType,
                isUrgent ? "Yes" : "No", notesInfo, materialsInfo, timeInfo
        );
    }

    public static class Builder {
        private final String studentId;
        private final String mentorId;
        private final String subject;

        private String sessionNotes = "";
        private List<String> materialsNeeded = new ArrayList<>();
        private boolean isUrgent = false;
        private LocalDateTime preferredTime = null;
        private int maxStudents = 1;

        public Builder(String studentId, String mentorId, String subject) {
            this.studentId = studentId;
            this.mentorId = mentorId;
            this.subject = subject;
        }

        public Builder sessionNotes(String notes) {
            this.sessionNotes = notes;
            return this;
        }

        public Builder materialsNeeded(List<String> materials) {
            this.materialsNeeded = new ArrayList<>(materials);
            return this;
        }

        public Builder isUrgent(boolean urgent) {
            this.isUrgent = urgent;
            return this;
        }

        public Builder preferredTime(LocalDateTime time) {
            this.preferredTime = time;
            return this;
        }

        public Builder maxStudents(int maxStudents) {
            this.maxStudents = maxStudents;
            return this;
        }

        public SessionRequest build() {
            validate();
            return new SessionRequest(this);
        }

        private void validate() {
            if (subject == null || subject.trim().isEmpty()) {
                throw new IllegalArgumentException("Subject must not be empty");
            }

            if (maxStudents > 1 && maxStudents < 2) {
                throw new IllegalArgumentException("Group sessions must allow at least 2 students");
            }

            if (isUrgent && preferredTime != null &&
                    preferredTime.isAfter(LocalDateTime.now().plusDays(7))) {
                throw new IllegalArgumentException("Urgent sessions cannot be scheduled more than 7 days in advance");
            }
        }
    }
}