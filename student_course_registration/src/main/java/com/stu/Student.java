package com.stu;

/**
 * Represents a student with their details.
 */
public class Student {
    private String studentId;
    private String name;

    /**
     * Constructs a new Student.
     * 
     * @param studentId the student ID
     * @param name the student's name
     */
    public Student(String name, String studentId) {
        this.name = name;
        this.studentId = studentId;
    }

    // Getters and setters

    public String getStudentId() {
        return studentId;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Student{" +
                "studentId='" + studentId + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
