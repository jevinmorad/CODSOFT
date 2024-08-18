package com.stu;

/**
 * Represents a course with its details.
 */
public class Course {

    private String course_code;
    private String course_name;
    private String description;
    private int capacity;
    private int enrolled_student_count;

    /**
     * @param course_code the course code
     * @param course_name the course course_name
     * @param description the course description
     * @param capacity the maximum number of students
     * @param enrolled_student_count the current number of enrolled_student_count students
     */
    public Course(String course_code, String course_name, String description, int capacity, int enrolled_student_count ) {
        this.course_code = course_code;
        this.course_name = course_name;
        this.description = description;
        this.capacity = capacity;
        this.enrolled_student_count = enrolled_student_count;
    }
    /**
     * @param course_code
     * @param course_name
     * @param description
     * @param slot_name
     */
    public Course(String course_code, String course_name, String description) {
        this.course_code = course_code;
        this.course_name = course_name;
        this.description = description;
    }

    public void toString1() {
        System.out.printf("%-12s | %-30s | %-15s | %-15s | %s%n", course_code, course_name, capacity, enrolled_student_count, description);
    }

    public void toString2() {
        System.out.printf("%-15s | %-35s | %-20s%n", course_code, course_name, description);
    }
}
