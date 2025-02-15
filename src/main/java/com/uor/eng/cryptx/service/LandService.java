package com.uor.eng.cryptx.service;

import com.uor.eng.cryptx.exception.LandNotFoundException;
import com.uor.eng.cryptx.model.Land;
import com.uor.eng.cryptx.repository.LandRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class LandService {

  private final LandRepository landRepository;

  public LandService(LandRepository landRepository) {
    this.landRepository = landRepository;
  }

  public Land createLand(Land land) {
    // You can place validations or business checks here
    return landRepository.save(land);
  }

  public List<Land> getAllLands() {
    return landRepository.findAll();
  }

  public Land getLandById(Long id) {
    return landRepository.findById(id)
        .orElseThrow(() -> new LandNotFoundException("Land not found with id: " + id));
  }

  public Land updateLand(Long id, Land updatedLand) {
    Land existingLand = landRepository.findById(id)
        .orElseThrow(() -> new LandNotFoundException("Cannot update. Land not found with id: " + id));

    existingLand.setAddress(updatedLand.getAddress());
    existingLand.setMinPrice(updatedLand.getMinPrice());
    existingLand.setSize(updatedLand.getSize());
    existingLand.setDescription(updatedLand.getDescription());
    // Update any other fields you need

    return landRepository.save(existingLand);
  }

  public void deleteLand(Long id) {
    Land land = landRepository.findById(id)
        .orElseThrow(() -> new LandNotFoundException("Cannot delete. Land not found with id: " + id));
    landRepository.delete(land);
  }
}
