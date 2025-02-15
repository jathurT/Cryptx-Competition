package com.uor.eng.cryptx.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "land_photos")
public class LandPhoto {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String s3Key; // The S3 file key

  @ManyToOne
  @JoinColumn(name = "land_id")
  private Land land;

  public LandPhoto(String s3Key, Land land) {
    this.s3Key = s3Key;
    this.land = land;
  }
}
