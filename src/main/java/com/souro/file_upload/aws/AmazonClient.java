package com.souro.file_upload.aws;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * The type Amazon client.
 */
@Service
public class AmazonClient {

    /**
     * The S 3 client.
     */
    private AmazonS3 s3client;

    /**
     * The Endpoint url.
     */
    @Value("${amazonProperties.endpointUrl}")
    private String endpointUrl;
    /**
     * The Bucket name.
     */
    @Value("${amazonProperties.bucketName}")
    private String bucketName;
    /**
     * The Access key.
     */
    @Value("${amazonProperties.accessKey}")
    private String accessKey;
    /**
     * The Secret key.
     */
    @Value("${amazonProperties.secretKey}")
    private String secretKey;

    /**
     * Gets s 3 client.
     *
     * @return the s 3 client
     */
    public AmazonS3 getS3client() {
        return s3client;
    }

    /**
     * Sets s 3 client.
     *
     * @param s3client the s 3 client
     */
    public void setS3client(AmazonS3 s3client) {
        this.s3client = s3client;
    }

    /**
     * Gets endpoint url.
     *
     * @return the endpoint url
     */
    public String getEndpointUrl() {
        return endpointUrl;
    }

    /**
     * Sets endpoint url.
     *
     * @param endpointUrl the endpoint url
     */
    public void setEndpointUrl(String endpointUrl) {
        this.endpointUrl = endpointUrl;
    }

    /**
     * Gets bucket name.
     *
     * @return the bucket name
     */
    public String getBucketName() {
        return bucketName;
    }

    /**
     * Sets bucket name.
     *
     * @param bucketName the bucket name
     */
    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    /**
     * Gets access key.
     *
     * @return the access key
     */
    public String getAccessKey() {
        return accessKey;
    }

    /**
     * Sets access key.
     *
     * @param accessKey the access key
     */
    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    /**
     * Gets secret key.
     *
     * @return the secret key
     */
    public String getSecretKey() {
        return secretKey;
    }

    /**
     * Sets secret key.
     *
     * @param secretKey the secret key
     */
    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    /**
     * Initialize amazon.
     */
    @PostConstruct
    private void initializeAmazon() {
        AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
        this.s3client = new AmazonS3Client(credentials);
    }
}
