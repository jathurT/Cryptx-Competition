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
@DiscriminatorValue("FORMER_FARMER")
public class FormerFarmer extends User {

  @Column(name = "years_of_farming_experience")
  private int yearsOfFarmingExperience;

  @ElementCollection
  @CollectionTable(
      name = "former_farmer_expertise",
      joinColumns = @JoinColumn(name = "former_farmer_id")
  )
  @Column(name = "expertise_area")
  private List<String> expertiseAreas = new ArrayList<>();

  public FormerFarmer(String userName, String email, String password,
                      int yearsOfFarmingExperience, List<String> expertiseAreas) {
    super(userName, email, password);
    this.yearsOfFarmingExperience = yearsOfFarmingExperience;
    this.expertiseAreas = expertiseAreas;
  }
}
