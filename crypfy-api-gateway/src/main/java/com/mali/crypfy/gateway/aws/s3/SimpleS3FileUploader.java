package com.mali.crypfy.gateway.aws.s3;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import com.mali.crypfy.gateway.aws.s3.enums.S3UploadPermission;
import com.mali.crypfy.gateway.services.user.impl.UserServiceImpl;
import com.mali.crypfy.gateway.utils.upload.SimpleFileUploader;
import com.mali.crypfy.gateway.utils.upload.SimpleFileUploaderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.InputStream;

@Component
public class SimpleS3FileUploader implements SimpleFileUploader {

    final static Logger logger = LoggerFactory.getLogger(SimpleS3FileUploader.class);

    @Value("${spring.aws.s3.access-key}")
    private String accessKey;
    @Value("${spring.aws.s3.secret-key}")
    private String secretKey;

    private AmazonS3 s3Client;

    @PostConstruct
    private void init() {
        BasicAWSCredentials credentials = new BasicAWSCredentials(accessKey,secretKey);
        s3Client = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials)).withRegion(Regions.SA_EAST_1).build();
    }

    @Override
    public void upload(String bucketName ,String key ,File file) throws SimpleFileUploaderException {
        try {
            s3Client.putObject(new PutObjectRequest(bucketName,key,file));
        }catch (Exception e) {
            logger.error("error on upload file",e);
            throw new SimpleFileUploaderException("upload file error");
        }
    }

    @Override
    public void upload(String bucketName, String key, InputStream inputStream, ObjectMetadata metadata) throws SimpleFileUploaderException {
        try {
            s3Client.putObject(new PutObjectRequest(bucketName,key,inputStream,metadata));
        }catch (Exception e) {
            logger.error("error on upload file",e);
            throw new SimpleFileUploaderException("upload file error");
        }
    }

    @Override
    public InputStream getObject(String bucketName, String keyName) throws SimpleFileUploaderException {
        S3Object object = s3Client.getObject(new GetObjectRequest(bucketName,keyName));
        return object.getObjectContent();
    }

    @Override
    public PutObjectResult upload(String bucketName ,String key ,File file, S3UploadPermission permission) throws SimpleFileUploaderException {
        try {
            ObjectMetadata objectMetadata = new ObjectMetadata();
            PutObjectResult putObjectResult = null;
            if(permission == S3UploadPermission.PUBLIC)
                putObjectResult = s3Client.putObject(new PutObjectRequest(bucketName,key,file).withCannedAcl(CannedAccessControlList.PublicRead));
            else
                putObjectResult = s3Client.putObject(new PutObjectRequest(bucketName,key,file));
            return putObjectResult;
        } catch (AmazonServiceException e) {
            logger.error("aws upload error",e);
            throw new SimpleFileUploaderException("ocorreu um erro ao realizar upload do arquivo");
        } catch (AmazonClientException e) {
            logger.error("aws upload error",e);
            throw new SimpleFileUploaderException("ocorreu um erro ao realizar upload do arquivo");
        }
    }
}
