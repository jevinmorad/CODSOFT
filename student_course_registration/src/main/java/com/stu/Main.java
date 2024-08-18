
package com.stu;

import java.util.List;
import java.util.Scanner;

/**
 * Main application to manage courses and students.
 */
public class Main {
    static Student student;

    public static void main(String[] args) {
        student = null;
        Scanner sc = new Scanner(System.in);

        System.out.println("\nWelcome to Student Course Registration");
        // 
        do {
            System.out.println("\n[1] Sign up");
            System.out.println("[2] Sign in");
            System.out.println("[3] Exit");
            System.out.print("Select an option: ");
            int choice1 = sc.nextInt();
            sc.nextLine(); // consume empty line
            switch (choice1) {
                case 1:
                    System.out.println("\n-------------Sign up-------------");
                    student = addStudent(sc);
                    if (student != null)
                        System.out.print("\nSign up successfully");
                    break;

                case 2:
                    System.out.println("\n-------------Sign in-------------");
                    student = login(sc);

                    if (student==null)
                        System.out.println("\n-> Password not found. Try again.");
                    else
                        System.out.print("\nSign in successfully");
                    break;

                case 3:
                    System.out.println("\nThank you for visit.\n");
                    return;

                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
        } while (student == null);

        // Options for the user
        boolean running = true;
        while (running) {
            System.out.println("\n\n[1] List all available courses");
            System.out.println("[2] List all registered course");
            System.out.println("[3] Register for a course");
            System.out.println("[4] Unregister from a course");
            System.out.println("[5] Exit");
            System.out.print("Select an option: ");
            int option = sc.nextInt();
            sc.nextLine();

            switch (option) {
                case 1:
                    listAllCourses();
                    break;
                case 2:
                    listRegisteredCourse();
                    break;
                case 3:
                    registerForCourse(sc);
                    break;
                case 4:
                    dropCourse(sc);
                    break;
                case 5:
                    System.out.println("\nThank you for the visit.\n");
                    running = false;
                    break;
                default:
                    System.out.println("\nInvalid option. Please try again.");
                    break;
            }
        }
        sc.close();
    }

    /**
     * Add new student in the database
     * 
     * @param sc
     */
    private static Student addStudent(Scanner sc) {
        System.out.print("Enter username: ");
        String name = sc.nextLine();
        // check for account not duplicate account name
        if (!DatabaseManager.checkNameAvailability(name)) {
            return null;
        }

        System.out.print("Enter password: ");
        String studentId = sc.nextLine();

        Student newStudent = new Student(name, studentId);

        return DatabaseManager.addStudent(newStudent);
    }

    /**
     * Login student in the system
     * 
     * @param sc
     * @return student
     */
    private static Student login(Scanner sc) {
        System.out.print("Enter password: ");
        String studentId = sc.nextLine();

        student = DatabaseManager.getStudentById(studentId); // return null if student not found in the database

        return student;
    }

    /**
     * List all available course in the database
     */
    private static void listAllCourses() {
        List<Course> courses = DatabaseManager.getAllCourses(); //Collection of all the courses available in database

        System.out.println("\n\n-> All courses...");
        System.out.printf("%n%-12s | %-30s | %-15s | %-15s | %s%n", "Course code", "Course name", "Total seats", "Available seats", "Description");
        System.out.println(
                "-----------------------------------------------------------------------------------------------------------------------------------");
        //print details of the courses
        for (Course course : courses) {
            course.toString1();
        }
    }

    /**
     * Print all registered courses details of student
     */
    private static void listRegisteredCourse() {
        String studentId = student.getStudentId(); // get studentID of the opened account

        List<Course> courses = DatabaseManager.getRegisteredCourses(studentId); //Collection of all the courses available in

        System.out.println("\n\n-> Registered course by you");
        System.out.printf("%n%-15s | %-35s | %-20s%n", "Course code", "Course name", "Description");
        System.out.println(
                "-------------------------------------------------------------------------------------------------------");
        //print details of the course
        for (Course course : courses) {
            course.toString2();
        }
    }

    /**
     * Register student for the available course
     * 
     * @param sc
     */
    private static void registerForCourse(Scanner sc) {
        // printing all courses
        listAllCourses();

        System.out.print("\nEnter course code to register: ");
        String courseCode = sc.nextLine();

        String studentId = student.getStudentId();

        DatabaseManager.registerStudentForCourse(studentId, courseCode);
    }

    /**
     * Drop course if student registered for it
     * 
     * @param sc
     */
    private static void dropCourse(Scanner sc) {
        listRegisteredCourse();

        System.out.print("\nEnter course code to remove: ");
        String courseCode = sc.nextLine();

        String studentId = student.getStudentId();

        DatabaseManager.dropCourse(studentId, courseCode);
    }
}
