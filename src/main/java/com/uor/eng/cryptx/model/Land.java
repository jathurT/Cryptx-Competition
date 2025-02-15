package com.uor.eng.cryptx.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "lands")
public class Land {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long landId;

  private String address;

  private Double minPrice;

  private Double size;

  private String description;


  @OneToOne
  @JoinColumn(name = "landowner_id")
  private LandOwner landOwner;

  @OneToOne
  @JoinColumn(name = "buyer_id")
  private Buyer buyer;

  public Land(Long addressId, Double minPrice, Double size, String description, LandOwner landOwner, Buyer buyer, String address) {
    this.landId = addressId;
    this.address = address;
    this.minPrice = minPrice;
    this.size = size;
    this.description = description;
    this.landOwner = landOwner;
    this.buyer = buyer;
  }

  public Land(Long addressId, Double minPrice, Double size, String description, LandOwner landOwner, String address) {
    this.landId = addressId;
    this.address = address;
    this.minPrice = minPrice;
    this.size = size;
    this.description = description;
    this.landOwner = landOwner;
  }
}
