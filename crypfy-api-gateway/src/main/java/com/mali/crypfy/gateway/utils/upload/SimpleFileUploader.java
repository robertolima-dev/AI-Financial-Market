package com.mali.crypfy.gateway.utils.upload;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.mali.crypfy.gateway.aws.s3.enums.S3UploadPermission;

import java.io.File;
import java.io.InputStream;

public interface SimpleFileUploader {

    final static String S3_SA_EAST_RESOURCE = "https://s3-sa-east-1.amazonaws.com/";

    public void upload(String bucketName,String key,File file) throws SimpleFileUploaderException;
    public void upload(String bucketName, String key, InputStream inputStream, ObjectMetadata metadata) throws SimpleFileUploaderException;
    public InputStream getObject(String bucketName, String keyName) throws SimpleFileUploaderException;
    public PutObjectResult upload(String bucketName ,String key ,File file, S3UploadPermission permission) throws SimpleFileUploaderException;
}
