package com.uor.eng.cryptx.payload.other;

public class LandPhotoResponse {
  private Long photoId;
  private String s3Key;
  private String message;

  public LandPhotoResponse() {}

  public LandPhotoResponse(Long photoId, String s3Key, String message) {
    this.photoId = photoId;
    this.s3Key = s3Key;
    this.message = message;
  }

  public Long getPhotoId() {
    return photoId;
  }
  public void setPhotoId(Long photoId) {
    this.photoId = photoId;
  }
  public String getS3Key() {
    return s3Key;
  }
  public void setS3Key(String s3Key) {
    this.s3Key = s3Key;
  }
  public String getMessage() {
    return message;
  }
  public void setMessage(String message) {
    this.message = message;
  }
}
