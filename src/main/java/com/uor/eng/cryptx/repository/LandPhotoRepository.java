package com.uor.eng.cryptx.repository;

import com.uor.eng.cryptx.model.LandPhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LandPhotoRepository extends JpaRepository<LandPhoto, Long> {
}
