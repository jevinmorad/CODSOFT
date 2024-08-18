package com.stu;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages database connections and operations.
 */
public class DatabaseManager {
    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=CourseManagement;encrypt=false;integratedSecurity=true;";

    //check for JDBC driver availability
    static {
        try {
            // Load the SQL Server JDBC driver
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Establishes a connection to the SQL Server database.
     * 
     * @return the database connection
     * @throws SQLException if a database access error occurs
     */
    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    /**
     * Check if name is available.
     * 
     * @param name
     */
    public static boolean checkNameAvailability(String name) {
        String checkName = "SELECT student_name FROM Students WHERE student_name = ?";
        try (Connection conn = connect();
                PreparedStatement pstmt = conn.prepareStatement(checkName)) {
            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next() && rs.getString(1).equals(name)) {
                System.out.println("\n--> Other account found with same name. Try again");
                return false;
            }
        } catch (Exception e) {
            System.out.println("Error in checking name");
            e.printStackTrace();
        }

        return true;
    }

    /**
     * Adds a new student to the database.
     * 
     * @param student the student to be added
     */
    public static Student addStudent(Student student) {
        String sql = "INSERT INTO Students (student_id,student_name) VALUES (?, ?)";

        try (Connection conn = connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, student.getStudentId());
            pstmt.setString(2, student.getName());
            pstmt.executeUpdate();

            System.out.println("--> Account created successfully.\n");
        } catch (SQLException e) {
            if (e.getSQLState().startsWith("23")) { // SQL state code for integrity constraint violation
                System.out.println("Error: A student with this ID already exists.");
            } else {
                System.out.println("Error adding student: ");
                e.printStackTrace();
            }
        }

        return student;
    }

    /**
     * Retrieves a student by their ID.
     * 
     * @param studentId the student's ID
     * @return the student object or null if not found
     */
    public static Student getStudentById(String studentId) {
        String sql = "SELECT * FROM Students WHERE student_id = ?";
        Student student = null;

        try (Connection conn = connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, studentId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                student = new Student(rs.getString("student_name"), rs.getString("student_id"));
            }

        } catch (SQLException e) {
            System.out.println("Error retrieving student: ");
            e.printStackTrace();
        }

        return student;
    }

    /**
     * Retrieves all courses from the database.
     * 
     * @return a list of all courses
     */
    public static List<Course> getAllCourses() {
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT course_code, course_name, description, capacity, enrolled_student_count FROM Courses";

        try (Connection conn = connect();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Course course = new Course(
                        rs.getString("course_code"),
                        rs.getString("course_name"),
                        rs.getString("description"),
                        rs.getInt("capacity"),
                        rs.getInt("enrolled_student_count"));
                courses.add(course);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return courses;
    }

    /**
     * Retrieves the courses a student is registered for.
     * 
     * @param studentId the student's ID
     * @return a list of courses the student is registered for
     */
    public static List<Course> getRegisteredCourses(String studentId) {
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT Courses.course_code, Courses.course_name, Courses.description " +
                "FROM Students " +
                "JOIN Enrollment ON Students.student_id = Enrollment.student_id " +
                "JOIN Courses ON Enrollment.course_code = Courses.course_code " +
                "WHERE Students.student_id = ?";

        try (Connection conn = connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Set the studentId parameter
            pstmt.setString(1, studentId);

            // Execute the query
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                // Map the result set to the Course object
                Course course = new Course(
                        rs.getString("course_code"),
                        rs.getString("course_name"),
                        rs.getString("description"));
                courses.add(course);
            }

        } catch (SQLException e) {
            System.out.println("Error retrieving registered courses:");
            e.printStackTrace();
        }

        return courses;
    }

    /**
     * Registers a student for a course.
     * 
     * @param studentId  the student's ID
     * @param courseCode the course code
     */
    public static void registerStudentForCourse(String studentId, String courseCode) {
        String checkCourseCapacitySql = "SELECT capacity, enrolled_student_count FROM Courses WHERE course_code = ?";
        String checkEnrollmentSql = "SELECT student_id FROM Enrollment WHERE student_id = ? AND course_code = ?";
        String registerStudentSql = "INSERT INTO Enrollment (student_id, course_code) VALUES (?, ?)";

        try (Connection conn = connect()) {
            // Check if student is already enrolled in the course
            try (PreparedStatement checkEnrollmentStmt = conn.prepareStatement(checkEnrollmentSql)) {
                checkEnrollmentStmt.setString(1, studentId);
                checkEnrollmentStmt.setString(2, courseCode);
                ResultSet enrollmentRs = checkEnrollmentStmt.executeQuery();

                if (enrollmentRs.next()) {
                    System.out.println("\n--> It looks like you're already registered for the course!");
                    return; // Exit the method since the student is already enrolled
                }
            }

            // for the capacity
            try (PreparedStatement checkStmt = conn.prepareStatement(checkCourseCapacitySql)) {
                checkStmt.setString(1, courseCode);
                ResultSet rs = checkStmt.executeQuery();

                if (rs.next()) {
                    int capacity = rs.getInt("capacity");
                    int enrolled_student_count = rs.getInt("enrolled_student_count");

                    // Check for capacity
                    if (enrolled_student_count < capacity) {

                        // Add course
                        try (PreparedStatement registerStmt = conn.prepareStatement(registerStudentSql)) {
                            registerStmt.setString(1, studentId);
                            registerStmt.setString(2, courseCode);
                            registerStmt.executeUpdate();

                            System.out.println("\n--> Registration for the course has been successfully completed!");
                        } catch (SQLException e) {
                            System.out.println("Error in adding course.");
                            e.printStackTrace();
                        }
                    } else {
                        System.out.println(
                                "\n--> We're sorry, but the course is currently at full capacity and we're unable to process additional registrations at this time.");
                    }
                } else {
                    System.out.println(
                            "\n--> We're unable to find the course you're looking for. Please check the course name or code and try again.");
                }
            } catch (SQLException e) {
                System.out.println("Error during registration");
                e.printStackTrace();
            }

        } catch (SQLException e) {
            System.out.println("Error connecting with the database");
            e.printStackTrace();
        }
    }

    /**
     * Drops a student from a course.
     * 
     * @param studentId  the student's ID
     * @param courseCode the course code
     */
    public static void dropCourse(String studentId, String courseCode) {
        String checkAvailability = "SELECT course_code FROM Enrollment WHERE student_id = ? AND course_code = ?";
        String deleteString = "DELETE FROM Enrollment WHERE student_id = ? AND course_code = ?";

        try (Connection conn = connect()) {
            conn.setAutoCommit(false);

            // Check availability
            try (PreparedStatement checkStmt = conn.prepareStatement(checkAvailability)) {
                checkStmt.setString(1, studentId);
                checkStmt.setString(2, courseCode);
                ResultSet rs = checkStmt.executeQuery();

                // Course found
                if (rs.next()) {
                    // Delete course
                    try (PreparedStatement deleteStmt = conn.prepareStatement(deleteString)) {
                        deleteStmt.setString(1, studentId);
                        deleteStmt.setString(2, courseCode);
                        int rowsAffected = deleteStmt.executeUpdate();

                        if (rowsAffected > 0) {
                            conn.commit();
                            System.out.println("\n--> The course has been successfully removed from your account.");
                        } else {
                            System.out.println("\n--> It looks like you haven't registered for the course yet.");
                        }
                    } catch (Exception e) {
                        conn.rollback();
                        System.out.println("Error in deleting course");
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("\n--> Course not found in your account.");
                }
            } catch (Exception e) {
                conn.rollback();
                System.out.println("Error in finding course: ");
                e.printStackTrace();
            }
        } catch (SQLException e) {
            System.out.println("Error deleting Course: ");
            e.printStackTrace();
        }
    }

}
