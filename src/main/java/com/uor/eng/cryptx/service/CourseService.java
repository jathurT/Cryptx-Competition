package com.uor.eng.cryptx.service;

import com.uor.eng.cryptx.model.Course;

import java.util.List;

public interface CourseService {

  Course createCourse(Course course);

  List<Course> getAllCourses();

  Course getCourseById(Long id);

  Course updateCourse(Long id, Course updatedCourse);

  void deleteCourse(Long id);
}
