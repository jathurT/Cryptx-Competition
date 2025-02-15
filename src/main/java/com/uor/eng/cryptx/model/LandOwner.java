package com.uor.eng.cryptx.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@DiscriminatorValue("LANDOWNER")
public class LandOwner extends User {

  // Any special fields for land owners can be added here

  public LandOwner(String userName, String email, String password) {
    super(userName, email, password);
  }
}
