package com.uor.eng.cryptx.repository;

import com.uor.eng.cryptx.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
  // Add any custom query methods if necessary
}
