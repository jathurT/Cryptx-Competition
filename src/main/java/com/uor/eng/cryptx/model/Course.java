package com.uor.eng.cryptx.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "courses")
public class Course {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long courseId;

  private String courseName;
  private String description;
  private String courseFee;

  /**
   * Changed to `mappedBy = "purchasedCourses"` so it matches
   * the field name in Buyer.java.
   */
  @ManyToMany(mappedBy = "purchasedCourses")
  private List<Buyer> enrolledYoungMinds = new ArrayList<>();

  public Course(String courseName, String description, String courseFee) {
    this.courseName = courseName;
    this.description = description;
    this.courseFee = courseFee;
  }
}
