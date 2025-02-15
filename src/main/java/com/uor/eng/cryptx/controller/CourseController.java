package com.uor.eng.cryptx.controller;

import com.uor.eng.cryptx.model.Course;
import com.uor.eng.cryptx.service.CourseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

  private final CourseService courseService;

  // Inject CourseService via constructor
  public CourseController(CourseService courseService) {
    this.courseService = courseService;
  }

  // CREATE
  @PostMapping
  public ResponseEntity<Course> createCourse(@RequestBody Course course) {
    Course created = courseService.createCourse(course);
    return new ResponseEntity<>(created, HttpStatus.CREATED);
  }

  // READ - Get all
  @GetMapping
  public ResponseEntity<List<Course>> getAllCourses() {
    List<Course> courses = courseService.getAllCourses();
    return new ResponseEntity<>(courses, HttpStatus.OK);
  }

  // READ - Get by ID
  @GetMapping("/{id}")
  public ResponseEntity<Course> getCourseById(@PathVariable Long id) {
    Course course = courseService.getCourseById(id);
    return new ResponseEntity<>(course, HttpStatus.OK);
  }

  // UPDATE
  @PutMapping("/{id}")
  public ResponseEntity<Course> updateCourse(@PathVariable Long id, @RequestBody Course updatedCourse) {
    Course course = courseService.updateCourse(id, updatedCourse);
    return new ResponseEntity<>(course, HttpStatus.OK);
  }

  // DELETE
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
    courseService.deleteCourse(id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
