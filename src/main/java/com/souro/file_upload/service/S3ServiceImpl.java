package com.souro.file_upload.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.services.s3.transfer.MultipleFileDownload;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.souro.file_upload.aws.AmazonClient;
import com.souro.file_upload.exception.LMSServiceException;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * The type Upload service.
 */
@Service("uploadService")
public class S3ServiceImpl {

    /**
     * The constant SUFFIX.
     */
    private static final String SUFFIX = "/";

    /**
     * The constant COURSE.
     */
    public static final String COURSE = "Course_";


    /**
     * The Amazon client.
     */
    @Autowired
    AmazonClient amazonClient;

    /**
     * The Zip files.
     */
    @Autowired
    ZipFilesServiceImpl zipFilesServiceImpl;

    /**
     * Save uploaded file string.
     *
     * @param multipartFile the multipart file
     * @param courseid      the courseid
     * @return the string
     */
    public String saveUploadedFile(MultipartFile[] multipartFile, String courseid)  throws LMSServiceException {
        String fileUrl = "";
        try {
            createFolder(amazonClient.getBucketName(), COURSE+courseid, amazonClient.getS3client());
            for (int i = 0; i < multipartFile.length; i++) {
                File file = convertMultiPartToFile(multipartFile[i]);
                String fileName = generateFileName(multipartFile[i], courseid);
                fileUrl = fileUrl + amazonClient.getBucketName() + "/" + fileName+"\n- ";
                uploadFileTos3bucket(fileName, file);
                file.delete();
            }

        } catch (Exception e) {
            throw new LMSServiceException(e.getMessage());

        }
        return fileUrl;
    }

    /**
     * Create folder.
     *
     * @param bucketName the bucket name
     * @param folderName the folder name
     * @param client     the client
     */
    private static void createFolder(String bucketName, String folderName, AmazonS3 client) {
        // create meta-data for your folder and set content-length to 0
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(0);
        // create empty content
        InputStream emptyContent = new ByteArrayInputStream(new byte[0]);
        // create a PutObjectRequest passing the folder name suffixed by /
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName,
            folderName, emptyContent, metadata);
        // send request to S3 to create folder
        client.putObject(putObjectRequest);
    }

    /**
     * Convert multi part to file file.
     *
     * @param file the file
     * @return the file
     * @throws IOException the io exception
     */
    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }

    /**
     * Generate file name string.
     *
     * @param multiPart  the multi part
     * @param folderName the folder name
     * @return the string
     */
    private String generateFileName(MultipartFile multiPart, String folderName) {
        return COURSE + folderName + SUFFIX + multiPart.getOriginalFilename();
    }

    /**
     * Upload file tos 3 bucket.
     *
     * @param fileName the file name
     * @param file     the file
     */
    private void uploadFileTos3bucket(String fileName, File file) {
        amazonClient.getS3client().putObject(new PutObjectRequest(amazonClient.getBucketName(), fileName, file));
    }

    /**
     * Delete files.
     *
     * @param folderName the folder name
     * @param fileNames  the file names
     */
    public void deleteFiles(String folderName, List<String> fileNames) {
        String fileUrl = amazonClient.getS3client().getUrl(amazonClient.getBucketName(), COURSE+folderName).toString();
        List<S3ObjectSummary> fileList =
            amazonClient.getS3client().listObjects(amazonClient.getBucketName(), COURSE+folderName).getObjectSummaries();
        for (String eachFileName : fileNames) {
            for (S3ObjectSummary file : fileList) {
                if (file.getKey().contains(SUFFIX)) {
                    if (eachFileName.equals(file.getKey().substring(file.getKey().lastIndexOf(SUFFIX) + 1))) {
                        amazonClient.getS3client().deleteObject(amazonClient.getBucketName(), file.getKey());
                        break;
                    }
                }

            }
        }
        fileList = amazonClient.getS3client().listObjects(amazonClient.getBucketName(), COURSE+folderName).getObjectSummaries();
        if (fileList.size() == 1) {
            amazonClient.getS3client().deleteObject(amazonClient.getBucketName(), COURSE+folderName);
        }
    }

    /**
     * Gets single file.
     *
     * @param courseid the courseid
     * @param fileName the file name
     * @return the single file
     */
    public File getSingleFile (String courseid, String fileName) throws LMSServiceException {
        String filePath = COURSE + courseid + SUFFIX + fileName;
        File newFile = new File(fileName);
        try {
            S3Object s3Object = amazonClient.getS3client().getObject(amazonClient.getBucketName(), filePath);
            InputStream inputStream = s3Object.getObjectContent();
            InputStream in = s3Object.getObjectContent();
            OutputStream out = new FileOutputStream(newFile);
            IOUtils.copy(in, out);
            out.close();
            in.close();
        } catch (FileNotFoundException e) {
            throw new LMSServiceException(e.getMessage());
        } catch (IOException e) {
            throw new LMSServiceException(e.getMessage());
        }
        return newFile;
    }

    /**
     * Gets all files.
     *
     * @param courseid the courseid
     * @return the all files
     */
    public File getAllFiles(String courseid) throws LMSServiceException{
        TransferManager xfer_mgr = new TransferManager(amazonClient.getS3client());
        try {
            MultipleFileDownload xfer = xfer_mgr.downloadDirectory(
                amazonClient.getBucketName(), COURSE+courseid, new File(COURSE+courseid));
            xfer.waitForCompletion();
            //xfer_mgr.shutdownNow();

            File downloadedPath = new File(COURSE + courseid + SUFFIX + SUFFIX + COURSE + courseid);
            String zipFileName = downloadedPath.getName().concat(".zip");
            File zippedFile = new File(zipFileName);
            zippedFile.delete();
            zipFilesServiceImpl.zipDirectory(downloadedPath, zipFileName);
            File zipFile = new File(zipFileName);
            return zipFile;

        } catch (InterruptedException e) {
            throw new LMSServiceException(e.getMessage());
        }
    }

}
