package com.souro.file_upload.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.transfer.MultipleFileDownload;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;

import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.souro.file_upload.aws.AmazonClient;
import com.amazonaws.services.s3.model.S3ObjectSummary;




/**
 * @author
 *
 */

@Service("uploadService")
public class UploadService {
	private static final String SUFFIX = "/";
	@Autowired
	AmazonClient amazonClient;

	@Autowired
	ZipFiles zipFiles;

	public String saveUploadedFile(MultipartFile[] multipartFile,String courseid) {

	    String fileUrl = "";
			try {
				createFolder(amazonClient.getBucketName(), courseid, amazonClient.getS3client());
				for (int i = 0; i < multipartFile.length; i++) {
					File file = convertMultiPartToFile(multipartFile[i]);
					String fileName = generateFileName(multipartFile[i], courseid);
					fileUrl = amazonClient.getEndpointUrl() + "/" + amazonClient.getBucketName() + "/" + fileName;
					uploadFileTos3bucket(fileName, file);
					file.delete();
				}

			} catch (Exception e) {
				e.printStackTrace();

		}
	    return "success";
	}

	public static void createFolder(String bucketName, String folderName, AmazonS3 client) {
		// create meta-data for your folder and set content-length to 0
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(0);
		// create empty content
		InputStream emptyContent = new ByteArrayInputStream(new byte[0]);
		// create a PutObjectRequest passing the folder name suffixed by /
		PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName,
			folderName , emptyContent, metadata);
		// send request to S3 to create folder
		client.putObject(putObjectRequest);
	}

	private File convertMultiPartToFile(MultipartFile file) throws IOException {
	    File convFile = new File(file.getOriginalFilename());
	    FileOutputStream fos = new FileOutputStream(convFile);
	    fos.write(file.getBytes());
	    fos.close();
	    return convFile;
	}
	private String generateFileName(MultipartFile multiPart,String folderName) {
	    return folderName + SUFFIX + multiPart.getOriginalFilename();
	}
	private void uploadFileTos3bucket(String fileName, File file) {
		amazonClient.getS3client().putObject(new PutObjectRequest(amazonClient.getBucketName(), fileName, file));
	            //.withCannedAcl(CannedAccessControlList.PublicRead));
	}

	public void deleteFiles(String folderName,List<String> fileNames) {
		String fileUrl=amazonClient.getS3client().getUrl(amazonClient.getBucketName(),folderName).toString();
		List<S3ObjectSummary> fileList =
			amazonClient.getS3client().listObjects(amazonClient.getBucketName(), folderName).getObjectSummaries();
		for(String eachFileName:fileNames) {
			for (S3ObjectSummary file : fileList) {
					if(file.getKey().contains(SUFFIX)) {
						if(eachFileName.equals(file.getKey().substring(file.getKey().lastIndexOf(SUFFIX) + 1))) {
							amazonClient.getS3client().deleteObject(amazonClient.getBucketName(), file.getKey());
							break;
						}
					}

			}
		}
		fileList = amazonClient.getS3client().listObjects(amazonClient.getBucketName(), folderName).getObjectSummaries();
		if(fileList.size()==1) {
			amazonClient.getS3client().deleteObject(amazonClient.getBucketName(), folderName);
		}
	}

	public File getSingleFile(String courseid , String fileName) {
		//List<File> fileList = new ArrayList<File>();
		//List<S3Object> s3ObjectList = new ArrayList<S3Object>();
		//List<S3ObjectSummary> s3ObjectSummaryList =
		//	amazonClient.getS3client().listObjects(amazonClient.getBucketName(), courseid).getObjectSummaries();
		//for (S3ObjectSummary file : s3ObjectSummaryList) {
		//	if(!file.getKey().contains(SUFFIX))
		//		continue;
		//	String fileName = file.getKey().substring(file.getKey().lastIndexOf(SUFFIX) + 1);
		//	s3ObjectList.add(amazonClient.getS3client().getObject(amazonClient.getBucketName(),file.getKey()));
		//	File newFile = new File("");
		//}
		//return fileList;
		String filePath = courseid + SUFFIX + fileName;
		File newFile = new File(fileName);
		try {
			S3Object s3Object = amazonClient.getS3client().getObject(amazonClient.getBucketName(), filePath);
			InputStream inputStream = s3Object.getObjectContent();
			//FileUtils.copyInputStreamToFile(inputStream, new File("/Users/user/Desktop/hello.txt"));
			InputStream in = s3Object.getObjectContent();
			OutputStream out = new FileOutputStream(newFile);
			IOUtils.copy(in,out);
			out.close();
			in.close();
		} catch (FileNotFoundException e) {
			return null;
		}
		catch (IOException e){
			return null;
		}
		return newFile;
	}

	public File getAllFiles(String courseid) {
		boolean pause = false;
		System.out.println("downloading to directory: " + courseid +
			(pause ? " (pause)" : ""));

		// snippet-start:[s3.java1.s3_xfer_mgr_download.directory]
		//TransferManager xfer_mgr = TransferManagerBuilder.standard().build();
		TransferManager xfer_mgr = new TransferManager(amazonClient.getS3client());


		try {
			MultipleFileDownload xfer = xfer_mgr.downloadDirectory(
				amazonClient.getBucketName(), courseid, new File(courseid));
			xfer.waitForCompletion();
			// loop with Transfer.isDone()
			//XferMgrProgress.showTransferProgress(xfer);
			// or block with Transfer.waitForCompletion()
			//XferMgrProgress.waitForCompletion(xfer);
			//xfer_mgr.shutdownNow();

			File downloadedPath = new File(courseid+SUFFIX+SUFFIX+courseid);
			String zipFileName = downloadedPath.getName().concat(".zip");
			/*FileOutputStream fos = new FileOutputStream(zipFileName);
			ZipOutputStream zos = new ZipOutputStream(fos);
			zos.putNextEntry(new ZipEntry(downloadedPath.getName()));
			//byte[] bFile = Files.readAllBytes(Paths.get(courseid+SUFFIX+courseid));
			byte[] bytesArray = new byte[(int) downloadedPath.length()];

			FileInputStream fis = new FileInputStream(downloadedPath);
			fis.read(bytesArray); //read file into bytes[]
			fis.close();

			zos.write(bytesArray, 0, bytesArray.length);
			zos.closeEntry();
			zos.close();*/
			File zippedFile = new File(zipFileName);
			zippedFile.delete();
			zipFiles.zipDirectory(downloadedPath,zipFileName);
			File zipFile = new File(zipFileName);
			return zipFile;

		} catch (Exception e) {
			e.printStackTrace();
		}


		return null;
	}

}
