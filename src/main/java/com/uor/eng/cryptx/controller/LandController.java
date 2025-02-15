package com.uor.eng.cryptx.controller;

import com.uor.eng.cryptx.model.Land;
import com.uor.eng.cryptx.service.LandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lands")
public class LandController {

  @Autowired
  private LandService landService;

  @PostMapping("/create")
  public ResponseEntity<Land> createLand(@RequestBody Land land) {
    Land createdLand = landService.createLand(land);
    return new ResponseEntity<>(createdLand, HttpStatus.CREATED);
  }

  @GetMapping("/all")
  public ResponseEntity<List<Land>> getAllLands() {
    List<Land> lands = landService.getAllLands();
    return new ResponseEntity<>(lands, HttpStatus.OK);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Land> getLandById(@PathVariable Long id) {
    Land land = landService.getLandById(id);
    return new ResponseEntity<>(land, HttpStatus.OK);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Land> updateLand(@PathVariable Long id, @RequestBody Land updatedLand) {
    Land land = landService.updateLand(id, updatedLand);
    return new ResponseEntity<>(land, HttpStatus.OK);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteLand(@PathVariable Long id) {
    landService.deleteLand(id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
