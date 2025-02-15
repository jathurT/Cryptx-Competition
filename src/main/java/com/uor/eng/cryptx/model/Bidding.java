package com.uor.eng.cryptx.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "bidding")
public class Bidding {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "bid_timestamp")
  private LocalDateTime dateTime;

  @OneToOne
  @JoinColumn(name = "landowner_id")
  private LandOwner landOwner;


  @OneToMany
  @JoinColumn(name = "bidding_id")  // This foreign key column goes in the buyers table
  private List<Buyer> buyers = new ArrayList<>();

  public Bidding(LocalDateTime dateTime, LandOwner landOwner, List<Buyer> buyers) {
    this.dateTime = dateTime;
    this.landOwner = landOwner;
    this.buyers = buyers;
  }
}
