package com.souro.file_upload.controller;

import com.souro.file_upload.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.net.MalformedURLException;
import java.util.List;

/**
 * The type Upload controller.
 */
@RestController
public class UploadController {

    /**
     * The Upload service.
     */
    @Autowired
    UploadService uploadService;

    /**
     * Upload file response entity.
     *
     * @param uploadfiles the uploadfiles
     * @param courseId    the course id
     * @return the response entity
     */
    @PostMapping("/api/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile[] uploadfiles,
                                        @RequestParam("courseid") String courseId) {
        if (uploadfiles.length == 0 || isEmptyAny(uploadfiles)) {
            return new ResponseEntity<Object>("Please select a file!", HttpStatus.OK);
        }
        uploadService.saveUploadedFile(uploadfiles, courseId);
        return new ResponseEntity<Object>("Successfully uploaded - " + uploadfiles[0].getOriginalFilename(),
                new HttpHeaders(), HttpStatus.OK);
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
     * @param folderName the folder name
     * @param fileNames  the file names
     * @return the response entity
     */
    @DeleteMapping("/deleteFile")
    public ResponseEntity<?> deleteFile(@RequestParam(name = "folderName") String folderName,
                                        @RequestParam(name = "fileNames") List<String> fileNames) {
        uploadService.deleteFiles(folderName, fileNames);
        return new ResponseEntity<Object>("Successfully Deleted", new HttpHeaders(), HttpStatus.OK);
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
                                                  @RequestParam(name = "fileName") String fileName) {
        String contentType = null;
        Resource resource = null;
        File file = null;
        try {
            file = uploadService.getSingleFile(courseId, fileName);

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

        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Gets all files.
     *
     * @param courseId the course id
     * @return the all files
     */
    @GetMapping("/getAllFiles")
    public ResponseEntity<Resource> getAllFiles(@RequestParam(name = "courseId") String courseId) {
        String contentType = null;
        Resource resource = null;
        File file = null;
        try {
            file = uploadService.getAllFiles(courseId);
            resource = new UrlResource(file.toURI());
            contentType = "application/octet-stream";
            long len = file.length();
            //file.deleteOnExit();
            return ResponseEntity
                    .ok()
                    .contentType(MediaType.parseMediaType(contentType)).contentLength(len)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);

        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

}
