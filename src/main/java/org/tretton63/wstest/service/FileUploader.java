package org.tretton63.wstest.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListObjectsRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsResponse;
import software.amazon.awssdk.services.s3.model.S3Object;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FileUploader {

    S3Client s3Client;
    private static final Logger logger = LoggerFactory.getLogger(FileUploader.class);

    public FileUploader(S3Client s3Client) {
        this.s3Client = s3Client;
    }


    public List<String> listBucket(String bucketName) {
        ListObjectsResponse response = s3Client.listObjects(
                ListObjectsRequest.builder()
                        .bucket(bucketName)
                        .build());
        return response.contents().stream()
                .map(S3Object::key)
                .collect(Collectors.toList());
    }


}
