/**
 * 
 */
package com.souro.file_upload.controller;

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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.souro.file_upload.service.UploadService;

/**
 * @author
 *
 */
@RestController
public class UploadController {
	@Autowired
	UploadService uploadService;

	@PostMapping("/api/upload")
	public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile[] uploadfiles,
										@RequestParam("courseid") String courseId) {

		if (uploadfiles.length == 0 || isEmptyAny(uploadfiles)) {
			return new ResponseEntity<Object>("please select a file!", HttpStatus.OK);
		}

		uploadService.saveUploadedFile(uploadfiles, courseId);

		return new ResponseEntity<Object>("Successfully uploaded - " + uploadfiles[0].getOriginalFilename(),
			new HttpHeaders(), HttpStatus.OK);

	}

	public boolean isEmptyAny(MultipartFile[] uploadfiles) {
		for (int i = 0; i < uploadfiles.length; i++) {
			if (uploadfiles[i].isEmpty()) {
				return true;
			}
		}
		return false;
	}

	@DeleteMapping("/deleteFile")
	public ResponseEntity<?> deleteFile(@RequestParam(name = "folderName") String folderName,
										@RequestParam(name = "fileNames") List<String> fileNames) {
		uploadService.deleteFiles(folderName, fileNames);
		return new ResponseEntity<Object>("Successfully Deleted", new HttpHeaders(), HttpStatus.OK);
	}

	@GetMapping("/getSingleFile")
	public ResponseEntity<Resource> getSingleFile(@RequestParam(name = "courseId") String courseId,
											@RequestParam(name = "fileName") String fileName) {
		String contentType = null;
		Resource resource = null;
		File file = null;
		try {
//			if (accessService.hasPrivilige(request.getHeader("Email"), "review-file")) {
			file = uploadService.getSingleFile(courseId, fileName);

			resource = new UrlResource(file.toURI());
			contentType = "application/octet-stream";
			long len = file.length();
			file.deleteOnExit();
			return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType)).contentLength(len)
				.header(HttpHeaders.CONTENT_DISPOSITION,
					"inline; filename=\"" + resource.getFilename() + "\"")
				.body(resource);

		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}
	}

	@GetMapping("/getAllFiles")
	public ResponseEntity<Resource> getAllFiles(@RequestParam(name = "courseId") String courseId) {
		String contentType = null;
		Resource resource = null;
		File file = null;
		try {
//			if (accessService.hasPrivilige(request.getHeader("Email"), "review-file")) {
			file = uploadService.getAllFiles(courseId);

			resource = new UrlResource(file.toURI());
			contentType = "application/octet-stream";
			long len = file.length();
			//file.deleteOnExit();
			return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType)).contentLength(len)
				.header(HttpHeaders.CONTENT_DISPOSITION,
					"attachment; filename=\"" + resource.getFilename() + "\"")
				.body(resource);

		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}
	}

}
