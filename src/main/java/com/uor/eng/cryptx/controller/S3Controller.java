package com.uor.eng.cryptx.controller;

import com.uor.eng.cryptx.payload.other.PresignedUrlRequest;
import com.uor.eng.cryptx.payload.other.PresignedUrlResponse;
import com.uor.eng.cryptx.util.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/s3")
public class S3Controller {

  @Autowired
  private S3Service s3Service;

  @PostMapping("/generate-presigned-url")
  public ResponseEntity<PresignedUrlResponse> generatePresignedUrl(@RequestBody PresignedUrlRequest request) {
    PresignedUrlResponse response = s3Service.generatePresignedUrl(request);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PostMapping("/upload")
  public ResponseEntity<String> uploadFile(@RequestBody MultipartFile file) {
    String fileKey = s3Service.uploadFile(file);
    return ResponseEntity.ok(fileKey);
  }
}
