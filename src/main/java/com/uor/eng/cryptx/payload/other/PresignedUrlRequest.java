package com.uor.eng.cryptx.payload.other;

import lombok.Data;

@Data
public class PresignedUrlRequest {
  private String fileName;
  private String contentType;
}