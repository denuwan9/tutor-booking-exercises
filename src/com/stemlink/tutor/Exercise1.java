
package com.stemlink.tutor;

import java.time.LocalDateTime;

public class Exercise1 {
    public static void main(String[] args) {
        System.out.println("Creating students...");

        Student student1 = new Student("Alice Johnson", "alice@email.com");
        Student student2 = new Student("Bob Smith", "bob@email.com");
        Student student3 = new Student("Carol White", "carol@email.com");

        System.out.println("\nStudent Details:");
        System.out.println(student1.getDetails());
        System.out.println(student2.getDetails());
        System.out.println(student3.getDetails());

        System.out.println("\nTotal students registered: " + Student.getTotalStudents());
    }
}

class Student {
    private final String studentId;
    private String name;
    private String email;

    private static int totalStudents = 0;
    private static int studentCounter = 1;

    public Student(String name, String email) {
        this.studentId = String.format("STU-%03d", studentCounter);
        this.name = name;
        this.email = email;

        studentCounter++;
        totalStudents++;

        System.out.println("Student created: " + this.studentId + ", " + this.name + ", " + this.email);
    }

    public String getDetails() {
        return String.format("%s, %s, %s", studentId, name, email);
    }

    public static int getTotalStudents() {
        return totalStudents;
    }

    // Getters
    public String getStudentId() { return studentId; }
    public String getName() { return name; }
    public String getEmail() { return email; }
}