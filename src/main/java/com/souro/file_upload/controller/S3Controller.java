package com.souro.file_upload.controller;

import com.souro.file_upload.constants.Constants;
import com.souro.file_upload.exception.LMSDaoException;
import com.souro.file_upload.exception.LMSServiceException;
import com.souro.file_upload.model.RestResponseEntity;
import com.souro.file_upload.service.S3ServiceImpl;
import java.io.File;
import java.net.MalformedURLException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * The type Upload controller.
 */
@RequestMapping("s3")
@RestController
public class S3Controller {
    /**
     * The Upload service.
     */
    @Autowired
    S3ServiceImpl s3ServiceImpl;

    /**
     * The Rest response entity.
     */
    private RestResponseEntity restResponseEntity;


    /**
     * Upload file response entity.
     *
     * @param uploadfiles the uploadfiles
     * @param courseId    the course id
     * @return the response entity
     */
    @PostMapping("/api/upload")
    public ResponseEntity<RestResponseEntity> uploadFile(@RequestParam(value="file", required=false) MultipartFile[] uploadfiles,
                                                         @RequestParam("courseid") String courseId) {
        try {
            restResponseEntity = new RestResponseEntity();
            if (uploadfiles==null || uploadfiles.length == 0 || isEmptyAny(uploadfiles)) {
                restResponseEntity.add("message", "\"Please select a file!\"");
                return new ResponseEntity<RestResponseEntity>(restResponseEntity, HttpStatus.BAD_REQUEST);
            }
            String result = s3ServiceImpl.saveUploadedFile(uploadfiles, courseId);
            restResponseEntity.add("message", "Successfully uploaded to- " + result);
            return new ResponseEntity<RestResponseEntity>(restResponseEntity, HttpStatus.OK);
        } catch (LMSServiceException e) {
            restResponseEntity.add("message", Constants.ERROR);
            restResponseEntity.setResult(null);
            return new ResponseEntity<RestResponseEntity>(restResponseEntity, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Is empty any boolean.
     *
     * @param uploadfiles the uploadfiles
     * @return the boolean
     */
    public boolean isEmptyAny(MultipartFile[] uploadfiles) {
        for (int i = 0; i < uploadfiles.length; i++) {
            if (uploadfiles[i].isEmpty()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Delete file response entity.
     *
     * @param courseId the course Id
     * @param fileNames  the file names
     * @return the response entity
     */
    @DeleteMapping("/deleteFile")
    public ResponseEntity<RestResponseEntity> deleteFile(@RequestParam(name = "courseId") String courseId,
                                        @RequestParam(name = "fileNames") List<String> fileNames) {
        try {
            restResponseEntity = new RestResponseEntity();
            s3ServiceImpl.deleteFiles(courseId, fileNames);
            restResponseEntity.add("message", "Successfully Deleted.");
            return new ResponseEntity<RestResponseEntity>(restResponseEntity, HttpStatus.OK);
        }
        catch (LMSServiceException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
        }
    }

    /**
     * Gets single file.
     *
     * @param courseId the course id
     * @param fileName the file name
     * @return the single file
     */
    @GetMapping("/getSingleFile")
    public ResponseEntity<Resource> getSingleFile(@RequestParam(name = "courseId") String courseId,
                                                  @RequestParam(name = "fileName") String fileName)
        throws MalformedURLException {
        String contentType = null;
        Resource resource = null;
        File file = null;
        try {
            file = s3ServiceImpl.getSingleFile(courseId, fileName);

            resource = new UrlResource(file.toURI());
            contentType = "application/octet-stream";
            long len = file.length();
            file.deleteOnExit();
            return ResponseEntity
                .ok()
                .contentType(MediaType.parseMediaType(contentType))
                .contentLength(len)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                .body(resource);

        } catch (LMSServiceException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
        }

    }

    /**
     * Gets all files.
     *
     * @param courseId the course id
     * @return the all files
     */
    @GetMapping("/getAllFiles")
    public ResponseEntity<Resource> getAllFiles(@RequestParam(name = "courseId") String courseId)
        throws MalformedURLException {
        String contentType = null;
        Resource resource = null;
        File file = null;
        try {
            file = s3ServiceImpl.getAllFiles(courseId);
            resource = new UrlResource(file.toURI());
            contentType = "application/octet-stream";
            long len = file.length();
            //file.deleteOnExit();
            return ResponseEntity
                .ok()
                .contentType(MediaType.parseMediaType(contentType)).contentLength(len)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);

        } catch (LMSServiceException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
        }
    }

    /**
     * Gets file names.
     *
     * @param courseId the course id
     * @return the file names
     */
    @GetMapping("/getFileNames")
    public ResponseEntity<RestResponseEntity> getFileNames(@RequestParam(name = "courseId") String courseId){
        try {
            restResponseEntity = new RestResponseEntity();
            List<String> fileNames = s3ServiceImpl.getAllFileNames(courseId);
            restResponseEntity.add("message", Constants.SUCCESS);
            restResponseEntity.setResult(fileNames);
            return new ResponseEntity<RestResponseEntity>(restResponseEntity, HttpStatus.OK);
        }
        catch (LMSServiceException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
        }
    }

}
