package com.uor.eng.cryptx.model;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@DiscriminatorValue("BUYER")
public class Buyer extends User {

  @ElementCollection
  @CollectionTable(name = "buyer_interests", joinColumns = @JoinColumn(name = "buyer_id"))
  @Column(name = "interest")
  private List<String> interests = new ArrayList<>();

  @ManyToMany
  @JoinTable(
      name = "buyer_courses",
      joinColumns = @JoinColumn(name = "buyer_id"),
      inverseJoinColumns = @JoinColumn(name = "course_id")
  )
  private List<Course> purchasedCourses = new ArrayList<>();

  public Buyer(String userName, String email, String password, List<String> interests) {
    super(userName, email, password);
    this.interests = interests;
  }

  public void purchaseCourse(Course course) {
    this.purchasedCourses.add(course);
    course.getEnrolledYoungMinds().add(this);
  }
}
