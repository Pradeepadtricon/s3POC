/**
 * 
 */
package com.souro.file_upload.model;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author
 *
 */
public class UploadModel {
	private String extraField;

	private MultipartFile file;

	public String getExtraField() {
		return extraField;
	}

	public void setExtraField(String extraField) {
		this.extraField = extraField;
	}

	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}

	@Override
	public String toString() {
		return "UploadModel{" + "extraField='" + extraField + '\'' + ", files=" + file + '}';
	}
}
