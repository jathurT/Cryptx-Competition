package com.uor.eng.cryptx.model;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@DiscriminatorValue("WORKER")
public class FarmerEmployee extends User {

  @ElementCollection
  @CollectionTable(name = "farmer_employee_skills", joinColumns = @JoinColumn(name = "farmer_employee_id"))
  @Column(name = "skill")
  private List<String> skillSet = new ArrayList<>();

  @Column(name = "experience_years")
  private int experienceYears;

  public FarmerEmployee(String userName, String email, String password, List<String> skillSet, int experienceYears) {
    super(userName, email, password);
    this.skillSet = skillSet;
    this.experienceYears = experienceYears;
  }
}
