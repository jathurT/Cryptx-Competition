package com.uor.eng.cryptx.service.Impl;

import com.uor.eng.cryptx.exception.CourseNotFoundException;
import com.uor.eng.cryptx.model.Course;
import com.uor.eng.cryptx.repository.CourseRepository;
import com.uor.eng.cryptx.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CourseServiceImpl implements CourseService {

  @Autowired
  private CourseRepository courseRepository;

  @Override
  public Course createCourse(Course course) {
    return courseRepository.save(course);
  }

  @Override
  public List<Course> getAllCourses() {
    return courseRepository.findAll();
  }

  @Override
  public Course getCourseById(Long id) {
    return courseRepository.findById(id)
        .orElseThrow(() -> new CourseNotFoundException("Course not found with id: " + id));
  }

  @Override
  public Course updateCourse(Long id, Course updatedCourse) {
    Course existingCourse = courseRepository.findById(id)
        .orElseThrow(() -> new CourseNotFoundException("Cannot update. Course not found with id: " + id));

    existingCourse.setCourseName(updatedCourse.getCourseName());
    existingCourse.setDescription(updatedCourse.getDescription());
    existingCourse.setCourseFee(updatedCourse.getCourseFee());
    return courseRepository.save(existingCourse);
  }

  @Override
  public void deleteCourse(Long id) {
    Course course = courseRepository.findById(id)
        .orElseThrow(() -> new CourseNotFoundException("Cannot delete. Course not found with id: " + id));
    courseRepository.delete(course);
  }
}
