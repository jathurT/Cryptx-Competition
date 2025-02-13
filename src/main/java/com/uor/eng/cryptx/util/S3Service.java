package com.uor.eng.cryptx.util;

import com.uor.eng.cryptx.exceptions.FileStorageException;
import com.uor.eng.cryptx.payload.other.PresignedUrlRequest;
import com.uor.eng.cryptx.payload.other.PresignedUrlResponse;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.io.IOException;
import java.time.Duration;
import java.util.Objects;
import java.util.UUID;

@Service
public class S3Service {

  private final S3Client s3Client;

  private S3Presigner presigner;

  @Value("${aws.s3.bucket}")
  private String bucketName;

  @Value("${aws.region}")
  private String region;

  public S3Service(S3Client s3Client) {
    this.s3Client = s3Client;
  }

  public String uploadFile(MultipartFile file) {
    String key = generateUniqueKey(Objects.requireNonNull(file.getOriginalFilename()));

    try {
      PutObjectRequest putObjectRequest = PutObjectRequest.builder()
          .bucket(bucketName)
          .key(key)
//          .acl(ObjectCannedACL.PUBLIC_READ)
          .contentType(file.getContentType())
          .build();

      s3Client.putObject(putObjectRequest, software.amazon.awssdk.core.sync.RequestBody.fromBytes(file.getBytes()));
    } catch (S3Exception e) {
      throw new FileStorageException("Failed to upload file to S3: " + e.awsErrorDetails().errorMessage(), e);
    } catch (IOException e) {
      throw new FileStorageException("Failed to read file data: " + e.getMessage(), e);
    }

    return key;
  }

  public void deleteFile(String key) {
    try {
      DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
          .bucket(bucketName)
          .key(key)
          .build();
      s3Client.deleteObject(deleteObjectRequest);
    } catch (S3Exception e) {
      throw new FileStorageException("Failed to delete file from S3: " + e.awsErrorDetails().errorMessage(), e);
    }
  }

  public String getFileUrl(String key) {
    return String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, region, key);
  }

  public String generateUniqueKey(String originalFilename) {
    String uuid = UUID.randomUUID().toString();
    return uuid + "_" + originalFilename.replace(" ", "_");
  }

  @PostConstruct
  private void initializePresigner() {
    this.presigner = S3Presigner.builder()
        .region(Region.of(region))
        .credentialsProvider(DefaultCredentialsProvider.create())
        .build();
  }

  private String generatePresignedUploadUrl(String fileName, String contentType) {
    String key = generateUniqueKey(fileName);

    PutObjectRequest objectRequest = PutObjectRequest.builder()
        .bucket(bucketName)
        .key(key)
        .contentType(contentType)
        .build();

    PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
        .signatureDuration(Duration.ofMinutes(10))
        .putObjectRequest(objectRequest)
        .build();

    PresignedPutObjectRequest presignedRequest = presigner.presignPutObject(presignRequest);
    return presignedRequest.url().toString();
  }

  public PresignedUrlResponse generatePresignedUrl(PresignedUrlRequest request) {
    String url = generatePresignedUploadUrl(request.getFileName(), request.getContentType());
    PresignedUrlResponse response = new PresignedUrlResponse();
    response.setUrl(url);
    response.setKey(generateUniqueKey(request.getFileName()));

    return response;
  }
}
