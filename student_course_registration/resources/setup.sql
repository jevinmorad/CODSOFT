-- The schema of the database

CREATE DATABASE CourseManagement;

USE CourseManagement;

CREATE TABLE Students (
    student_id NVARCHAR(50) PRIMARY KEY,
    student_name NVARCHAR(50) NOT NULL
);

CREATE TABLE Courses (
    course_code NVARCHAR(50) PRIMARY KEY,
    course_name NVARCHAR(50) NOT NULL,
    description NVARCHAR(100),
    capacity INT NOT NULL,
    enrolled_student_count INT DEFAULT 0
);

CREATE TABLE Enrollment (
    enrollment_id INT PRIMARY KEY IDENTITY(1,1),
    student_id NVARCHAR(50) NOT NULL,
    course_code NVARCHAR(50) NOT NULL,
    FOREIGN KEY (student_id) REFERENCES Students(student_id),
    FOREIGN KEY (course_code) REFERENCES Courses(course_code)
);

SELECT Courses.course_code, Courses.course_name, Courses.description 
FROM Students 
JOIN Enrollment ON Students.student_id = Enrollment.student_id 
JOIN Courses ON Enrollment.course_code = Courses.course_code 
WHERE Students.student_id = '1171'

-- Trigger to update enrolled_student_count when a new enrollment is added
CREATE TRIGGER trg_UpdateEnrolledCount
ON Enrollment
AFTER INSERT
AS
BEGIN
    UPDATE Courses
    SET enrolled_student_count = enrolled_student_count + 1
    FROM Courses
    INNER JOIN inserted i ON Courses.course_code = i.course_code;
END;

-- Trigger to update enrolled_student_count when an enrollment is deleted
CREATE TRIGGER trg_UpdateEnrolledCountDelete
ON Enrollment
AFTER DELETE
AS
BEGIN
    UPDATE Courses
    SET enrolled_student_count = enrolled_student_count - 1
    FROM Courses
    INNER JOIN deleted d ON Courses.course_code = d.course_code;
END;

-- Insert sample data into tables

INSERT INTO Students (student_id, student_name)
VALUES
    ('1148', 'Yash'),
    ('1263', 'Bob'),
    ('1358', 'Carol');

INSERT INTO Courses (course_code, course_name, description, capacity, enrolled_student_count)
VALUES
    ('101', 'Introduction to Programming', 'Learn the basics of programming using Python.', 2, 0),
    ('102', 'Database Management Systems', 'An overview of database design and SQL.', 25, 0),
    ('103', 'Web Development Fundamentals', 'Introduction to HTML, CSS, and JavaScript.', 20, 0);

INSERT INTO Enrollment (student_id, course_code)
VALUES
    ('1148', '101'),
    ('1148', '102'),
    ('1263', '103'),
    ('1358', '101');
