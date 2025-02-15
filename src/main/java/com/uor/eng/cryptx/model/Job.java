package com.uor.eng.cryptx.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "jobs")
@Data
@NoArgsConstructor
public class Job {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long jobId;

  private String role;
  private String description;
  private Double salary;

  @ManyToOne
  @JoinColumn(name = "buyer_id")
  private Buyer buyer;

  @OneToMany
  @JoinColumn(name = "job_id")
  private List<FarmerEmployee> farmerEmployees = new ArrayList<>();

  @OneToMany
  @JoinColumn(name = "job_id")
  private List<FormerFarmer> formerFarmers = new ArrayList<>();

  public Job(String role, String description, Double salary, Buyer buyer) {
    this.role = role;
    this.description = description;
    this.salary = salary;
    this.buyer = buyer;
  }
}
