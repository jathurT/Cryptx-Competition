package com.uor.eng.cryptx.service;

import com.uor.eng.cryptx.exception.LandNotFoundException;
import com.uor.eng.cryptx.model.Land;
import com.uor.eng.cryptx.model.LandPhoto;
import com.uor.eng.cryptx.payload.other.AssociatePhotosRequest;
import com.uor.eng.cryptx.payload.other.LandPhotoResponse;
import com.uor.eng.cryptx.repository.LandPhotoRepository;
import com.uor.eng.cryptx.repository.LandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class LandService {

  @Autowired
  private  LandRepository landRepository;

  @Autowired
  private LandPhotoRepository landPhotoRepository;

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
  public List<LandPhotoResponse> associatePhotosWithLand(Long landId, AssociatePhotosRequest request) {
    Land land = getLandById(landId);

    List<LandPhotoResponse> responses = new ArrayList<>();

    for (String key : request.getS3Keys()) {
      LandPhoto photo = new LandPhoto(key, land);
      LandPhoto saved = landPhotoRepository.save(photo);

      LandPhotoResponse resp = new LandPhotoResponse(
          saved.getId(),
          saved.getS3Key(),
          "Photo associated successfully."
      );
      responses.add(resp);
    }

    return responses;
  }
}
